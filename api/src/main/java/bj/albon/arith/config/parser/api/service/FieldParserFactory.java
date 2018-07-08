package bj.albon.arith.config.parser.api.service;

import bj.albon.arith.config.parser.api.service.impl.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import bj.albon.arith.config.parser.api.annotation.AutoParseField;
import bj.albon.arith.config.parser.api.exception.ConfigParseException;
import bj.albon.arith.config.parser.api.model.ParserType;
import bj.albon.arith.config.parser.api.util.FieldUtil;
import bj.albon.arith.config.parser.api.util.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author albon
 *         Date : 17-1-19
 *         Time: 下午5:38
 */
public class FieldParserFactory {
    private static final Logger logger = LoggerFactory.getLogger(FieldParserFactory.class);

    // key: FieldParser.class
    private static Map<Class, FieldParser> fieldParserMap = Maps.newConcurrentMap();

    // key: typeInfo.toString or className
    private static Map<Type, List<FieldParser>> typeInfoFieldParserMap = Maps.newConcurrentMap();

    static {
        init();
    }

    public static void init() {
        try {
            List<FieldParser> fieldParserList = buildDefaultFieldParser();

            for (FieldParser fieldParser : fieldParserList) {
                addFieldParser(fieldParser);
            }

            logger.info("fieldParserMap.keySet: {}", JsonUtil.toString(fieldParserMap.keySet()));
        } catch (Throwable e) {
            logger.error("field parser factory error", e);
            throw new RuntimeException("field parser factory error", e);
        }
    }

    public static void addFieldParser(FieldParser fieldParser) {
        fieldParserMap.put(fieldParser.getClass(), fieldParser);
        addToTypeInfoFieldParserMap(typeInfoFieldParserMap, buildTypeInfo((TypeCapture) fieldParser), fieldParser);
    }

    private static List<FieldParser> buildDefaultFieldParser() {
        List<FieldParser> fieldParserList = Lists.newArrayList();

        fieldParserList.add(new BigDecimalFieldParser());
        fieldParserList.add(new BooleanFieldParser());
        fieldParserList.add(new DoubleFieldParser());
        fieldParserList.add(new FloatFieldParser());
        fieldParserList.add(new IntFieldParser());
        fieldParserList.add(new IntListFieldParser());
        fieldParserList.add(new IntSetFieldParser());
        fieldParserList.add(new LongFieldParser());
        fieldParserList.add(new PostFixMapFieldParser());
        fieldParserList.add(new ShortFieldParser());
        fieldParserList.add(new StringFieldParser());
        fieldParserList.add(new StringListFieldParser());
        fieldParserList.add(new StringMapFieldParser());
        fieldParserList.add(new StringSetFieldParser());

        return fieldParserList;
    }

    private static void addToTypeInfoFieldParserMap(Map<Type, List<FieldParser>> typeInfoFieldParserMap,
            Type typeInfo, FieldParser fieldParser) {
        if (typeInfoFieldParserMap.containsKey(typeInfo)) {
            typeInfoFieldParserMap.get(typeInfo).add(fieldParser);
        } else {
            typeInfoFieldParserMap.put(typeInfo, Lists.newArrayList(fieldParser));
        }
    }

    private static Type buildTypeInfo(TypeCapture typeCapture) {
        return typeCapture.capture();
    }

    public static FieldParser chooseSuitableFieldParser(ParserType parserType,
            AutoParseField autoParseField, Field field, Class<?> parentClass) throws ConfigParseException {
        if (autoParseField.parser().equals(NullFieldParser.class)) {
            String fieldClassName = StringUtils.join(parentClass.getName(), "#", field.getName());

            Type fieldType = FieldUtil.getType(field);
            List<FieldParser> fieldParserList = typeInfoFieldParserMap.get(fieldType);
            Preconditions.checkArgument(CollectionUtils.isNotEmpty(fieldParserList),
                    StringUtils.join("field ", fieldClassName, " can't find parser class"));

            List<FieldParser> suitableFieldParserList = Lists.newArrayList();
            for (FieldParser fieldParser : fieldParserList) {
                if (fieldParser instanceof SingleFieldParser && parserType.equals(ParserType.SINGLE)) {
                    suitableFieldParserList.add(fieldParser);
                } else if (fieldParser instanceof MultiFieldParser && parserType.equals(ParserType.MULTI)) {
                    suitableFieldParserList.add(fieldParser);
                }
            }

            if (suitableFieldParserList.size() == 0) {
                throw new ConfigParseException(StringUtils.join("field ", fieldClassName,
                        " has no suitable parse class\n", "字段 ", fieldClassName, " 找不到对应的解析类"));
            } else if (suitableFieldParserList.size() == 1) {
                return suitableFieldParserList.get(0);
            } else {
                throw new ConfigParseException(StringUtils.join("field ", fieldClassName, " has ",
                        suitableFieldParserList.size(), " suitable parse class: ",
                        serializeFieldParserList(suitableFieldParserList), ", please specify AutoParseField.parser\n",
                        "字段 ", fieldClassName, " 对应多个解析类，请为 AutoParseField 注解的 parser 字段设置正确的解析类"));
            }
        } else {
            return fieldParserMap.get(autoParseField.parser());
        }
    }

    private static String serializeFieldParserList(List<FieldParser> suitableFieldParserList) {
        StringBuilder content = new StringBuilder();

        for (FieldParser fieldParser : suitableFieldParserList) {
            content.append(fieldParser.getClass().getName()).append(", ");
        }

        return content.substring(0, content.length() - 2);
    }
}
