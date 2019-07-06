package com.zifang.demo.jdk.java.net.bookdemo;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

public class UDPDiscardServerWithChannels {

	public final static int PORT = 9;
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
				System.out.print(client + " says ");
				while (buffer.hasRemaining())
					System.out.write(buffer.get());
				System.out.println();
				buffer.clear();
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}