package com.github.eokasta.sqlreader.example.model;

import java.util.LinkedHashMap;
      import java.util.Map;

public class ClassTypeModel {

    private final Class<?> classType;
    private final Map<String, FieldModel> fields;

    public ClassTypeModel(Class<?> classType) {
        this.classType = classType;
        this.fields = new LinkedHashMap<>();
    }

    public Class<?> getClassType() {
        return classType;
    }

    public Map<String, FieldModel> getFields() {
        return fields;
    }
}
