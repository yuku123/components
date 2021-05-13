package com.zifang.util.server.net.bookdemo;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class DatagramExample {

    public static void main(String[] args) {

        String s = "This is a test.";

        try {
            byte[] data = s.getBytes(StandardCharsets.UTF_8);
            InetAddress ia = InetAddress.getByName("www.ibiblio.org");
            int port = 7;
            DatagramPacket dp = new DatagramPacket(data, data.length, ia, port);
            System.out.println("This packet is addressed to " + dp.getAddress() + " on port " + dp.getPort());
            System.out.println("There are " + dp.getLength() + " bytes of data in the packet");
            System.out.println(new String(dp.getData(), dp.getOffset(), dp.getLength(), StandardCharsets.UTF_8));
        } catch (UnknownHostException ex) {
            System.err.println(ex);
        }
    }
}