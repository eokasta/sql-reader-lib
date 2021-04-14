package com.github.eokasta.sqlreader.provider;

import com.github.eokasta.sqlreader.Macaco;
import com.github.eokasta.sqlreader.adapter.ReaderAdapter;

public class MacacoAdapter implements ReaderAdapter<String, Macaco> {

    @Override
    public Macaco serialize(Object o) {
        final String string = (String) o;
        final String[] split = string.split(":");

        return new Macaco(split[0], Integer.parseInt(split[1]));
    }

    @Override
    public String deserialize(Object o) {
        final Macaco macaco = (Macaco) o;
        return macaco.getName() + ":" + macaco.getAge();
    }
}
