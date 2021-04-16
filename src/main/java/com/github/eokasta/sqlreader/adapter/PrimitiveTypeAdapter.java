package com.github.eokasta.sqlreader.adapter;

public class PrimitiveTypeAdapter {

    public Class<?> getType(Class<?> clazz) {
        if (!clazz.isPrimitive())
            throw new IllegalArgumentException("clazz is not primitive type.");

        if (clazz.equals(Character.TYPE))
            return Character.class;
        if (clazz.equals(Integer.TYPE))
            return Integer.class;
        if (clazz.equals(Double.TYPE))
            return Double.class;
        if (clazz.equals(Float.TYPE))
            return Float.TYPE;
        if (clazz.equals(Long.TYPE))
            return Long.class;
        if (clazz.equals(Boolean.TYPE))
            return Boolean.class;
        if (clazz.equals(Short.TYPE))
            return Short.class;
        if (clazz.equals(Byte.TYPE))
            return Byte.class;

        return null;
    }

}
