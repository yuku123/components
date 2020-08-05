package com.zifang.util.monitor.jvm.management;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * 用于 Java 虚拟机的线程管理接口。
 */
public class ThreadMXBeanDemo {

	/**
	 * 获取指定JVM的线程信息
	 * 
	 * 远程程序启动初始JVM参数配置开启远程监控 -Djava.rmi.server.hostname=192.168.10.105
	 * -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9999
	 * -Dcom.sun.management.jmxremote.ssl=false
	 * -Dcom.sun.management.jmxremote.authenticate=false
	 * 
	 * @return
	 */
	public static ThreadMXBean getRemoteThreadMXBean() {
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
			ThreadMXBean threadMXBean = ManagementFactory.newPlatformMXBeanProxy(mbs,
					ManagementFactory.THREAD_MXBEAN_NAME, ThreadMXBean.class);
			return threadMXBean;
		} catch (Exception e) {
		}
		return null;

	}

	/**
	 * 获取当前虚拟机线程信息
	 * 
	 * @return
	 */
	public static ThreadMXBean getLocalThreadMXBean() {
		return ManagementFactory.getThreadMXBean();
	}

	public static void main(String[] args) {
		ThreadMXBean threadMXBean = getRemoteThreadMXBean();
//		 ThreadMXBean threadMXBean = getLocalThreadMXBean();
		// 返回当前线程的总 CPU 时间（以毫微秒为单位）。
		System.out.println("CurrentThreadCpuTime:" + threadMXBean.getCurrentThreadCpuTime());
		// 返回当前线程在用户模式中执行的 CPU 时间（以毫微秒为单位）。
		System.out.println("CurrentThreadUserTime:" + threadMXBean.getCurrentThreadUserTime());
		// 返回活动守护线程的当前数目。
		System.out.println("DaemonThreadCount:" + threadMXBean.getDaemonThreadCount());
		// 返回自从 Java 虚拟机启动或峰值重置以来峰值活动线程计数。
		System.out.println("PeakThreadCount:" + threadMXBean.getPeakThreadCount());
		// 返回活动线程的当前数目，包括守护线程和非守护线程。
		System.out.println("ThreadCount:" + threadMXBean.getThreadCount());
		// 返回自从 Java 虚拟机启动以来创建和启动的线程总数目。
		System.out.println("TotalStartedThreadCount:" + threadMXBean.getTotalStartedThreadCount());
		// 查找因为等待获得对象监视器或可拥有同步器而处于死锁状态的线程循环。
		System.out.println("FindDeadlockedThreads:" + threadMXBean.findDeadlockedThreads());
		// 测试 Java 虚拟机是否支持当前线程的 CPU 时间测量。
		System.out.println("isCurrentThreadCpuTimeSupported:" + threadMXBean.isCurrentThreadCpuTimeSupported());
		// 测试 Java 虚拟机是否支持使用对象监视器的监视。
		System.out.println("isObjectMonitorUsageSupported:" + threadMXBean.isObjectMonitorUsageSupported());
		// 测试 Java 虚拟机是否支持使用 可拥有同步器的监视。
		System.out.println("isSynchronizerUsageSupported:" + threadMXBean.isSynchronizerUsageSupported());
		// 测试是否启用了线程争用监视。
		System.out.println("isThreadContentionMonitoringEnabled:" + threadMXBean.isThreadContentionMonitoringEnabled());
		// 测试 Java 虚拟机是否支持线程争用监视。
		System.out.println(
				"isThreadContentionMonitoringSupported:" + threadMXBean.isThreadContentionMonitoringSupported());
		// 测试是否启用了线程 CPU 时间测量。
		System.out.println("isThreadCpuTimeEnabled:" + threadMXBean.isThreadCpuTimeEnabled());
		// 测试 Java 虚拟机实现是否支持任何线程的 CPU 时间测量。
		System.out.println("isThreadCpuTimeSupported:" + threadMXBean.isThreadCpuTimeSupported());
		long[] ids = threadMXBean.getAllThreadIds();
		for (long id : ids) {
			ThreadInfo threadInfo = threadMXBean.getThreadInfo(id);
			System.out.println("-----------------------");
			// 返回与此 ThreadInfo 关联的线程的名称。
			System.out.println("线程名称：" + threadInfo.getThreadName());
			// 返回与此 ThreadInfo 关联的线程被阻塞进入或重进入监视器的总次数。
			System.out.println("BlockedCount:" + threadInfo.getBlockedCount());
			// 返回自从启用线程争用监视以来，与此 ThreadInfo 关联的线程被阻塞进入或重进入监视器的近似累计时间（以毫秒为单位）。
			System.out.println("BlockedTime:" + threadInfo.getBlockedTime());
			// 返回对象的字符串表示形式，与此 ThreadInfo 关联的线程被锁定并等待该对象。
			System.out.println("LockName:" + threadInfo.getLockName());
			// 返回拥有对象的线程的 ID，与此 ThreadInfo 关联的线程被阻塞并等待该对象。
			System.out.println("LockOwnerId:" + threadInfo.getLockOwnerId());
			// 返回拥有对象的线程的名称，与此 ThreadInfo 关联的线程被阻塞并等待该对象。
			System.out.println("LockOwnerName:" + threadInfo.getLockOwnerName());
			// 返回与此 ThreadInfo 关联的线程的 ID。
			System.out.println("ThreadId:" + threadInfo.getThreadId());
			// 返回与此 ThreadInfo 关联的线程等待通知的总次数。
			System.out.println("WaitedCount:" + threadInfo.getWaitedCount());
			// 返回自从启用线程争用监视以来,与此 ThreadInfo 关联的线程等待通知的近似累计时间（以毫秒为单位）。
			System.out.println("WaitedTime:" + threadInfo.getWaitedTime());
			// 返回与此 ThreadInfo 关联的线程的状态。
			System.out.println("ThreadState:" + threadInfo.getThreadState());
			// 返回指定 ID 的线程的总 CPU 时间（以毫微秒为单位）。
			System.out.println("ThreadCpuTime:" + threadMXBean.getThreadCpuTime(id));
			// 返回指定 ID 的线程在用户模式中执行的 CPU 时间（以毫微秒为单位）。
			System.out.println("ThreadUserTime:" + threadMXBean.getThreadUserTime(id));
		}

	}

}