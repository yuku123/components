
package com.zifang.util.core.resource.praser.xml.example.rulesbinder;


import com.zifang.util.praser.xml.example.rulesbinder.module.EmployeeModule;
import com.zifang.util.praser.xml.example.rulesbinder.pojo.Address;
import com.zifang.util.praser.xml.example.rulesbinder.pojo.Employee;
import com.zifang.util.praser.xml.example.simpletest.ExampleMain;
import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 * @author    http://www.cnblogs.com/chenpi/
 * @version   2017年6月5日
 */
public class AsyncParseMain {
	private static DigesterLoader dl = DigesterLoader.newLoader(new EmployeeModule())
			.setNamespaceAware(false).setExecutorService(Executors.newSingleThreadExecutor());
	public static void main(String[] args) {
		try {
		    
			Digester digester = dl.newDigester();
			Future<Employee> future = digester.asyncParse(ExampleMain.class.getClassLoader().getResourceAsStream("employee.xml"));

			Employee employee = future.get();
			
			System.out.print(employee.getFirstName() + " ");
			System.out.print(employee.getLastName() + ", ");
			for (Address a : employee.getAddressList()) {
				System.out.print(a.getType() + ", ");
				System.out.print(a.getCity() + ", ");
				System.out.println(a.getState());
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
