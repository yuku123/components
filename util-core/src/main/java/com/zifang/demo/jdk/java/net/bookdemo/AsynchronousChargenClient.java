package com.zifang.demo.jdk.java.net.bookdemo;

import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.io.IOException;

public class AsynchronousChargenClient {

	private static class LineHandler implements CompletionHandler<Integer, ByteBuffer> {

		@Override
		public void completed(Integer result, ByteBuffer buffer) {
			buffer.flip();
			WritableByteChannel out = Channels.newChannel(System.out);
			try {
				out.write(buffer);
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}

		@Override
		public void failed(Throwable ex, ByteBuffer attachment) {
			System.err.println(ex.getMessage());
		}

	}

	public static int DEFAULT_PORT = 19;

	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("Usage: java ChargenClient host [port]");
			return;
		}

		int port;
		try {
			port = Integer.parseInt(args[1]);
		} catch (RuntimeException ex) {
			port = DEFAULT_PORT;
		}

		SocketAddress address = new InetSocketAddress(args[0], port);
		try {
			AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
			client.connect(address);

			while (true) {
				ByteBuffer buffer = ByteBuffer.allocate(74);
				CompletionHandler<Integer, ByteBuffer> handler = new LineHandler();
				try {
					client.read(buffer, buffer, handler);
				} catch (ReadPendingException ex) {
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}