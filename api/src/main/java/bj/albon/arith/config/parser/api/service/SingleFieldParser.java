package bj.albon.arith.config.parser.api.service;

/**
 * @author albon
 *         Date: 17-1-19
 *         Time: 上午11:30
 */
public abstract class SingleFieldParser<T> extends TypeCapture<T> implements FieldParser {

    /**
     * 输入一个配置value，返回解析后的对象
     * 
     * @param value 输入参数
     * @return
     */
    public abstract T parse(String value);
}
