package com.zifang.util.source.generator;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;

import static com.github.javaparser.ast.Modifier.Keyword.PUBLIC;
import static com.github.javaparser.ast.Modifier.Keyword.STATIC;
import static com.github.javaparser.ast.Modifier.createModifierList;

public class ADemo {
    public static void main(String[] args) {
        CompilationUnit compilationUnit = new CompilationUnit();
        compilationUnit.setPackageDeclaration("com.abc.def");
        compilationUnit.addImport("org.springframework.boot.SpringApplication");
        ClassOrInterfaceDeclaration classDeclaration = compilationUnit.addClass("AnyClassName").setPublic(true);
        classDeclaration.addAnnotation("AnyAnnotation");
        classDeclaration.addExtendedType("aaa");
        classDeclaration.setAbstract(true);
//        FieldDeclaration fieldDeclaration = classDeclaration.addField("String", "title",PUBLIC,STATIC);

        FieldDeclaration fieldDeclaration = new FieldDeclaration();
        VariableDeclarator variable = new VariableDeclarator(StaticJavaParser.parseType("String"), "title");
        variable.setInitializer(new NameExpr("\"sdasdasd\""));
        fieldDeclaration.getVariables().add(variable);
        fieldDeclaration.setModifiers(createModifierList(PUBLIC, STATIC));
        classDeclaration.getMembers().add(fieldDeclaration);

        classDeclaration.addConstructor(PUBLIC)
                .addParameter("String", "title")
                .addParameter("Person", "author")
                .setBody(new BlockStmt()
                        .addStatement(new ExpressionStmt(new AssignExpr(
                                new FieldAccessExpr(new ThisExpr(), "title"),
                                new NameExpr("title"),
                                AssignExpr.Operator.ASSIGN)))
                        .addStatement(new ExpressionStmt(new AssignExpr(
                                new FieldAccessExpr(new ThisExpr(), "author"),
                                new NameExpr("author"),
                                AssignExpr.Operator.ASSIGN))));


        // 给类增加一个方法
        MethodDeclaration methodDeclaration = classDeclaration.addMethod("anyMethodName", PUBLIC);
        methodDeclaration.setType("AnyReturnType");
        // 方法的入参
        methodDeclaration.addAndGetParameter(String.class, "args").setVarArgs(false);// setVarArgs(true)表达是否多参
        methodDeclaration.addAndGetParameter(String.class, "cc").setVarArgs(false);// setVarArgs(true)表达是否多参
        // 方法增加注解
        methodDeclaration.addAnnotation("AnyAnnotation");
        methodDeclaration.addMarkerAnnotation("Override");
        // 增加method的代码块儿
        BlockStmt blockStmt = new BlockStmt();
        methodDeclaration.setBody(blockStmt);

        blockStmt.addStatement("int i = 4;");
        blockStmt.addStatement("int i = 4;");
        blockStmt.addStatement("int i = 4;");
        blockStmt.addStatement("int i = 4;");
        blockStmt.addStatement("int i = 4;");

        String code = compilationUnit.toString();
        System.out.println(code);

    }
}
