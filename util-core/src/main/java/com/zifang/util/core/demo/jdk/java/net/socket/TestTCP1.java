package com.zifang.util.core.demo.jdk.java.net.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

//TCP编程例一：客户端给服务端发送信息。服务端输出此信息到控制台上
//网络编程实际上就是Socket的编程
public class TestTCP1 {

	public static void main(String[] args) throws Exception {
		server();
		client();
	}
	
	//客户端
	public static void client() throws Exception{
		//1、创建一个Socket的对象，通过构造器指明服务端的IP地址，以及直接接受程序的端口号
		Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 9090);
		//2.getOutputStream():发送数据，方法返回OutputStream对象
		OutputStream os = socket.getOutputStream();
		//3.具体的输出过程
		os.write("我是客户端，请多多照顾！".getBytes());
		
		//4.关闭相应的流和socket
		if(os != null){
			os.close();
		}
		if(socket != null){
			socket.close();
		}
	}
	
	//服务端
	public static void server() throws Exception{
		//1.创建一个ServerSocket对象，通过构造器指明自身的端口号
		ServerSocket ss = new ServerSocket(9090);
		//2.调用其accept()方法，返回一个Socket的对象
		Socket s = ss.accept();
		//3.调用Socket对象的getInputStream()互殴一个从客户端发送过来的输入流
		InputStream is = s.getInputStream();
		System.out.println(s.getInetAddress().getHostAddress());
		//4.对获取的输入流进行的操作
		byte[] b = new byte[20];
		int len;
		while((len = is.read(b)) != -1){
			String str = new String(b,0,len);
			System.out.println(str);
		}

		if(is !=null){
			is.close();
		}
		if(s != null){
			s.close();
		}
		if(ss != null){
			ss.close();
		}
	}
}
