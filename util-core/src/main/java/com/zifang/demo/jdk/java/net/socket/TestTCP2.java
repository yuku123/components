package com.zifang.demo.jdk.java.net.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


//客户端给服务端发送信息，服务端将信息打印到控制台上，同时发送“已收到信息”给客户端
public class TestTCP2 {

	public static void main(String[] args) throws Exception{
//		server();
		client();
	}
	
	public static void client() throws Exception{
		Socket s = new Socket(InetAddress.getByName("127.0.0.1"), 8989);
		OutputStream os = s.getOutputStream();
		byte[] data = "我是客户端".getBytes();
		int len = data.length;
		os.write(int2byte(len));
		os.write(data);
		os.flush();
		//shutdownOutput():执行此方法，显示的告诉服务端发送完毕
		InputStream is = s.getInputStream();
		
		
		int returnLen = readLen(is);
		byte[] b = new byte[returnLen];
		System.out.println("client receive size : " + returnLen);
		int readSize=0;
		int count=0;
		while(count<returnLen && (readSize = is.read(b)) != -1){
			String str = new String(b,0,readSize);
			System.out.println(str);
			count+=readSize;
		}
		
		//4.关闭相应的流和socket
		if(is !=null){
			is.close();
		}
		if(os != null){
			os.close();
		}
		if(s != null){
			s.close();
		}
	}
	
	public static void server()  throws Exception{
		ServerSocket ss = new ServerSocket(8989);
		Socket s = ss.accept();
		InputStream is = s.getInputStream();
		//4.对获取的输入流进行的操作
		
		int returnLen = readLen(is);
		byte[] b = new byte[returnLen];
		System.out.println("server receive size : " + returnLen);
		int readSize=0;
		int count=0;
		while(count<returnLen && (readSize = is.read(b)) != -1){
			String str = new String(b,0,readSize);
			System.out.println(str);
			count+=readSize;
		}

		OutputStream os = s.getOutputStream();
//		os.write("已收到信息".getBytes());
		byte[] data = "已收到信息".getBytes();
		int len = data.length;
		os.write(int2byte(len));
		os.write(data);
		os.flush();
		if(os != null){
			os.close();
		}
		if(is != null){
			is.close();
		}
		if(s != null){
			s.close();
		}
		if(ss != null){
			ss.close();
		}
	}
	
	public static int readLen(InputStream is) throws IOException{
		int b1 = is.read();
		int b2 = is.read();
		int b3 = is.read();
		int b4 = is.read();
		int len = (b1<<24)+(b2<<16)+(b3<<8)+b4;
		return len;
	}
	
	public static byte[] int2byte(int len){
		byte[] b = new byte[4];
		b[0] = (byte)(len>>24);
		b[1] = (byte)(len>>16 & 0XFF);
		b[2] = (byte)(len>>8 & 0XFF);
		b[3] = (byte)(len & 0XFF);
		return b;
	}
}
