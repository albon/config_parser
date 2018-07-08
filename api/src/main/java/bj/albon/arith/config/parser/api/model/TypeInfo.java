package bj.albon.arith.config.parser.api.model;

import java.lang.reflect.Type;

/**
 * @author albon
 *         Date : 17-1-24
 *         Time: 下午2:17
 */
public class TypeInfo {

    private Type type;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TypeInfo typeInfo = (TypeInfo) o;

        if (type != null ? !type.equals(typeInfo.type) : typeInfo.type != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TypeInfo{");
        sb.append("type=").append(type);
        sb.append('}');
        return sb.toString();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
