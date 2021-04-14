package com.github.eokasta.sqlreader.adapter;

import javafx.util.Pair;

import java.util.HashMap;

public class ReaderAdapterMap extends HashMap<Pair<Class<?>, Class<?>>, ReaderAdapter<?, ?>> {

    public ReaderAdapterMap registerAdapter(Pair<Class<?>, Class<?>> pair, ReaderAdapter<?, ?> adapter) {
        put(pair, adapter);
        return this;
    }

    public ReaderAdapterMap registerAdapter(Class<?> parseFrom, Class<?> parseTo, ReaderAdapter<?, ?> adapter) {
        registerAdapter(new Pair<>(parseFrom, parseTo), adapter);
        return this;
    }

    public ReaderAdapterMap unregisterAdapter(Pair<Class<?>, Class<?>> pair) {
        remove(pair);
        return this;
    }

    public <T> T parse(Object parseFrom, T parseTo) {
        final ReaderAdapter<?, ?> readerAdapter = get(new Pair<>(parseFrom, parseTo));
        if (readerAdapter == null) return null;

        return (T) readerAdapter.deserialize(parseFrom);
    }

}