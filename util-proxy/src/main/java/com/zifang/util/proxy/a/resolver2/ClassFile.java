package com.zifang.util.proxy.a.resolver2;

import com.zifang.util.proxy.a.resolver2.constantpool.ConstantPoolInfo;
import com.zifang.util.proxy.a.resolver2.field.FieldInfo;
import com.zifang.util.proxy.a.resolver2.inter.InterfaceIndex;
import com.zifang.util.proxy.a.resolver2.method.MethodInfo;
import com.zifang.util.proxy.a.resolver2.readtype.U2;
import com.zifang.util.proxy.a.resolver2.readtype.U4;

public class ClassFile {
    public static U4 magic;//魔术
    public static U2 minorVersion;//次版本号
    public static U2 majorVersion;//主版本号
    public static U2 constantPoolSize;//常量池大小
    public static ConstantPoolInfo poolInfo;//常量池内容
    public static U2 accessFlag;//类的访问标志
    public static U2 classIndex;//类索引
    public static U2 superClassIndex;//父类索引
    public static InterfaceIndex interfaceIndex;//接口索引
    public static FieldInfo fieldInfo;//字段信息
    public static MethodInfo methodInfo;//方法信息


}
