package com.zifang.util.core.anoation;

import com.zifang.util.core.annoations.AnnotationUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class AnnotationUtilTest {
	
	@Test
	public void t1() {
		Object value = AnnotationUtil.getAnnotationValue(ClassWithAnnotation.class, AnnotationForTest.class);
		Object v = AnnotationUtil.getAnnotationValue(ClassWithAnnotation.class, ClassInfo.class,"className");
		System.out.println(v);
		Object v1 = AnnotationUtil.getAnnotationValue(ClassWithAnnotation.class, superInfo.class,"superInfo");
		Assert.assertEquals("测试", value);
		Assert.assertEquals("superInfo", v1);
	}
}

@ClassInfo(className = "name")
@AnnotationForTest("测试")
class ClassWithAnnotation{

	private String name;
	private String password;

}

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@interface AnnotationForTest {

	/**
	 * 注解的默认属性值
	 *
	 * @return 属性值
	 */
	String value();
}
