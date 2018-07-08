package bj.albon.arith.config.parser.api.annotation;

import bj.albon.arith.config.parser.api.model.ParserType;
import bj.albon.arith.config.parser.api.service.FieldParser;
import bj.albon.arith.config.parser.api.service.InvalidListener;
import bj.albon.arith.config.parser.api.service.Listener;
import bj.albon.arith.config.parser.api.service.impl.NullFieldParser;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author albon
 *         Date : 17-1-19
 *         Time: 上午11:02
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoParseField {
    // field 对应的QConfig配置的 key
    String key();

    // 解析类型，单个kv解析，或者一组kv解析
    ParserType type() default ParserType.SINGLE;

    Class<? extends FieldParser> parser() default NullFieldParser.class;

    Class<? extends Listener> listener() default InvalidListener.class;
}
