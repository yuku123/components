package com.zifang.util.core.demo.temp.sun.tools;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Set;

import sun.jvmstat.monitor.HostIdentifier;
import sun.jvmstat.monitor.MonitorException;
import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;
import sun.tools.jps.Arguments;

public class InfcnJPS {

	private static Arguments arguments;

	public static void main(String[] paramArrayOfString) {
		paramArrayOfString = new String[] { "-mlvV" };
		try {
			arguments = new Arguments(paramArrayOfString);
		} catch (IllegalArgumentException localIllegalArgumentException) {
			System.err.println(localIllegalArgumentException.getMessage());
			Arguments.printUsage(System.err);
			return;
		}

		if (arguments.isHelp()) {
			Arguments.printUsage(System.out);
			System.exit(0);
		}
		try {
			HostIdentifier localHostIdentifier = arguments.hostId();
			MonitoredHost localObject1 = MonitoredHost.getMonitoredHost(localHostIdentifier);

			Set<Integer> localSet = ((MonitoredHost) localObject1).activeVms();
			Iterator<Integer> localIterator = localSet.iterator();

			for (localIterator = localSet.iterator(); localIterator.hasNext();) {
				StringBuilder localStringBuilder = new StringBuilder();
				Object localObject2 = null;

				int i = ((Integer) localIterator.next()).intValue();

				localStringBuilder.append(String.valueOf(i));

				if (arguments.isQuiet()) {
					System.out.println(localStringBuilder);
					continue;
				}

				MonitoredVm localMonitoredVm = null;
				String str1 = "//" + i + "?mode=r";

				String str2 = null;
				try {
					str2 = " -- process information unavailable";
					VmIdentifier localVmIdentifier = new VmIdentifier(str1);
					localMonitoredVm = ((MonitoredHost) localObject1).getMonitoredVm(localVmIdentifier, 0);

					str2 = " -- main class information unavailable";
					localStringBuilder.append(" "
							+ MonitoredVmUtil.mainClass(localMonitoredVm, arguments.showLongPaths()));
					String str3;
					if (arguments.showMainArgs()) {
						str2 = " -- main args information unavailable";
						str3 = MonitoredVmUtil.mainArgs(localMonitoredVm);
						if ((str3 != null) && (str3.length() > 0)) {
							localStringBuilder.append(" " + str3);
						}
					}
					if (arguments.showVmArgs()) {
						str2 = " -- jvm args information unavailable";
						str3 = MonitoredVmUtil.jvmArgs(localMonitoredVm);
						if ((str3 != null) && (str3.length() > 0)) {
							localStringBuilder.append(" " + str3);
						}
					}
					if (arguments.showVmFlags()) {
						str2 = " -- jvm flags information unavailable";
						str3 = MonitoredVmUtil.jvmFlags(localMonitoredVm);
						if ((str3 != null) && (str3.length() > 0)) {
							localStringBuilder.append(" " + str3);
						}
					}

					str2 = " -- detach failed";
					((MonitoredHost) localObject1).detach(localMonitoredVm);

					System.out.println(localStringBuilder);

					str2 = null;
				} catch (URISyntaxException localURISyntaxException) {
					localObject2 = localURISyntaxException;
					// if (!$assertionsDisabled) throw new AssertionError();
				} catch (Exception localException) {
					localObject2 = localException;
				} finally {
					if (str2 != null) {
						localStringBuilder.append(str2);
						if ((arguments.isDebug()) && (localObject2 != null)
								&& (((Exception) localObject2).getMessage() != null)) {
							localStringBuilder.append("\n\t");
							localStringBuilder.append(((Exception) localObject2).getMessage());
						}

						System.out.println(localStringBuilder);
						if (arguments.printStackTrace()) {
							((Exception) localObject2).printStackTrace();
							continue;
						}
					}
				}
			}
		} catch (MonitorException localMonitorException) {
			Object localObject1;
			if (localMonitorException.getMessage() != null) {
				System.err.println(localMonitorException.getMessage());
			} else {
				localObject1 = localMonitorException.getCause();
				if ((localObject1 != null) && (((Throwable) localObject1).getMessage() != null))
					System.err.println(((Throwable) localObject1).getMessage());
				else
					localMonitorException.printStackTrace();
			}
		}
	}
}
