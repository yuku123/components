package com.zifang.util.core.demo.jdk.java.lang.management;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;


/**
 * 用于 Java 虚拟机运行时参数及其它信息接口。
 */
public class RuntimeMXBeanDemo{


	/**
	 * 获取指定JVM的运行时参数及其它信息
	 * 
	 * 远程程序启动初始JVM参数配置开启远程监控
	 *  -Djava.rmi.server.hostname=192.168.10.105 
	 *  -Dcom.sun.management.jmxremote 
	 *  -Dcom.sun.management.jmxremote.port=9999 
	 *  -Dcom.sun.management.jmxremote.ssl=false 
	 *  -Dcom.sun.management.jmxremote.authenticate=false
	 * @return
	 */
	public static RuntimeMXBean getRemoteRuntimeMXBean() {
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
			RuntimeMXBean runtimeMXBean = ManagementFactory.newPlatformMXBeanProxy(mbs,
					ManagementFactory.RUNTIME_MXBEAN_NAME, RuntimeMXBean.class);
			return runtimeMXBean;
		} catch (Exception e) {
		}
		return null;

	}
	
	/**
	 * 获取当前虚拟机运行时参数及其它信息
	 * @return
	 */
	public static RuntimeMXBean getLocalRuntimeMXBean(){
		return ManagementFactory.getRuntimeMXBean();
	}

	public static void main(String[] args) {
//		RuntimeMXBean runtimeMXBean = getRemoteRuntimeMXBean();
		RuntimeMXBean runtimeMXBean = getLocalRuntimeMXBean();

		//返回由引导类加载器用于搜索类文件的引导类路径。 
		System.out.println("BootClassPath:"+runtimeMXBean.getBootClassPath());
		//返回系统类加载器用于搜索类文件的 Java 类路径。 
		System.out.println("ClassPath:"+runtimeMXBean.getClassPath());
		//返回传递给 Java 虚拟机的输入变量，其中不包括传递给 main 方法的变量。
		List<String> list = runtimeMXBean.getInputArguments();
		System.out.println("-------------JVM初始化参数---begin------------");
		for (String str : list) {
			System.out.println(str);
		}
		System.out.println("-------------JVM初始化参数---end------------");
		//返回 Java 库路径。 
		System.out.println("LibraryPath:"+runtimeMXBean.getLibraryPath());
		//返回正在运行的 Java 虚拟机实现的管理接口的规范版本。
		System.out.println("ManagementSpecVersion:"+runtimeMXBean.getManagementSpecVersion());
		//返回表示正在运行的 Java 虚拟机的名称。 
		System.out.println("Name:"+runtimeMXBean.getName());
		//返回 Java 虚拟机规范名称。 
		System.out.println("SpecName:"+runtimeMXBean.getSpecName());
		//返回 Java 虚拟机规范供应商。 
		System.out.println("SpecVendor:"+runtimeMXBean.getSpecVendor());
		//返回 Java 虚拟机规范版本。 
		System.out.println("SpecVersion:"+runtimeMXBean.getSpecVersion());
		//返回 Java 虚拟机的启动时间（以毫秒为单位）。 
		System.out.println("StartTime:"+runtimeMXBean.getStartTime());
		//返回所有系统属性的名称和值的映射。 
		System.out.println("SystemProperties:"+runtimeMXBean.getSystemProperties());
		//返回 Java 虚拟机的正常运行时间（以毫秒为单位）。 
		System.out.println("Uptime:"+runtimeMXBean.getUptime());
		//返回 Java 虚拟机实现名称。 
		System.out.println("VmName:"+runtimeMXBean.getVmName());
		//返回 Java 虚拟机实现供应商。 
		System.out.println("VmVendor:"+runtimeMXBean.getVmVendor());
		//返回 Java 虚拟机实现版本。 
		System.out.println("VmVersion:"+runtimeMXBean.getVmVersion());
		//测试 Java 虚拟机是否支持由引导类加载器用于搜索类文件的引导类路径机制。
		System.out.println("isBootClassPathSupported:"+runtimeMXBean.isBootClassPathSupported());

	}

}