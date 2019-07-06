package com.zifang.util.core.demo.jdk.java.net.bookdemo;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

public class UDPEchoServerWithChannels {

	public final static int PORT = 7;
	public final static int MAX_PACKET_SIZE = 65507;

	public static void main(String[] args) {

		try {
			DatagramChannel channel = DatagramChannel.open();
			DatagramSocket socket = channel.socket();
			SocketAddress address = new InetSocketAddress(PORT);
			socket.bind(address);
			ByteBuffer buffer = ByteBuffer.allocateDirect(MAX_PACKET_SIZE);
			while (true) {
				SocketAddress client = channel.receive(buffer);
				buffer.flip();
				channel.send(buffer, client);
				buffer.clear();
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}