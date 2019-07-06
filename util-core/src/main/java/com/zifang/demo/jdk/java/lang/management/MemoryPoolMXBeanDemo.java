package com.zifang.demo.jdk.java.lang.management;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;


/**
 * 用于 Java 虚拟机的内存池的管理接口。
 */
public class MemoryPoolMXBeanDemo{


	/**
	 * 获取指定JVM的内存池信息
	 * 
	 * 远程程序启动初始JVM参数配置开启远程监控
	 *  -Djava.rmi.server.hostname=192.168.10.105 
	 *  -Dcom.sun.management.jmxremote 
	 *  -Dcom.sun.management.jmxremote.port=9999 
	 *  -Dcom.sun.management.jmxremote.ssl=false 
	 *  -Dcom.sun.management.jmxremote.authenticate=false
	 * @return
	 */
	public static void getRemoteMemoryPoolMXBean() {
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
			ObjectName poolName = new ObjectName(ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE + ",*");

			Set<ObjectName> mbeans = mbs.queryNames(poolName, null);
			if (mbeans != null) {

				Iterator<ObjectName> iterator = mbeans.iterator();
				while (iterator.hasNext()) {
					ObjectName objName = (ObjectName) iterator.next();
					System.out.println("objName:" + objName.getCanonicalName());
					MemoryPoolMXBean p = ManagementFactory.newPlatformMXBeanProxy(mbs, objName.getCanonicalName(),
							MemoryPoolMXBean.class);
					System.out.println("name:" + p.getName());
					
					MemoryUsage usage = p.getCollectionUsage();
					if (usage == null) {
						continue;
					}
					
					if (objName.toString().indexOf("Eden Space") != -1) {
						System.out.println("-----------Eden Space-----------------");
					} else if (objName.toString().indexOf("Survivor Space") != -1) {
						System.out.println("----------------------------");
					} else if (objName.toString().indexOf("Code Cache") != -1) {
						System.out.println("-----------Survivor Space-----------------");
					} else if (objName.toString().indexOf("Perm Gen") != -1) {
						System.out.println("-----------Perm Gen-----------------");
					} else if (objName.toString().indexOf("Old Gen") != -1) {
						System.out.println("-----------Old Gen-----------------");
					} else if (objName.toString().indexOf("Metaspace") !=-1){
						System.out.println("-----------Metaspace-----------------");
					}
					
					printMemoryUsage(usage);
				}
			}
		} catch (Exception e) {
		}

	}
	
	private static void printMemoryUsage(MemoryUsage memoryUsage){
		if(memoryUsage!=null){
		// 返回已提交给 Java 虚拟机使用的内存量（以字节为单位）。 
		System.out.println("Committed:"+memoryUsage.getCommitted());
		//返回 Java 虚拟机最初从操作系统请求用于内存管理的内存量（以字节为单位）。 
		System.out.println("Init:"+memoryUsage.getInit());
		//  返回可以用于内存管理的最大内存量（以字节为单位）。 
		System.out.println("Max:"+memoryUsage.getMax());
		//  返回已使用的内存量（以字节为单位）。 
		System.out.println("Used:"+memoryUsage.getUsed());
		}
	}
	
	/**
	 * 获取当前虚拟机内存池信息
	 * @return
	 */
	public static void getLocalMemoryPoolMXBean(){
		List<MemoryPoolMXBean> list = ManagementFactory.getMemoryPoolMXBeans();
		if(list !=null ){
			for (MemoryPoolMXBean memoryPoolMXBean : list) {
				MemoryUsage usage = memoryPoolMXBean.getUsage();
				if (usage == null) {
					continue;
				}
				String objName = memoryPoolMXBean.getName();
				if (objName.indexOf("Eden Space") != -1) {
					System.out.println("-----------Eden Space-----------------");
				} else if (objName.indexOf("Survivor Space") != -1) {
					System.out.println("----------------------------");
				} else if (objName.indexOf("Code Cache") != -1) {
					System.out.println("-----------Survivor Space-----------------");
				} else if (objName.indexOf("Perm Gen") != -1) {
					System.out.println("-----------Perm Gen-----------------");
				} else if (objName.indexOf("Old Gen") != -1) {
					System.out.println("-----------Old Gen-----------------");
				} else if (objName.indexOf("Metaspace") !=-1){
					System.out.println("-----------Metaspace-----------------");
				}
				printMemoryUsage(usage);
			}
		}
	}

	public static void main(String[] args) {
//		getRemoteMemoryPoolMXBean();
		getLocalMemoryPoolMXBean();
	}

}