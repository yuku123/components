package com.zifang.util.core.anoation;

import com.zifang.util.core.lang.annoations.AnnotationUtil;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilTest {

    @Test
    public void test1() {
        Object value = AnnotationUtil.getAnnotationValue(WholeBase.class, ClassInfo.class);
        Object className = AnnotationUtil.getAnnotationValue(WholeBase.class, ClassInfo.class, "className");
        Object superInfo = AnnotationUtil.getAnnotationValue(WholeBase.class, SuperInfo.class, "superInfo");
        Assert.assertEquals("default-class-value", value);
        Assert.assertEquals("superInfo", superInfo);
        Assert.assertEquals("test", className);
    }
}


