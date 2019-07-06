package com.zifang.demo.thirdpart.jar.common.dbutils;

import com.zifang.demo.thirdpart.jar.json.GsonUtil;
import com.zifang.demo.thirdpart.components.database.mysql.JdbcDruidMysql;
import com.zifang.demo.thirdpart.components.database.mysql.MysqlDruidDBUtils;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {

    public static void test1(){
        QueryRunner qr = new QueryRunner(MysqlDruidDBUtils.getComboPooledDataSource());
        String sql = "insert into users values(null,?,?)";
        try {
            int update = qr.update(sql, "狗蛋","123456");
            System.out.println(update);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void test2(){

//        class MyHandler implements ResultSetHandler<List<User>>{
//
//            @Override
//            public List<User> handle(ResultSet rs) throws SQLException {
//                // 封装数据，数据从 Resultset 中获取
//                List<User> list = new ArrayList<User>();
//                while(rs.next()){
//                    User u = new User();
//                    u.setId(rs.getInt( "id"));
//                    u.setName(rs.getString( "name"));
//                    u.setPwd(rs.getString( "pwd"));
//
//                    list.add(u);
//                }
//                return list;
//            }
//
//        }

        QueryRunner qr = new QueryRunner(MysqlDruidDBUtils.getComboPooledDataSource());
        String sql = "select * from users";
//        try {
//            List<User> list = qr.query(sql, new MyHandler());
//            System.out.println(list);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//     ①ArrayHandler：     将查询结果的第一行数据，保存到Object数组中
//     ②ArrayListHandler     将查询的结果，每一行先封装到Object数组中，然后将数据存入List集合
//     ③BeanHandler     将查询结果的第一行数据，封装到user对象
//     ④BeanListHandler     将查询结果的每一行封装到user对象，然后再存入List集合
//     ⑤ColumnListHandler     将查询结果的指定列的数据封装到List集合中
//     ⑥MapHandler     将查询结果的第一行数据封装到map结合（key==列名，value==列值）
//     ⑦MapListHandler     将查询结果的每一行封装到map集合（key==列名，value==列值），再将map集合存入List集合
//     ⑧BeanMapHandler     将查询结果的每一行数据，封装到User对象，再存入mao集合中（key==列名，value==列值）
//     ⑨KeyedHandler     将查询的结果的每一行数据，封装到map1（key==列名，value==列值 ），然后将map1集合（有多个）存入map2集合（只有一个）
//     ⑩ScalarHandler     封装类似count、avg、max、min、sum......函数的执行结果
    }

    public static void test3() throws SQLException, ClassNotFoundException {
        BeanProcessor beanProcessor = new BeanProcessor();

        JdbcDruidMysql jdbcDruidMysql = new JdbcDruidMysql();
        ResultSet resultSet = jdbcDruidMysql.select("select * from users");
        resultSet.next();
        Users a = beanProcessor.toBean(resultSet,Users.class);
        System.out.println(GsonUtil.objectToJsonStr(a));
    }


    public static void test4() throws SQLException, ClassNotFoundException {
        QueryRunner qr = new QueryRunner(MysqlDruidDBUtils.getComboPooledDataSource());
        String sql = "select * from users";
        Users a = qr.query(sql,new BeanHandler<>(Users.class));
        System.out.println(GsonUtil.objectToJsonStr(a));
    }

    public static void test5(){
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(Users.class);

        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //test5();
        test4();
    }
}
