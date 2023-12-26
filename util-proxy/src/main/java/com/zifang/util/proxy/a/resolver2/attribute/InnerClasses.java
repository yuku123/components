package com.zifang.util.proxy.a.resolver2.attribute;

import com.zifang.util.proxy.a.resolver2.readtype.U2;
import com.zifang.util.proxy.a.resolver2.readtype.U4;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//InnerClasses属性用于记录内部类与宿主类之间的关联。如果一个类中定义了内部类，那编译器将 会为它以及它所包含的内部类生成InnerClasses属性。
public class InnerClasses extends AbstractAttribute {
    private U2 numberOfClasses;
    private List<InnerClassInfo> innerClasses = new ArrayList<>(numberOfClasses.value);


    public InnerClasses(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {

    }

    public class InnerClassInfo {
        private U2 innerClassInfoIndex;
        private U2 outerClassInfoIndex;
        private U2 innerNameIndex;
        private U2 InnerClassAccessFlags;


    }
}
