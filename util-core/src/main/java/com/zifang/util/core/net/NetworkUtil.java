package com.zifang.util.core.net;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static com.zifang.util.core.Const.Symbol.MINUS;


/**
 * 网络操作类
 */
public class NetworkUtil {

    /**
     * 默认的localhost
     */
    public static final String DEFAULT_LOCALHOST = "127.0.0.1";

    /**
     * 获取mac地址
     * @params: [macConnector] mac地址连接符
     * @return: java.lang.String 响应参数
     */
    public static String getMac(String macConnector) throws SocketException, UnknownHostException {
        StringBuilder macAddress = new StringBuilder();
        List<String> ipList = getLocalHostAddress();
        for (String str : ipList) {
            // 获取本地IP对象
            InetAddress address = InetAddress.getByName(str);
            // 获得网络接口对象(即网卡), 并得到mac地址, mac地址存在于一个byte数组中。
            byte[] mac = NetworkInterface.getByInetAddress(address).getHardwareAddress();
            if (null != mac) {
                // 下面代码是把mac地址拼装成String
                StringBuilder segment = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    if (i != 0) {
                        segment.append(macConnector);
                    }
                    // mac[i] & 0xFF 是为了把byte转化为正整数
                    String hexMac = Integer.toHexString(mac[i] & 0xFF);
                    segment.append(hexMac.length() == 1 ? 0 + hexMac : hexMac);
                }
                // 把字符串所有小写字母改为大写成为正规的mac地址并返回
                macAddress.append(segment.toString().toUpperCase());
            }
        }
        return macAddress.toString();
    }

    /**
     * @author: zifang
     * @description: 获取mac地址
     * @time: 2020/5/14 16:00
     * @return: java.lang.String 响应参数
     */
    public static String getMac() throws SocketException, UnknownHostException {
        return getMac(MINUS);
    }

    /**
     * @author: zifang
     * @description: 获取本机ip
     * @time: 2020/5/14 16:02
     * @params: [] 请求参数
     * @return: java.util.List<java.lang.String> 响应参数
     */
    public static List<String> getLocalHostAddress() throws SocketException {
        List<String> ipList = new ArrayList<>();
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (address instanceof Inet4Address) {
                    if (!DEFAULT_LOCALHOST.equals(address.getHostAddress())) {
                        ipList.add(address.getHostAddress());
                    }
                }
            }
        }
        return ipList;
    }


    public static String getFirstLocalHostAddress() {
        List<String> ipList;
        try {
            ipList = getLocalHostAddress();
            // 取最后一个
            return ipList.get(ipList.size() - 1);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return DEFAULT_LOCALHOST;
    }

    /**
     * @author: zifang
     * @description: ip str转int
     * @time: 2021/7/29 16:01:00
     * @params: [ipStr] request
     * @return: int response
     */
    public static int ipToInt(String ipStr) {
        String[] ip = ipStr.split("\\.");
        return (Integer.parseInt(ip[0]) << 24) + (Integer.parseInt(ip[1]) << 16) + (
                Integer.parseInt(ip[2]) << 8) + Integer.parseInt(ip[3]);
    }

    /**
     * @author: zifang
     * @description: ip int转str
     * @time: 2021/7/29 16:02:00
     * @params: [intIp] request
     * @return: java.lang.String response
     */
    public static String intToIp(int intIp) {
        return (intIp >> 24) + "."
                + ((intIp & 0x00FFFFFF) >> 16) + "."
                + ((intIp & 0x0000FFFF) >> 8) + "."
                + (intIp & 0x000000FF);
    }

    /**
     * 获取本地第一个有效IPv4地址（非回环、非禁用、非IPv6）
     * @return 本地IP字符串，获取失败返回null
     */
    public static String getLocalIp() {
        try {
            // 遍历所有网络接口
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = networkInterfaces.nextElement();
                // 过滤：未启用的接口、回环接口、虚拟接口（如Docker桥接）
                if (!ni.isUp() || ni.isLoopback() || ni.isVirtual()) {
                    continue;
                }

                // 遍历接口绑定的所有地址
                Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress addr = inetAddresses.nextElement();
                    // 过滤：IPv6地址、回环地址（127.0.0.1）
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        // 若未找到有效IP，返回回环地址作为 fallback
        return InetAddress.getLoopbackAddress().getHostAddress();
    }

    /**
     * 获取所有本地有效IPv4地址
     * @return 有效IP数组
     */
    public static String[] getAllLocalIps() throws UnknownHostException {
        return java.util.stream.Stream.of(InetAddress.getAllByName(getLocalHostName()))
                .filter(addr -> addr instanceof Inet4Address && !addr.isLoopbackAddress())
                .map(InetAddress::getHostAddress)
                .toArray(String[]::new);
    }

    /**
     * 获取本地主机名
     */
    private static String getLocalHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }

    // 测试
    public static void main(String[] args) throws UnknownHostException {
        System.out.println("自定义NetUtil获取本地IP：" + NetworkUtil.getLocalIp());
        System.out.println("所有本地有效IPv4：");
        for (String ip : NetworkUtil.getAllLocalIps()) {
            System.out.println("- " + ip);
        }
    }

}
