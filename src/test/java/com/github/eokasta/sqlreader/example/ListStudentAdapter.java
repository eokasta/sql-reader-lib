package com.github.eokasta.sqlreader.example;

import com.github.eokasta.sqlreader.example.adapter.ReaderAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListStudentAdapter implements ReaderAdapter<ArrayList<Student>> {

    private final Gson gson = new Gson();

    @Override
    public ArrayList<Student> parse(Object o) {
        final String json = (String) o;
        final Type type = new TypeToken<List<Student>>() {}.getType();

        return gson.fromJson(json, type);
    }

}
