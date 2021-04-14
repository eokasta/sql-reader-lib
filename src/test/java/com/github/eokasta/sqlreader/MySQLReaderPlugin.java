package com.github.eokasta.sqlreader;

import com.github.eokasta.sqlreader.adapter.ReaderAdapterMap;
import com.github.eokasta.sqlreader.provider.MacacoAdapter;
import com.github.eokasta.sqlreader.reader.ResultSetReader;

public class MySQLReaderPlugin {

    private static final ReaderAdapterMap READER_ADAPTER_MAP = new ReaderAdapterMap();
    private static final ResultSetReader RESULT_SET_ADAPTER = new ResultSetReader(READER_ADAPTER_MAP);

    public static void main(String[] args) {
        READER_ADAPTER_MAP.registerAdapter(String.class, Macaco.class, new MacacoAdapter());

        final AClass aClass = RESULT_SET_ADAPTER.getFromSafe(AClass.class);
        System.out.println("\n\n\n\n\n\n\n\n\n\n");
        System.out.println(aClass);
        System.out.println("\n\n\n\n\n\n\n\n\n\n");
    }

}
