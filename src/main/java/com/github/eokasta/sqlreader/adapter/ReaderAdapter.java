package com.github.eokasta.sqlreader.adapter;

public interface ReaderAdapter<T, T2> {

    T2 serialize(Object o);

    T deserialize(Object o);

}
