package com.github.eokasta.sqlreader;

import com.github.eokasta.sqlreader.annotations.ReadClass;

import java.util.ArrayList;

@ReadClass
public class ClassRoom {

    private int id;
    private ArrayList<Student> students;

    @Override
    public String toString() {
        return "ClassRoom{" +
              "id=" + id +
              ", students=" + students +
              '}';
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
