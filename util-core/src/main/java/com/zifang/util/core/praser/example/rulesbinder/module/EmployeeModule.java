package com.zifang.util.core.praser.example.rulesbinder.module;


import com.zifang.util.core.praser.example.rulesbinder.pojo.Address;
import com.zifang.util.core.praser.example.rulesbinder.pojo.Employee;
import org.apache.commons.digester3.binder.AbstractRulesModule;

/**
 * 
 * 
 * @author    http://www.cnblogs.com/chenpi/
 * @version   2017年6月5日
 */
public class EmployeeModule extends AbstractRulesModule {

	@Override
	protected void configure() {
		forPattern("employee").createObject().ofType(Employee.class);
		forPattern("employee/firstName").setBeanProperty();
		forPattern("employee/lastName").setBeanProperty();

		forPattern("employee/address").createObject().ofType(Address.class).then().setNext("addAddress");
		forPattern("employee/address/type").setBeanProperty();
		forPattern("employee/address/city").setBeanProperty();
		forPattern("employee/address/state").setBeanProperty();
	}

}
