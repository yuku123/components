package com.zifang.util.zex.bust.ex;

import lombok.Data;
import org.junit.Test;

import java.io.*;

public class SerializableTest {

    @Test
    public void test0() throws IOException, ClassNotFoundException {
        //初始化一个User实例，并填充一部分数据
        User user = new User();
        user.setName("aa");
        user.setAge(23);
        System.out.println(user);

        //使用ObjectOutputStream开始写入一个文件
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tempFile"));
        oos.writeObject(user);
        oos.close();

        //从文件内反序列化
        ObjectInputStream ois  = new ObjectInputStream(new FileInputStream(new File("tempFile")));
        User newUser = (User) ois.readObject();
        System.out.println(newUser);
    }
}

@Data
class User implements Serializable {
    private String name;
    private int age;
}
