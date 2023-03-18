package com.zifang.util.db.respository;

import com.zifang.util.db.context.DataSourceContext;
import com.zifang.util.db.context.PersistentContext;
import com.zifang.util.db.define.Param;
import com.zifang.util.db.define.Select;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaseRepositoryInvocationHandler implements InvocationHandler {

    private Class targetClass;

    public BaseRepositoryInvocationHandler(Class clazz) {
        this.targetClass = clazz;
        solve(targetClass);
    }

    private void solve(Class targetClass) {

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> clazz = method.getDeclaringClass();
        if (clazz == CrudRepository.class) {

        } else {
            String sql = method.getAnnotation(Select.class).value();
            String name = ((Param) method.getParameterAnnotations()[0][0]).value();

            BoundSql b = boundSql(method, args);

            DataSourceContext dataSourceContext = PersistentContext.fetchContext(PersistentContext.DEFAULT);

            Connection connection = dataSourceContext.getDatasourceFactory().newDatasource().getConnection();
            PreparedStatement prepareStatement = connection.prepareStatement(b.getTransformSql());
            for (Map.Entry<Integer, Object> entry : b.getIndexValueInsert().entrySet()) {
                prepareStatement.setObject(entry.getKey(), entry.getValue());
            }
            ResultSet resultSet = prepareStatement.executeQuery();

            ResultSetHandler resultSetHandler = new ResultSetHandler();
            resultSetHandler.setTargetType(method.getGenericReturnType());
            resultSetHandler.setResultSet(resultSet);

            return resultSetHandler.solve();
        }
        return null;
    }

    private BoundSql boundSql(Method method, Object[] args) {
        BoundSql boundSql = new BoundSql();

        String sql = method.getAnnotation(Select.class).value();
        String transformSQL = sql;

        Map<Integer, String> indexName = new LinkedHashMap<>();
        Map<Integer, Object> indexValue = new LinkedHashMap<>();

        // 获得注解信息
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            for (int j = 0; j < annotations[i].length; j++) {
                if (annotations[i][j].annotationType() == Param.class) {
                    Map<Integer, String> map = new LinkedHashMap<>();
                    indexName.put(i, ((Param) annotations[i][j]).value());
                }
            }
        }

        // 获得参数信息
        for (int i = 0; i < args.length; i++) {
            indexValue.put(i, args[i]);
        }

        Map<Integer, Object> indexValueInsert = new LinkedHashMap<>();
        int index = 1;
        for (Map.Entry<Integer, String> entry : indexName.entrySet()) {
            if (transformSQL.indexOf(":" + entry.getValue()) > 0) {
                indexValueInsert.put(index++, indexValue.get(entry.getKey()));
                transformSQL = transformSQL.replace(":" + entry.getValue(), "?");
            }
        }

        boundSql.setOriginSql(sql);
        boundSql.setTransformSql(transformSQL);
        boundSql.setIndexName(indexName);
        boundSql.setIndexValue(indexValue);
        boundSql.setIndexValueInsert(indexValueInsert);

        return boundSql;
    }
}
