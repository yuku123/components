package com.zifang.util.server.net.bookdemo;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class InterfaceLister {

    public static void main(String[] args) throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            if (ni.isUp()) {
                System.out.println(ni);
                byte[] addres = ni.getHardwareAddress();
                if (addres != null) {
                    System.out.println(addres.length);
                }
            }
        }
    }
}
