package com.zifang.util.monitor.jvm.management;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;


/**
 * 用于 Java 虚拟机的编译系统的管理接口。
 */
public class CompilationMXBeanDemo {


	/**
	 * 获取指定JVM的类编译信息
	 * 
	 * 远程程序启动初始JVM参数配置开启远程监控
	 *  -Djava.rmi.server.hostname=192.168.10.105 
	 *  -Dcom.sun.management.jmxremote 
	 *  -Dcom.sun.management.jmxremote.port=9999 
	 *  -Dcom.sun.management.jmxremote.ssl=false 
	 *  -Dcom.sun.management.jmxremote.authenticate=false
	 * @return
	 */
	public static CompilationMXBean getRemoteCompilationMXBean() {
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
			CompilationMXBean compilationMXBean = ManagementFactory.newPlatformMXBeanProxy(mbs,
					ManagementFactory.COMPILATION_MXBEAN_NAME, CompilationMXBean.class);
			return compilationMXBean;
		} catch (Exception e) {
		}
		return null;

	}
	
	/**
	 * 获取当前虚拟机类编译信息
	 * @return
	 */
	public static CompilationMXBean getLocalCompilationMXBean(){
		return ManagementFactory.getCompilationMXBean();
	}

	public static void main(String[] args) {
		CompilationMXBean compilationMXBean = getRemoteCompilationMXBean();
//		CompilationMXBean compilationMXBean = getLocalCompilationMXBean();
		//返回即时 (JIT) 编译器的名称。
		System.out.println("JIT 编译器:"+compilationMXBean.getName());
		//返回在编译上花费的累积耗费时间的近似值（以毫秒为单位）。
		System.out.println("总编译时间:"+compilationMXBean.getTotalCompilationTime());
		//测试 Java 虚拟机是否支持监视编译时间。 
		System.out.println("否支持监视编译时间:"+compilationMXBean.isCompilationTimeMonitoringSupported());

	}
}