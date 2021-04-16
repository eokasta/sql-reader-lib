package com.github.eokasta.sqlreader.example.reader;

import com.github.eokasta.sqlreader.example.adapter.TypeAdapter;
import com.github.eokasta.sqlreader.example.adapter.ReaderAdapter;
import com.github.eokasta.sqlreader.example.adapter.ReaderAdapterMap;
import com.github.eokasta.sqlreader.example.annotations.IgnoreField;
import com.github.eokasta.sqlreader.example.annotations.ReadClass;
import com.github.eokasta.sqlreader.example.annotations.ReadField;
import com.github.eokasta.sqlreader.example.model.ClassTypeModel;
import com.github.eokasta.sqlreader.example.model.FieldModel;
import javafx.util.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResultSetReader {

    private static final TypeAdapter TYPE_ADAPTER = new TypeAdapter();

    private final ReaderAdapterMap readerAdapterMap;
    private final Map<Class<?>, ClassTypeModel> classTypeCache = new HashMap<>();

    public ResultSetReader(ReaderAdapterMap readerAdapterMap) {
        this.readerAdapterMap = readerAdapterMap;
    }

    public <T> T parse(ResultSet resultSet, Class<T> type)
          throws IllegalAccessException, InstantiationException, SQLException, NoSuchFieldException {
        final Map<Class<?>, Object> instances = new LinkedHashMap<>();
        ClassTypeModel classTypeModel = classTypeCache.get(type);

        if (classTypeModel == null) {
            classTypeModel = new ClassTypeModel(type);
            classTypeCache.put(type, classTypeModel);

            if (type.isAnnotationPresent(ReadClass.class)) {
                for (Field declaredField : type.getDeclaredFields()) {
                    declaredField.setAccessible(true);
                    if (declaredField.isAnnotationPresent(IgnoreField.class)) continue;

                    final Class<?> fieldType = declaredField.getType();
                    final String fieldName = declaredField.getName();
                    classTypeModel.getFields().put(fieldName, new FieldModel(declaredField, fieldType, fieldName));
                }
            } else {
                for (Field declaredField : type.getDeclaredFields()) {
                    declaredField.setAccessible(true);
                    final Class<?> fieldType = declaredField.getType();

                    if (!declaredField.isAnnotationPresent(ReadField.class))
                        continue;

                    final ReadField annotation = declaredField.getAnnotation(ReadField.class);
                    final String key = annotation.key();
                    final String fieldName = key.isEmpty() ? declaredField.getName() : key;

                    classTypeModel.getFields().put(fieldName, new FieldModel(declaredField, fieldType, fieldName));
                }
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
            final Class<?> parsedFieldType =
                  fieldType.isPrimitive() ? TYPE_ADAPTER.getTypeByPrimitiveOrDefault(fieldType) : fieldType;
            final Object parsedObject =
                  readerAdapter != null ? readerAdapter.parse(object) : object;
            final Class<?> parsedObjectType =
                  TYPE_ADAPTER.getTypeByInterfacesOrDefault(parsedObject.getClass(), parsedFieldType);
            if (!parsedObjectType.equals(parsedFieldType))
                throw new IllegalArgumentException("Field type is not the same type as the query object.");

            instances.put(fieldType, parsedObject);
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

    public <T> T parseSafe(ResultSet resultSet, Class<T> type) {
        try {
            return parse(resultSet, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
