
package com.zifang.util.core.resource.praser.xml.example.rulesbinder;

import com.zifang.util.praser.xml.example.rulesbinder.module.EmployeeModule;
import com.zifang.util.praser.xml.example.rulesbinder.pojo.Address;
import com.zifang.util.praser.xml.example.rulesbinder.pojo.Employee;
import com.zifang.util.praser.xml.example.simpletest.ExampleMain;
import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * 
 * 
 * @author    http://www.cnblogs.com/chenpi/
 * @version   2017年6月5日
 */
public class DigesterLoaderMain {

	private static DigesterLoader dl = DigesterLoader.newLoader(new EmployeeModule())
			.setNamespaceAware(false);
	public static void main(String[] args) {
		try {
		    
			Digester digester = dl.newDigester();
			Employee employee = digester.parse(ExampleMain.class.getClassLoader().getResourceAsStream("employee.xml"));

			System.out.print(employee.getFirstName() + " ");
			System.out.print(employee.getLastName() + ", ");
			for (Address a : employee.getAddressList()) {
				System.out.print(a.getType() + ", ");
				System.out.print(a.getCity() + ", ");
				System.out.println(a.getState());
			}

		} catch (IOException e) {

			e.printStackTrace();
		} catch (SAXException e) {

			e.printStackTrace();
		}
	}
}
