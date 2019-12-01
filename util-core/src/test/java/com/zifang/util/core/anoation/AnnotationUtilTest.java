package com.zifang.util.core.anoation;

import com.zifang.util.core.annoations.AnnotationUtil;
import org.junit.Assert;
import org.junit.Test;

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