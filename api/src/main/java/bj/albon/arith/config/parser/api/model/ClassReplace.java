package bj.albon.arith.config.parser.api.model;

/**
 * @author albon
 *         Date : 17-1-22
 *         Time: 下午3:30
 */
public enum ClassReplace {
    SHORT(short.class, Short.class), BYTE(byte.class, Byte.class), CHAR(char.class, Character.class), INT(int.class,
            Integer.class), LONG(long.class, Long.class), FLOAT(float.class, Float.class), DOUBLE(double.class,
            Double.class), BOOLEAN(boolean.class, Boolean.class), ;

    public Class<?> from;
    public Class<?> to;

    ClassReplace(Class<?> from, Class<?> to) {
        this.from = from;
        this.to = to;
    }

    public static Class<?> convert(Class<?> original) {
        for (ClassReplace classReplace : values()) {
            if (classReplace.from.equals(original)) {
                return classReplace.to;
            }
        }

        return null;
    }

    public static Class<?> convert(String name) {
        for (ClassReplace classReplace : values()) {
            if (classReplace.from.getName().equals(name)) {
                return classReplace.to;
            }
        }

        return null;
    }
}
