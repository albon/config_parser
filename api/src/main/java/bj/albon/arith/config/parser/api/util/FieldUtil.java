package bj.albon.arith.config.parser.api.util;

import bj.albon.arith.config.parser.api.model.ClassReplace;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Created by albon on 17/3/5.
 */
public class FieldUtil {

    public static Type getType(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof Class) {
            Class<?> replaceClass = ClassReplace.convert((Class<?>) genericType);
            if (replaceClass != null) {
                return replaceClass;
            }
        }

        return genericType;
    }

}
