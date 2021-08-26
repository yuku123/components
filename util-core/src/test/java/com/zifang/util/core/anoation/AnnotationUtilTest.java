package com.zifang.util.core.anoation;

import com.zifang.util.core.lang.annoations.AnnotationUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;

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

@ClassInfo(className = "test")
class WholeBase {

    @FieldInfo(name = "a-private", password = "a-private")
    private String a;
    @FieldInfo(name = "b-private", password = "b-private")
    public String b;

    @ConstructInfo(constructName = "wholeBase constructName")
    public WholeBase(@ParameterInfo(setString = "test b") String b) {
    }

    @ConstructInfo(constructName = "wholeBase private constructName---parameter a")
    private WholeBase(String a, @ParameterInfo(setString = "test b") String b) {
    }

    @Override
    @Deprecated
    @SuppressWarnings({"unchecked", "deprecation"})
    @MethodInfo(author = "Pankaj", comments = "Main method", date = "Nov 17 2012", revision = 1)
    public String toString() {
        return "Overriden toString method";
    }

    @Deprecated
    @SuppressWarnings({"unchecked", "deprecation"})
    @MethodInfo(author = "sddsd", comments = "aa private", date = "sdsadasdsadsadsa", revision = 1)
    private String aa() {
        return "aa private";
    }

}


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
@SuperInfo(superInfo = "superInfo")
@interface ClassInfo {
    String className();

    String value() default "default-class-value";
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
@interface SuperInfo {
    String superInfo();

    String value() default "default-class-value";

}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR})
@Documented
@Inherited
@interface ConstructInfo {
    String constructName();
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
@Inherited
@interface ParameterInfo {
    String setString();
}

@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface MethodInfo {
    String author() default "Pankaj";

    String date();

    int revision() default 1;

    String comments();
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
@Inherited
@interface FieldInfo {
    String name();

    String password();

    String comments() default "this is default parts";
}