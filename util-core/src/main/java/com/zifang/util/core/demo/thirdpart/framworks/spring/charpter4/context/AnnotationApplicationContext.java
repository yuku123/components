package com.zifang.util.core.demo.thirdpart.framworks.spring.charpter4.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.zifang.util.core.demo.thirdpart.framworks.spring.charpter4.Car;


public class AnnotationApplicationContext {

	 public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(Beans.class);
		Car car =ctx.getBean("car",Car.class);
	}
}
