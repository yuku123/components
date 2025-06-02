package com.zifang.util.core.anoation;

import com.zifang.util.core.lang.annoations.AnnotationUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;

public class AnnotationUtilTest {

    @ClassInfo(className = "test")
    static class WholeBase {

        @FieldInfo(name = "a-private", password = "a-private")
        private String a;
        @FieldInfo(name = "b-private", password = "b-private")
        public String b;

        @ConstructInfo(constructName = "wholeBase constructName")
        public WholeBase(@ParameterInfo(setString = "test b") String b) {}

        @ConstructInfo(constructName = "wholeBase private constructName---parameter a")
        private WholeBase(String a, @ParameterInfo(setString = "test b") String b) {}

        @Override
        @Deprecated
        @MethodInfo(author = "Pankaj", comments = "Main method", date = "Nov 17 2012", revision = 1)
        public String toString() {
            return "Overriden toString method";
        }

        @Deprecated
        @MethodInfo(author = "sddsd", comments = "aa private", date = "sdsadasdsadsadsa", revision = 1)
        private String aa() {
            return "aa private";
        }

    }

    @Test
    public void test1() {
        Assert.assertEquals("default-class-value", AnnotationUtil.getAnnotationValue(WholeBase.class, ClassInfo.class));
        Assert.assertEquals("test", AnnotationUtil.getAnnotationValue(WholeBase.class, ClassInfo.class, "className"));
        Assert.assertEquals("superInfo", AnnotationUtil.getAnnotationValue(WholeBase.class, SuperInfo.class, "superInfo"));
    }
}


