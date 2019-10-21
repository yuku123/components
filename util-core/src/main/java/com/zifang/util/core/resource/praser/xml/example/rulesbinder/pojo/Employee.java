
package com.zifang.util.core.resource.praser.xml.example.rulesbinder.pojo;

import com.zifang.util.praser.xml.example.rulesbinder.pojo.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author    http://www.cnblogs.com/chenpi/
 * @version   2017年6月5日
 */
public class Employee {

	private String firstName;
	private String lastName;
	private List<com.zifang.util.praser.xml.example.rulesbinder.pojo.Address> addressList = new ArrayList<com.zifang.util.praser.xml.example.rulesbinder.pojo.Address>();
	
	public void addAddress(com.zifang.util.praser.xml.example.rulesbinder.pojo.Address address){
		addressList.add(address);
	}
	
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the addressList
	 */
	public List<com.zifang.util.praser.xml.example.rulesbinder.pojo.Address> getAddressList() {
		return addressList;
	}
	/**
	 * @param addressList the addressList to set
	 */
	public void setAddressList(List<Address> addressList) {
		this.addressList = addressList;
	}

}
