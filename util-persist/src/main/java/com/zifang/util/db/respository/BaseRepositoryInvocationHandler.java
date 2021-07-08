package com.zifang.util.db.respository;

import com.zifang.util.db.context.DataSourceContext;
import com.zifang.util.db.context.PersistentContext;
import com.zifang.util.db.define.Param;
import com.zifang.util.db.define.Select;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
        String prepare = method.getAnnotation(Select.class).value();
        String name = ((Param)method.getParameterAnnotations()[0][0]).value();

        String sql = "select * from resource_item where cms_id = '5e8888e8be7fff746fb26b5a'";
        DataSourceContext dataSourceContext  = PersistentContext.fetchContext(PersistentContext.DEFAULT);

        Connection connection = dataSourceContext.getDatasourceFactory().newDatasource().getConnection();
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        ResultSet resultSet = prepareStatement.executeQuery();
        while (resultSet.next()){
            System.out.print(resultSet.getObject(1)+":");
            System.out.println(resultSet.getObject(2));

        }

        return null;
    }
}
