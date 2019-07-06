package com.zifang.util.core.demo.jdk.java.io.bio;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PipedStreamDemo1 {
    public static void main(String[] args) {
    	//创建一个线程池
        ExecutorService executorService = Executors.newCachedThreadPool();

        try {
            //创建输入和输出管道流
            PipedOutputStream pos = new PipedOutputStream();
            PipedInputStream pis = new PipedInputStream(pos);

            //创建发送线程和接收线程
            Sender sender = new Sender(pos);
            Reciever reciever = new Reciever(pis);

            //提交给线程池运行发送线程和接收线程
            executorService.execute(sender);
            executorService.execute(reciever);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //通知线程池，不再接受新的任务，并执行完成当前正在运行的线程后关闭线程池。
        executorService.shutdown();
        try {
        	//shutdown 后可能正在运行的线程很长时间都运行不完成，这里设置超过1小时，强制执行 Interruptor 结束线程。
        	executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class Sender extends Thread {
        private PipedOutputStream pos;

        public Sender(PipedOutputStream pos) {
            super();
            this.pos = pos;
        }

        @Override
        public void run() {
            try {
                String s = "hello world, amazing java !";
                System.out.println("Sender:" + s);
                byte[] buf = s.getBytes();
                pos.write(buf, 0, buf.length);
                pos.close();
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class Reciever extends Thread {
        private PipedInputStream pis;

        public Reciever(PipedInputStream pis) {
            super();
            this.pis = pis;
        }

        @Override
        public void run() {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = pis.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                byte[] result = baos.toByteArray();
                String s = new String(result, 0, result.length);
                System.out.println("Reciever:" + s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}