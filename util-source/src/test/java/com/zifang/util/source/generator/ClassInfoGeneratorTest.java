package com.zifang.util.source.generator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.zifang.util.source.generator.info.ModifierAdapter;
import org.junit.Test;

import java.lang.reflect.Modifier;

public class ClassInfoGeneratorTest {
    @Test
    public void test1(){
        // 简单诞生一个object javaClass
        CompilationUnit compilationUnit = new CompilationUnit();
        compilationUnit.setPackageDeclaration("com.abc.def");
        compilationUnit.addImport("org.springframework.boot.SpringApplication");
        ClassOrInterfaceDeclaration classDeclaration = compilationUnit.addClass("AnyClassName");
        classDeclaration.addMarkerAnnotation("AnyAnnotation");
        classDeclaration.addExtendedType("aaa1");
        classDeclaration.addImplementedType("aaa2");

        classDeclaration.setModifiers(
                ModifierAdapter.getKeyWord(Modifier.PUBLIC)
        );
        classDeclaration.setJavadocComment(new JavadocComment("dasdasda\ndsdasdasdas\ndaseeeeedasd"));
        classDeclaration.setJavadocComment(new JavadocComment("dasdasda\ndsdasdasdas\ndasdasd"));

        System.out.println(compilationUnit.toString());
    }

    @Test
    public void test2(){
        CompilationUnit compilationUnit = new CompilationUnit();
        compilationUnit.setPackageDeclaration("com.abc.def");
        ClassOrInterfaceDeclaration classDeclaration = compilationUnit.addClass("AnyClassName");
        System.out.println(compilationUnit.toString());

    }
}
