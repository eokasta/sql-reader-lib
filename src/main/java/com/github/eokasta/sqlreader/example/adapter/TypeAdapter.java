package com.github.eokasta.sqlreader.example.adapter;

public class TypeAdapter {

    public Class<?> getTypeByPrimitiveOrDefault(Class<?> clazz) {
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

        return clazz;
    }

    public Class<?> getTypeByInterfacesOrDefault(Class<?> clazz, Class<?> interfaceClass) {
        for (Class<?> anInterface : clazz.getInterfaces())
            if (anInterface.equals(interfaceClass))
                return anInterface;

        return clazz;
    }

}
