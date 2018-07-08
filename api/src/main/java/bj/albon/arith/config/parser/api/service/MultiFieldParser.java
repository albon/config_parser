package bj.albon.arith.config.parser.api.service;

import java.util.Map;

/**
 * @author albon
 *         Date: 17-1-19
 *         Time: 上午11:30
 */
public abstract class MultiFieldParser<T> extends TypeCapture<T> implements FieldParser {

    /**
     * 输入多个配置参数，返回一个解析后的结果
     * 
     * @param configMap 配置参数map
     * @param key 配置关键字
     * @return
     */
    public abstract T parse(Map<String, String> configMap, String key);

}
