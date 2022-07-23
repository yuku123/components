package com.zifang.util.http.net.bookdemo;

import javax.xml.bind.DatatypeConverter;

public class CallbackDigestUserInterface {

    public static void receiveDigest(byte[] digest, String name) {
        StringBuilder result = new StringBuilder(name);
        result.append(": ");
        result.append(DatatypeConverter.printHexBinary(digest));
        System.out.println(result);
    }

    public static void main(String[] args) {
        String filename = "D:\\test.txt";
        // Calculate the digest
        // CallbackDigest cb = new CallbackDigest(filename);
        // Thread t = new Thread(cb);
        // t.start();
    }
}