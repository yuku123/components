package com.zifang.util.server.net.bookdemo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.log;

public class DaytimeUDPServer {

	private final static int PORT = 13;
	private final static log audit = log.getlog("requests");
	private final static log errors = log.getlog("errors");

	public static void main(String[] args) {
		try (DatagramSocket socket = new DatagramSocket(PORT)) {
			while (true) {
				try {
					DatagramPacket request = new DatagramPacket(new byte[1024], 1024);
					socket.receive(request);

					String daytime = new Date().toString();
					byte[] data = daytime.getBytes(StandardCharsets.US_ASCII);
					DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(),
							request.getPort());
					socket.send(response);
					audit.info(daytime + " " + request.getAddress());
				} catch (IOException | RuntimeException ex) {
					errors.log(Level.SEVERE, ex.getMessage(), ex);
				}
			}
		} catch (IOException ex) {
			errors.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
}