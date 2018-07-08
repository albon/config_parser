package bj.albon.arith.config.parser.api.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import bj.albon.arith.config.parser.api.annotation.AutoParseField;
import bj.albon.arith.config.parser.api.exception.ConfigParseException;
import bj.albon.arith.config.parser.api.model.ParserType;
import bj.albon.arith.config.parser.api.util.ErrorUtil;
import bj.albon.arith.config.parser.api.util.InetAddressUtil;
import bj.albon.arith.config.parser.api.util.JsonUtil;
import bj.albon.arith.config.parser.api.util.MachineUtil;
import org.apache.bval.jsr.ApacheValidationProvider;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * @author albon
 *         Date : 17-1-20
 *         Time: 上午11:20
 */
public class ConfigParser {
    private static final Logger logger = LoggerFactory.getLogger(ConfigParser.class);

    private static Validator validator;
    static {
        ValidatorFactory avf = Validation.byProvider(ApacheValidationProvider.class).configure().buildValidatorFactory();
        validator = avf.getValidator();
    }

    /**
     * 解析配置和赋值
     * @param configMap             配置 map
     * @param configObject          待赋值的属性所在的类
     * @throws ConfigParseException
     */
    public static void parse(Map<String, String> configMap, Object configObject) throws ConfigParseException {
        Field currentField = null;
        Map<String, Object> oldValueMap = Maps.newHashMap();
        try {
            logger.info("configMap: {}, configObject: {}", JsonUtil.toString(configMap),
                    configObject.getClass().getName());
            Preconditions.checkNotNull(configMap, "输入的配置Map为NULL");
            Preconditions.checkNotNull(configObject, "待赋值的配置对象为空");

            Class<?> aClass = configObject.getClass();
            Field[] fields = aClass.getDeclaredFields();

            for (Field field : fields) {
                currentField = field;
                String fieldName = field.getName();

                AutoParseField fieldAnnotation = field.getAnnotation(AutoParseField.class);
                if (fieldAnnotation == null) {
                    logger.debug("field don't has annotation: {}", fieldName);
                    continue;
                }

                String key = fieldAnnotation.key();
                ParserType type = fieldAnnotation.type();
                field.setAccessible(true);

                Object oldValue = field.get(configObject);
                oldValueMap.put(fieldName, oldValue);
                logger.info("field.name: {}, type: {}, oldValue: {}", fieldName, field.getType(),
                        toJsonString(oldValue));

                if (type.equals(ParserType.SINGLE) && configMap.containsKey(key) == false) {
                    logger.warn("field.name: {}, key not exist: {}", fieldName, key);
                    continue;
                }

                parseAndSet(configMap, configObject, aClass, field, fieldAnnotation, key, type);

                logger.info("field: {}, newValue: {}", fieldName, toJsonString(field.get(configObject)));
            }

            // 参数校验
            Set<ConstraintViolation<Object>> constraintViolations = validator.validate(configObject);
            if (CollectionUtils.isNotEmpty(constraintViolations)) {
                throw new ConfigParseException(convertConstraintErrorToString(constraintViolations));
            }
        } catch (ConfigParseException e) {
            logger.error("配置解析异常 config auto parse error", e);
            ErrorInformer.inform(buildErrorMessage(e, configObject, currentField));
            rollbackConfig(oldValueMap, configObject);
            throw e;
        } catch (Throwable e) {
            logger.error("配置解析异常 config auto parse error", e);
            ErrorInformer.inform(buildErrorMessage(e, configObject, currentField));
            rollbackConfig(oldValueMap, configObject);
            throw new ConfigParseException("config auto parse error", e);
        }
    }

    private static void rollbackConfig(Map<String, Object> oldValueMap, Object configObject) {
        logger.warn("config error, rollback config");

        try {
            Class<?> aClass = configObject.getClass();
            Field[] fields = aClass.getDeclaredFields();

            for (Field field : fields) {
                AutoParseField fieldAnnotation = field.getAnnotation(AutoParseField.class);
                String fieldName = field.getName();
                if (fieldAnnotation == null || !oldValueMap.containsKey(fieldName)) {
                    continue;
                }

                field.setAccessible(true);
                field.set(configObject, oldValueMap.get(fieldName));
            }
        } catch (Exception e) {
            logger.error("rollback config error", e);
        }
    }

    public static void parseAndSet(Map<String, String> configMap, Object configObject, Class<?> aClass, Field field,
            AutoParseField fieldAnnotation, String key, ParserType type) throws ConfigParseException,
            IllegalAccessException {
        if (type.equals(ParserType.SINGLE)) {
            SingleFieldParser fieldParser = (SingleFieldParser) FieldParserFactory.chooseSuitableFieldParser(type,
                    fieldAnnotation, field, aClass);
            logger.info("field: {}, parser: {}", field.getName(), fieldParser.getClass());
            field.set(configObject, fieldParser.parse(configMap.get(key).trim()));
        } else if (type.equals(ParserType.MULTI)) {
            MultiFieldParser fieldParser = (MultiFieldParser) FieldParserFactory.chooseSuitableFieldParser(type,
                    fieldAnnotation, field, aClass);
            logger.info("field: {}, parser: {}", field.getName(), fieldParser.getClass());
            field.set(configObject, fieldParser.parse(configMap, key));
        } else {
            logger.error("ParseType Error value = {}", type);
            throw new ConfigParseException("ParseType Error value = " + type);
        }
    }

    private static String convertConstraintErrorToString(Set<ConstraintViolation<Object>> constraintViolations) {
        StringBuilder errorMessage = new StringBuilder("配置校验异常：\n");
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            errorMessage.append(constraintViolation.getMessage()).append(", field: ")
                    .append(constraintViolation.getPropertyPath()).append(", invalidValue: ")
                    .append(constraintViolation.getInvalidValue()).append("\n");
        }
        return errorMessage.toString();
    }

    private static String toJsonString(Object object) {
        if (object instanceof String) {
            return (String) object;
        }

        return JsonUtil.toString(object);
    }

    private static String buildErrorMessage(Throwable t, Object configObject, Field exceptionField) {
        StringBuilder messageBuilder = new StringBuilder();
        if (MachineUtil.isProd() == false) {
            messageBuilder.append("【beta】测试\n\n");
        }

        messageBuilder.append("配置解析异常\n\n");
        messageBuilder.append("应用AppCode: ").append(AppInfo.getINSTANCE().getAppName()).append("\n");
        messageBuilder.append("机器ip: ").append(InetAddressUtil.queryIp()).append("\n");
        messageBuilder.append("机器名: ").append(InetAddressUtil.queryHostName()).append("\n");

        messageBuilder.append("待赋值类: ").append(findObjectType(configObject)).append("\n");
        if (exceptionField != null) {
            messageBuilder.append("待赋值字段: ").append(exceptionField.getName()).append("\n");
        }

        messageBuilder.append("异常信息: \n").append(ErrorUtil.getStackTrace(t)).append("\n\n");

        return messageBuilder.toString();
    }

    private static String findObjectType(Object configObject) {
        if (configObject == null) {
            return null;
        }
        return configObject.getClass().getName();
    }

}
