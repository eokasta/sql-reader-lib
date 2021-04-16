package com.github.eokasta.sqlreader.example.adapter;

public interface ReaderAdapter<T> {

    T parse(Object o);

}
