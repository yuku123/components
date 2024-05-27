package com.zifang.util.core.lang.dynamic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zifang
 */

public class DynamicClass {

    private List<DynamicImport> imports = new ArrayList<>();

    private String packageName;

    private List<DynamicAnnotation> annotations = new ArrayList<>();

    private Boolean isInterface;

    private String className;

    /**
     * 实现的接口类
     */
    private List<DynamicClass> implementClasses = new ArrayList<>();

    /**
     * 继承的bean
     */
    private DynamicClass extendClass;

    /**
     * 动态bean字段
     */
    private List<DynamicField> fields = new ArrayList<>();

    /**
     * 动态方法
     */
    private List<DynamicMethod> methods = new ArrayList<>();


    public List<DynamicClass> getImplementClasses() {
        return implementClasses;
    }

    public void setImplementClasses(List<DynamicClass> implementClasses) {
        this.implementClasses = implementClasses;
    }

    public DynamicClass getExtendClass() {
        return extendClass;
    }

    public void setExtendClass(DynamicClass extendClass) {
        this.extendClass = extendClass;
    }

    public List<DynamicField> getFields() {
        return fields;
    }

    public void setFields(List<DynamicField> fields) {
        this.fields = fields;
    }

    public List<DynamicMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<DynamicMethod> methods) {
        this.methods = methods;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Boolean getInterface() {
        return isInterface;
    }

    public void setInterface(Boolean anInterface) {
        isInterface = anInterface;
    }

    public List<DynamicImport> getImports() {
        return imports;
    }

    public void setImports(List<DynamicImport> imports) {
        this.imports = imports;
    }

    public List<DynamicAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<DynamicAnnotation> annotations) {
        this.annotations = annotations;
    }

    public String generateSourceCode() {

        if(className == null){
            className = "Bean_"+System.currentTimeMillis();
        }

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("public class "+ className +"{\n");

        for(DynamicField dynamicField : fields){
            String te = "\tprivate %s %s;\n";
            stringBuffer.append(String.format(te, dynamicField.getType(), dynamicField.getName()));
        }

        for(DynamicMethod dynamicMethod : methods){
            String te = "\tpublic %s %s(%s){\n\t\t%s\n\t}\n";
            String code = String.format(te,
                    dynamicMethod.getReturnType(),
                    dynamicMethod.getMethodName(),
                    String.join(",", dynamicMethod.getParameters().stream().map(e->e.toString()).collect(Collectors.toList())),
                    dynamicMethod.getBody());

            stringBuffer.append(code);
        }

        stringBuffer.append("}");

        return stringBuffer.toString();
    }

    public String getType(){
        if(packageName == null){
            return className;
        } else {
            return packageName + "." + className;
        }
    }

    public void addField(String key, Class<?> clazz) {
        addField(key, clazz, false,false);
    }

    public void addField(String key, Class<?> clazz, boolean generateGetter, boolean generateSetter) {
        addField(key,clazz.getName(), generateGetter, generateSetter);
    }

    public void addField(String key, String type, boolean generateGetter, boolean generateSetter) {

        fields.add(DynamicField.of(key, type));

        if(generateGetter){
            String methodName = "get" + key.substring(0,1).toUpperCase() + key.substring(1);
            methods.add(DynamicMethod.of(
                    methodName,
                    new ArrayList<>(),
                    type, "return this."+key+";"));
        }
    }
}
