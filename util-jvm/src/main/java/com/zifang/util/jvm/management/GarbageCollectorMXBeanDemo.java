package com.zifang.util.jvm.management;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * 用于 Java 虚拟机的获取垃圾回收器的管理接口。
 * 
 *
 *
 */
public class GarbageCollectorMXBeanDemo{


	/**
	 * 获取指定JVM的垃圾回收器信息
	 * 
	 * 远程程序启动初始JVM参数配置开启远程监控
	 *  -Djava.rmi.server.hostname=192.168.10.105 
	 *  -Dcom.sun.management.jmxremote 
	 *  -Dcom.sun.management.jmxremote.port=9999 
	 *  -Dcom.sun.management.jmxremote.ssl=false 
	 *  -Dcom.sun.management.jmxremote.authenticate=false
	 * @return
	 */
	public static void getRemoteGarbageCollectorMXBean() {
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
			ObjectName gcName = new ObjectName(ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE + ",*");
			Set<ObjectName> mbeans = mbs.queryNames(gcName, null);
			if (mbeans != null) {
				Iterator<ObjectName> iterator = mbeans.iterator();
				while (iterator.hasNext()) {
					ObjectName objName = (ObjectName) iterator.next();
					GarbageCollectorMXBean gc = ManagementFactory.newPlatformMXBeanProxy(mbs,
							objName.getCanonicalName(), GarbageCollectorMXBean.class);
					
					//System.out.println(jvmGC.getName());
					//返回已发生的回收的总次数。
					//System.out.println(jvmGC.getCollectionCount());
					//返回近似的累积回收时间（以毫秒为单位）。 
					//System.out.println(jvmGC.getCollectionTime());
					System.out.println("垃圾收集器： 名称='"+gc.getName()+"',收集="+gc.getCollectionCount()+",总花费时间="+gc.getCollectionTime());

				}
			}
			
		} catch (Exception e) {
		}

	}
	
	/**
	 * 获取当前虚拟机垃圾回收器的信息
	 * @return
	 */
	public static void getLocalGarbageCollectorMXBean(){
		List<GarbageCollectorMXBean> list = ManagementFactory.getGarbageCollectorMXBeans();
		if(list != null){
			for (GarbageCollectorMXBean gc : list) {
				//System.out.println(jvmGC.getName());
				//返回已发生的回收的总次数。
				//System.out.println(jvmGC.getCollectionCount());
				//返回近似的累积回收时间（以毫秒为单位）。 
				//System.out.println(jvmGC.getCollectionTime());
				System.out.println("垃圾收集器： 名称='"+gc.getName()+"',收集="+gc.getCollectionCount()+",总花费时间="+gc.getCollectionTime());
			}
		}
	}

	public static void main(String[] args) {
		getRemoteGarbageCollectorMXBean();
		getLocalGarbageCollectorMXBean();

	}

}