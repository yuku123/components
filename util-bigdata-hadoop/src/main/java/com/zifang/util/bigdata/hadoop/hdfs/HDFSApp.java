package com.zifang.util.bigdata.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;

/**
 * Created by japson on 5/26/2018.
 * Hadoop HDFS Java API 操作
 */
public class HDFSApp {

    // HDFS_PATH 很重要！是访问连接HDFS的关键
    public static final String HDFS_PATH = "hdfs://192.168.0.117:8020";

    // 必需的FileSystem类
    FileSystem fileSystem = null;
    // 配置对象
    Configuration configuration = null;

    /**
     * 初始化一些资源
     */
    @Before
    public void setUp() throws Exception{
        configuration = new Configuration();
        // 获取一个fileSystem对象，这就相当于建立连接了
        fileSystem = FileSystem.get(new URI(HDFS_PATH),configuration,"japson");
        System.out.println("HDFSApp.setUp");
    }

    /**
     * 创建HDFS目录
     * @throws Exception
     */
    @Test
    public void mkdir() throws Exception {
        // 创建一个新的目录，参数为路径
        fileSystem.mkdirs(new Path("/hdfsapi/test3"));
        System.out.println("mkdir已执行");
    }


    /**
     * 释放资源
     */
    @After
    public void tearDown() throws Exception {
        configuration = null;
        fileSystem = null;
        System.out.println("HDFSApp.tearDown");
    }

    @Test
    public void listFiles() throws Exception {
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/hdfsapi/test"));
        for (FileStatus fileStatus : fileStatuses) {
            String isDir = fileStatus.isDirectory() ? "文件夹" : "文件";
            short replication = fileStatus.getReplication();
            long len = fileStatus.getLen();
            String path = fileStatus.getPath().toString();

            System.out.println(isDir + "\t" + replication + "\t" + len + "\t" + path);
        }
    }

    @Test
    public void copyToLocalFile() throws Exception {
        Path localPath = new Path("E:\\IntelliJ IDEA\\hadoopTest1\\download.txt");
        Path hdfsPath = new Path("/hdfsapi/test/b.txt");
        fileSystem.copyToLocalFile(hdfsPath,localPath);
    }
    @Test
    public void copyFromLocalFileWithProgress() throws Exception {
        InputStream in = new BufferedInputStream(new FileInputStream(new File("D:\\迅雷下载\\逃避可耻却有用1.mp4")));
        FSDataOutputStream outputStream = fileSystem.create(new Path("/hdfsapi/test/gakki.mp4"),
                new Progressable() {
                    @Override
                    public void progress() {
                        System.out.print(".");   // 带进度提醒
                    }
                });
        IOUtils.copyBytes(in,outputStream,4096);
    }

    @Test
    public void copyFromLocalFile() throws Exception {
        Path localPath = new Path("E:\\IntelliJ IDEA\\hadoopTest1\\localText.txt");
        Path hdfsPath = new Path("/hdfsapi/test");

        fileSystem.copyFromLocalFile(localPath,hdfsPath);
    }

    @Test
    public void rename() throws Exception {
        Path oldPath = new Path("/hdfsapi/test3/a.txt");
        Path newPath = new Path("/hdfsapi/test3/b.txt");
        fileSystem.rename(oldPath,newPath);
    }


    @Test
    public void delete() throws Exception {

        fileSystem.delete(new Path("/hdfsapi/test/a.txt"),true);

    }

    @Test
    public void cat() throws Exception {
        FSDataInputStream inputStream = fileSystem.open(new Path("/hdfsapi/test3/a.txt"));
        IOUtils.copyBytes(inputStream,System.out,1024);
        inputStream.close();

    }

    @Test
    public void create() throws Exception {
        FSDataOutputStream outputStream = fileSystem.create(new Path("/hdfsapi/test3/a.txt"));
        outputStream.write("hello hadoop".getBytes());
        outputStream.flush();
        outputStream.close();
    }
}