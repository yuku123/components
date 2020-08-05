package com.zifang.util.monitor.jvm.management;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;


/**
 * 用于 Java 虚拟机的类加载系统的管理接口。
 */
public class ClassLoadingMXBeanDemo{


	/**
	 * 获取指定JVM的类加载信息
	 * 
	 * 远程程序启动初始JVM参数配置开启远程监控
	 *  -Djava.rmi.server.hostname=192.168.10.105 
	 *  -Dcom.sun.management.jmxremote 
	 *  -Dcom.sun.management.jmxremote.port=9999 
	 *  -Dcom.sun.management.jmxremote.ssl=false 
	 *  -Dcom.sun.management.jmxremote.authenticate=false
	 * @return
	 */
	public static ClassLoadingMXBean getRemoteClassLoading() {
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
			ClassLoadingMXBean classLoadingMXBean = ManagementFactory.newPlatformMXBeanProxy(mbs,
					ManagementFactory.CLASS_LOADING_MXBEAN_NAME, ClassLoadingMXBean.class);
			return classLoadingMXBean;
		} catch (Exception e) {
		}
		return null;

	}
	
	/**
	 * 获取当前虚拟机类加载信息
	 * @return
	 */
	public static ClassLoadingMXBean getLocalClassLoading(){
		return ManagementFactory.getClassLoadingMXBean();
	}

	public static void main(String[] args) {
		ClassLoadingMXBean classLoadingMXBean = getRemoteClassLoading();
//		ClassLoadingMXBean classLoadingMXBean = getLocalClassLoading();
		//返回当前加载到 Java 虚拟机中的类的数量。
		System.out.println("已加装当前类："+classLoadingMXBean.getLoadedClassCount());
		//返回自 Java 虚拟机开始执行到目前已经加载的类的总数。
		System.out.println("已加载类总数："+classLoadingMXBean.getTotalLoadedClassCount());
		//返回自 Java 虚拟机开始执行到目前已经卸载的类的总数。
		System.out.println("已卸御类总数："+classLoadingMXBean.getUnloadedClassCount());
		//测试是否已为类加载系统启用了 verbose 输出。
		System.out.println("JVM是否打印GC："+classLoadingMXBean.isVerbose());

	}

}