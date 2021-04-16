package com.github.eokasta.sqlreader.example;

import java.util.UUID;

public class Student {

    private UUID uniqueId;
    private String name;
    private int age;

    public Student() {
    }

    public Student(UUID uniqueId, String name, int age) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.age = age;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
