package com.github.eokasta.sqlreader.reader;

import com.github.eokasta.sqlreader.adapter.ReaderAdapter;
import com.github.eokasta.sqlreader.adapter.ReaderAdapterMap;
import com.github.eokasta.sqlreader.annotations.FieldKey;
import com.github.eokasta.sqlreader.model.ClassTypeModel;
import com.github.eokasta.sqlreader.model.FieldModel;
import javafx.util.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResultSetReader {

    private final ReaderAdapterMap readerAdapterMap;
    private final Map<Class<?>, ClassTypeModel> classTypeCache = new HashMap<>();

    public ResultSetReader(ReaderAdapterMap readerAdapterMap) {
        this.readerAdapterMap = readerAdapterMap;
    }

    public <T> T getFrom(ResultSet resultSet, Class<T> type)
          throws IllegalAccessException, InstantiationException, SQLException, NoSuchFieldException {
        final Map<Class<?>, Object> instances = new LinkedHashMap<>();
        ClassTypeModel classTypeModel = classTypeCache.get(type);

        if (classTypeModel == null) {
            classTypeModel = new ClassTypeModel(type);
            classTypeCache.put(type, classTypeModel);

            for (Field declaredField : type.getDeclaredFields()) {
                declaredField.setAccessible(true);
                final Class<?> fieldType = declaredField.getType();

                if (!declaredField.isAnnotationPresent(FieldKey.class))
                    continue;

                final FieldKey annotation = declaredField.getAnnotation(FieldKey.class);
                final String key = annotation.key();
                final String fieldName = key.isEmpty() ? declaredField.getName() : key;

                classTypeModel.getFields().put(fieldName, new FieldModel(declaredField, fieldType, fieldName));
            }
        }

        for (FieldModel fieldModel : classTypeModel.getFields().values()) {
            final Object object = resultSet.getObject(fieldModel.getName());
            if (object == null)
                throw new NullPointerException("query object is null.");

            final Class<?> fieldType = fieldModel.getType();
            final Pair<? extends Class<?>, ? extends Class<?>> classClassPair =
                  new Pair<>(object.getClass(), fieldType);
            final ReaderAdapter<?, ?> readerAdapter = readerAdapterMap.get(classClassPair);
            final Object resultObject = (readerAdapter != null ? readerAdapter.serialize(object) : object);

            if (!resultObject.getClass().equals(fieldType))
                throw new IllegalArgumentException("Field type is not the same type as the query object.");

            instances.put(fieldType, resultObject);
        }

        final T instance = type.newInstance();
        for (Field declaredField : instance.getClass().getDeclaredFields()) {
            declaredField.setAccessible(true);
            final Object object = instances.get(declaredField.getType());
            if (object == null) continue;

            final Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(declaredField, declaredField.getModifiers() & ~Modifier.FINAL);

            declaredField.set(instance, object);
        }

        return instance;
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
