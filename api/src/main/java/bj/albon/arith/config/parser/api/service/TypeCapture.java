package bj.albon.arith.config.parser.api.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Captures the actual type of {@code T}.
 * 
 * @author gson
 *         Date : 17-1-24
 *         Time: 下午2:06
 */
public abstract class TypeCapture<T> {
    /** Returns the captured type. */
    public final Type capture() {
        Type superclass = getClass().getGenericSuperclass();
        checkArgument(superclass instanceof ParameterizedType, "%s isn't parameterized", superclass);
        return ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }
}
