package com.github.eokasta.sqlreader;

import com.github.eokasta.sqlreader.annotations.FieldKey;

public class AClass {

    @FieldKey()
    private String name;

    @FieldKey()
    private Integer age;

    @FieldKey()
    private Macaco macaco;

    public AClass(String name, Integer age, Macaco macaco) {
        this.name = name;
        this.age = age;
        this.macaco = macaco;
    }

    @Override
    public String toString() {
        return "AClass{" +
              "name='" + name + '\'' +
              ", age=" + age +
              ", macaco=" + macaco.toString() +
              '}';
    }
}
