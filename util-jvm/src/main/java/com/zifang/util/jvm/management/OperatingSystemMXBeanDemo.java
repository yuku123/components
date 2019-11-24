package com.zifang.util.jvm.management;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;


/**
 * 用于 Java 虚拟机所在的操作系统系统的管理接口。
 * 
 *
 *
 */
public class OperatingSystemMXBeanDemo{


	/**
	 * 获取指定JVM所在的操作系统信息
	 * 
	 * 远程程序启动初始JVM参数配置开启远程监控
	 *  -Djava.rmi.server.hostname=192.168.10.105 
	 *  -Dcom.sun.management.jmxremote 
	 *  -Dcom.sun.management.jmxremote.port=9999 
	 *  -Dcom.sun.management.jmxremote.ssl=false 
	 *  -Dcom.sun.management.jmxremote.authenticate=false
	 * @return
	 */
	public static OperatingSystemMXBean getRemoteOperatingSystemMXBean() {
		String jmxURL = "service:jmx:rmi:///jndi/rmi://192.168.10.98:9999/jmxrmi";
		MBeanServerConnection mbs = null;
		try {
			JMXServiceURL address = new JMXServiceURL(jmxURL);
			JMXConnector connector = JMXConnectorFactory.connect(address);
			// 获取MBeanServerConnection
			mbs = connector.getMBeanServerConnection();
		} catch (IOException e) {
			System.out.println("jmx.url 连接异常！" + e.getMessage());
		}

		try {
			OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.newPlatformMXBeanProxy(mbs,
					ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
			return operatingSystemMXBean;
		} catch (Exception e) {
		}
		return null;

	}
	
	/**
	 * 获取当前虚拟机所在的操作系统信息
	 * @return
	 */
	public static OperatingSystemMXBean getLocalOperatingSystemMXBean(){
		return ManagementFactory.getOperatingSystemMXBean();
	}

	public static void main(String[] args) {
		OperatingSystemMXBean operatingSystemMXBean = getRemoteOperatingSystemMXBean();
//		OperatingSystemMXBean operatingSystemMXBean = getLocalOperatingSystemMXBean();
		// 返回操作系统的架构。
		System.out.println("Arch:"+operatingSystemMXBean.getArch());
		// 返回 Java 虚拟机可以使用的处理器数目。
		System.out.println("AvailableProcessors:"+operatingSystemMXBean.getAvailableProcessors());
		// 返回操作系统名称。
		System.out.println("Name:"+operatingSystemMXBean.getName());
		// 返回最后一分钟内系统加载平均值。
		System.out.println("SystemLoadAverage:"+operatingSystemMXBean.getSystemLoadAverage());
		// 返回操作系统的版本。
		System.out.println("Version:"+operatingSystemMXBean.getVersion());


	}

}