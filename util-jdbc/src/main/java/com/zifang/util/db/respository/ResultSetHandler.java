package com.zifang.util.db.respository;

import com.zifang.util.core.lang.converter.Converters;
import lombok.Data;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class ResultSetHandler {

    private Type targetType;

    private ResultSet resultSet;

    private ResultSetMetaData resultSetMetaData;

    public Object solve() throws InstantiationException, IllegalAccessException, SQLException {

        resultSetMetaData = resultSet.getMetaData();

        // 是泛型类
        if (targetType instanceof ParameterizedType) {
            if (((ParameterizedType) targetType).getRawType() == List.class) {
                Type type = ((ParameterizedTypeImpl) targetType).getActualTypeArguments()[0];
                if (type instanceof ParameterizedType) {
                    Class clazz = ((ParameterizedTypeImpl) type).getRawType();
                    if (clazz == Map.class) {
                        List<Map<String, Object>> list = fetch(resultSet);
                        return list;
                    }
                } else {
                    Class clazz = (Class) ((ParameterizedTypeImpl) targetType).getActualTypeArguments()[0];
                    List<Map<String, Object>> list = fetch(resultSet);
                    List<Object> o = transformBatch(list, clazz);
                    return o;
                }
            } else if (((ParameterizedType) targetType).getRawType() == Map.class) {
                List<Map<String, Object>> list = fetch(resultSet);
                if (list.size() > 1) {
                    throw new RuntimeException("rows > 1");
                } else if (list.size() == 0) {
                    return null;
                } else {
                    return list.get(0);
                }
            }
        } else {
            List<Map<String, Object>> list = fetch(resultSet);
            if (list.size() > 1) {
                throw new RuntimeException("rows > 1");
            } else if (list.size() == 0) {
                return null;
            } else {
                Class<?> clazz = (Class<?>) targetType;
                Map<String, Object> map = list.get(0);
                return transform(map, clazz);
            }
        }
        return null;
    }

    private List<Object> transformBatch(List<Map<String, Object>> list, Class clazz) throws InstantiationException, IllegalAccessException {
        List<Object> objects = new ArrayList<>();
        for (Map<String, Object> map : list) {
            Object o = transform(map, clazz);
            objects.add(o);
        }
        return objects;
    }

    private Object transform(Map<String, Object> map, Class<?> clazz) throws InstantiationException, IllegalAccessException {
        Object o = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Field> columnMap = new LinkedHashMap<>();
        for (Field field : fields) {
            columnMap.put(handleColumn(field), field);
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (columnMap.get(entry.getKey()) != null) {
                Field field = columnMap.get(entry.getKey());
                field.setAccessible(true);
                Object target = Converters.caller(entry.getValue().getClass(), field.getType()).to(entry.getValue());
                field.set(o, target);
            }
        }
        return o;
    }

    private static String handleColumn(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (column == null) {
            StringBuilder convertName = new StringBuilder();
            for (int i = 0; i < field.getName().length(); i++) {
                //如果是大写前面先加一个_
                if (isUpperCase(field.getName().charAt(i))) {
                    convertName.append("_");
                }
                convertName.append(field.getName().charAt(i));
            }
            return convertName.toString().toLowerCase();
        } else {
            return column.name();
        }
    }

    //字母是否是大写
    private static boolean isUpperCase(char c) {
        return c >= 65 && c <= 90;
    }

    private List<Map<String, Object>> fetch(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> li = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (int i = 1; i < resultSetMetaData.getColumnCount(); i++) {
                map.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
            }
            li.add(map);
        }
        return li;
    }
}
