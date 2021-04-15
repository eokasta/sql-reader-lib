package com.github.eokasta.sqlreader.model;

import java.lang.reflect.Field;

public class FieldModel {

    private final Field field;
    private final Class<?> type;
    private final String name;

    public FieldModel(Field field, Class<?> type, String name) {
        this.field = field;
        this.type = type;
        this.name = name;
    }

    public Field getField() {
        return field;
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
