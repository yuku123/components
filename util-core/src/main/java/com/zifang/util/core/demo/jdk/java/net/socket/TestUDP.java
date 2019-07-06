package com.zifang.util.core.demo.jdk.java.net.socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

//UDP编程
public class TestUDP {

	public static void main(String[] args) throws Exception {
//		receive();
		send();
	}

	public static void send() throws Exception {
		DatagramSocket ds = new DatagramSocket();

		byte[] b = "你好，我是要发送的数据".getBytes();
		// 创建一个数据报：每一个数据报不能大于64K，都记录着数据信息，发送端的IP、端口、以及要发送到的接收端的IP、端口号
		DatagramPacket pack = new DatagramPacket(b, 0, b.length,
				InetAddress.getByName("127.0.0.1"), 9090);
		ds.send(pack);
		
		ds.close();
	}
	
	public static void receive() throws Exception {
		DatagramSocket ds = new DatagramSocket(9090);
		
		byte[] b = new byte[1024];
		DatagramPacket pack = new DatagramPacket(b, 0, b.length);
		ds.receive(pack);
		
		String str = new String(pack.getData(), 0, pack.getLength());
		System.out.println(str);
		
		ds.close();
	}
}
