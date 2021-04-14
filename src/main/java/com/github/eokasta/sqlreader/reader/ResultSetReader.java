package com.github.eokasta.sqlreader.reader;

import com.github.eokasta.sqlreader.adapter.ReaderAdapter;
import com.github.eokasta.sqlreader.adapter.ReaderAdapterMap;
import com.github.eokasta.sqlreader.annotations.FieldKey;
import javafx.util.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResultSetReader {

    private final ReaderAdapterMap readerAdapterMap;

    public ResultSetReader(ReaderAdapterMap readerAdapterMap) {
        this.readerAdapterMap = readerAdapterMap;
    }

    public <T> T getFrom(ResultSet resultSet, Class<T> type) throws IllegalAccessException, InstantiationException, SQLException, NoSuchMethodException, InvocationTargetException {
        final Map<Class<?>, Object> instances = new LinkedHashMap<>();
        for (Field declaredField : type.getDeclaredFields()) {
            declaredField.setAccessible(true);
            final Class<?> fieldType = declaredField.getType();

            if (!declaredField.isAnnotationPresent(FieldKey.class))
                continue;

            final FieldKey annotation = declaredField.getAnnotation(FieldKey.class);
            final String key = annotation.key();
            final String fieldName = key.isEmpty() ? declaredField.getName() : key;

            final Object object = resultSet.getObject(fieldName);
            if (object == null)
                throw new NullPointerException("query object is null.");

            if (!object.getClass().equals(fieldType))
                throw new IllegalArgumentException("Field type is not the same type as the query object.");

            final Pair<? extends Class<?>, ? extends Class<?>> classClassPair = new Pair<>(object.getClass(), fieldType);
            final ReaderAdapter<?, ?> readerAdapter = readerAdapterMap.get(classClassPair);

            final Object resultObject = (readerAdapter != null ? readerAdapter.serialize(object) : object);

            instances.put(fieldType, resultObject);
        }

        return type.getConstructor(instances.keySet().
              toArray(new Class[0])).
              newInstance(instances.values().toArray(new Object[0]));
    }

    public <T> T getFromSafe(ResultSet resultSet, Class<T> type) {
        try {
            return getFrom(resultSet, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
