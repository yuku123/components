package com.zifang.util.zex.spring.context;

import com.zifang.util.zex.spring.Car;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class AnnotationApplicationContext {

	 public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(Beans.class);
		Car car =ctx.getBean("car",Car.class);
	}
}
