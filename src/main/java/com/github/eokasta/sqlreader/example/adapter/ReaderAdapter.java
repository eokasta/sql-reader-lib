package com.github.eokasta.sqlreader.example.adapter;

public interface ReaderAdapter<T, T2> {

    T2 parse(Object o);

    T unparse(Object o);

}
