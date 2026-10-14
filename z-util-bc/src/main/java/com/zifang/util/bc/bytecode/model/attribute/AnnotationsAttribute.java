package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.annotation.Annotation;
import com.zifang.util.bc.bytecode.model.readtype.ByteReader;
import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 注解容器属性（4.7.16）的公共骨架
 * <p>
 * 体是 num_annotations(u2) 加若干 annotation，与 {@link RuntimeVisibleAnnotationsAttribute}
 * 和 {@link RuntimeInvisibleAnnotationsAttribute} 结构相同，只有名字和语义不同。
 * <p>
 * 没动过这个属性时原样回放 {@code rawBody}：注解是编译期常量，编码里没有任何
 * 需要重算的东西，回放能保证没碰过的类逐字节不变。
 */
public abstract class AnnotationsAttribute extends AbstractAttribute {

    private final List<Annotation> annotations = new ArrayList<>();
    private byte[] rawBody;
    private boolean annotationsReplaced;

    protected AnnotationsAttribute(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {
        ByteReader reader = new ByteReader(inputStream);
        rawBody = reader.readFully(getAttributeLength().getValue());
        ByteReader body = new ByteReader(new java.io.ByteArrayInputStream(rawBody));
        int n = body.readU2().intValue();
        for (int i = 0; i < n; i++) {
            annotations.add(Annotation.read(body));
        }
    }

    /**
     * 已解析出的注解列表
     *
     * @return ListAnnotation类型返回值
     */
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    /**
     * 注解条数
     *
     * @return int类型返回值
     */
    public int getNumAnnotations() {
        return annotations.size();
    }

    /**
     * 解析时的原始属性体
     *
     * @return byte[]类型返回值
     */
    public byte[] getRawBody() {
        return rawBody;
    }

    /**
     * 追加一条注解
     * <p>
     * 必须走这里而不是直接改 {@link #getAnnotations()} 返回的列表：
     * 那个列表是活的，不经本方法就翻不掉「原样回放原始字节」的开关，
     * 新加的那条会在写出时被原始字节顶掉。
     *
     * @param annotation 要追加的注解
     */
    public void addAnnotation(Annotation annotation) {
        annotations.add(annotation);
        annotationsReplaced = true;
    }

    /**
     * 整体替换注解列表
     *
     * @param newAnnotations 新的注解列表
     */
    public void setAnnotations(List<Annotation> newAnnotations) {
        annotations.clear();
        annotations.addAll(newAnnotations);
        annotationsReplaced = true;
    }

    @Override
    protected void writeBody(DataOutput out) throws IOException {
        if (rawBody != null && !annotationsReplaced) {
            out.write(rawBody);
            return;
        }
        out.writeShort(annotations.size());
        for (Annotation a : annotations) {
            a.write(out);
        }
    }

    @Override
    public int contentSize() {
        if (rawBody != null && !annotationsReplaced) {
            return rawBody.length;
        }
        int n = 2;
        for (Annotation a : annotations) {
            n += a.contentSize();
        }
        return n;
    }
}
