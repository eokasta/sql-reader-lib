package com.github.eokasta.sqlreader.example;

import com.github.eokasta.sqlreader.example.annotations.ReadClass;

import java.util.List;

@ReadClass
public class ClassRoom {

    private int id;
    private List<Student> students;

    @Override
    public String toString() {
        return "ClassRoom{" +
              "id=" + id +
              ", students=" + students +
              '}';
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
