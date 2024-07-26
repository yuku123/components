package com.zifang.util.zex.bust.charpter13;

import org.junit.Test;

import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class InetAddressTest {

    @Test
    public void test001() throws UnknownHostException {
        InetAddress inetAddress1 = InetAddress.getByName("www.baidu.com");
        InetAddress[] inetAddress2 = InetAddress.getAllByName("www.baidu.com");
        InetAddress inetAddress3 = InetAddress.getByName("180.101.49.12");
        InetAddress inetAddress4 = InetAddress.getByAddress(new byte[]{-76, 101, 49, 12});
    }

    @Test
    public void test002() throws UnknownHostException {
        InetAddress inetAddress1 = InetAddress.getLoopbackAddress();
        InetAddress inetAddress2 = InetAddress.getLocalHost();
        System.out.println(inetAddress1);
        System.out.println(inetAddress2);
    }

    @Test
    public void test3() throws UnknownHostException {
        showIntAddress(InetAddress.getByName("www.oreilly.com"));
        showIntAddress(InetAddress.getByName("208.201.239.100"));
        showIntAddress(InetAddress.getLocalHost());
        showIntAddress(InetAddress.getByName("www.taobao.com"));
        showIntAddress(InetAddress.getByName("183.136.135.225"));
        showIntAddress(InetAddress.getByAddress(new byte[]{(byte) 180, 101, 49, 12}));
        showIntAddress(InetAddress.getByAddress("www.baidu.com", new byte[]{(byte) 180, 101, 49, 12}));
    }

    public static void showIntAddress(InetAddress inetAddress) {
        System.out.println("inetAddress.getAddress()               " + Arrays.toString(inetAddress.getAddress()));
        System.out.println("inetAddress.getHostName()              " + inetAddress.getHostName());
        System.out.println("inetAddress.getHostAddress()           " + inetAddress.getHostAddress());
        System.out.println("inetAddress.getCanonicalHostName()     " + inetAddress.getCanonicalHostName());
        System.out.println("----");
        System.out.println("inetAddress.isAnyLocalAddress()    " + inetAddress.isAnyLocalAddress());//通配地址
        System.out.println("inetAddress.isLoopbackAddress()    " + inetAddress.isLoopbackAddress());//回送地址
        System.out.println("inetAddress.isLinkLocalAddress()   " + inetAddress.isLinkLocalAddress());//ip6的本地连接地址
        System.out.println("inetAddress.isSiteLocalAddress()   " + inetAddress.isSiteLocalAddress());//ip6的本地网站地址
        System.out.println("inetAddress.isMulticastAddress()   " + inetAddress.isMulticastAddress());//组播地址
        System.out.println("inetAddress.isMCGlobal()           " + inetAddress.isMCGlobal());//全球组播网址
        System.out.println("inetAddress.isMCOrgLocal()         " + inetAddress.isMCOrgLocal());//组织范围组播网址
        System.out.println("inetAddress.isMCSiteLocal()        " + inetAddress.isMCSiteLocal());//网站范围组播网址
        System.out.println("inetAddress.isMCLinkLocal()        " + inetAddress.isMCLinkLocal());//子网范围内组播网址
        System.out.println("inetAddress.isMCNodeLocal()        " + inetAddress.isMCNodeLocal());//本地接口组播地址
        System.out.println("----------------------------");
    }

    @Test
    public void test4() throws SocketException {
        Enumeration<NetworkInterface> networkInterface = NetworkInterface.getNetworkInterfaces();
        List<NetworkInterface> networkInterfaceList = new ArrayList<>();
        while (networkInterface.hasMoreElements()) {
            networkInterfaceList.add(networkInterface.nextElement());
        }
    }

    @Test
    public void test0() throws URISyntaxException {
        URI uri = URI.create("foo://username:password@example.com:8042/over/there/index.dtb?type=animal&name=narwhal#nose");

        System.out.println("scheme: " + uri.getScheme());
        System.out.println("userInfo: " + uri.getUserInfo());
        System.out.println("host: " + uri.getHost());
        System.out.println("port: " + uri.getPort());
        System.out.println("path: " + uri.getPath());
        System.out.println("query: " + uri.getQuery());
        System.out.println("fragment: " + uri.getFragment());
        System.out.println("authority: " + uri.getAuthority());

        System.out.println("RawSchemeSpecificPart: " + uri.getRawSchemeSpecificPart());
        System.out.println("RawUserInfo: " + uri.getRawUserInfo());
        System.out.println("RawAuthority: " + uri.getRawAuthority());
        System.out.println("RawPath: " + uri.getRawPath());
        System.out.println("RawQuery: " + uri.getRawQuery());
        System.out.println("RawFragment: " + uri.getRawFragment());

    }
}
