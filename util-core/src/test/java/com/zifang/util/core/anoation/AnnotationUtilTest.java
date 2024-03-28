package com.zifang.util.core.anoation;

import com.zifang.util.core.lang.annoations.AnnotationUtil;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilTest {

    @Test
    public void test1() {
        Assert.assertEquals("default-class-value", AnnotationUtil.getAnnotationValue(WholeBase.class, ClassInfo.class));
        Assert.assertEquals("test", AnnotationUtil.getAnnotationValue(WholeBase.class, ClassInfo.class, "className"));
        Assert.assertEquals("superInfo", AnnotationUtil.getAnnotationValue(WholeBase.class, SuperInfo.class, "superInfo"));
    }
}


