package com.zifang.util.bc.bytecode.decompile.flow;

import com.zifang.util.bc.bytecode.model.ClassFile;
import com.zifang.util.bc.bytecode.model.attribute.Code;
import com.zifang.util.bc.bytecode.model.method.MethodInfo;
import com.zifang.util.bc.bytecode.resolver.ByteCodeResolver;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * 控制流结构化：夹具的 if/else、while、do-while、for、tableswitch、lookupswitch
 * 都要还原成对应的 Java 形状，而不是一堆 goto。
 */
public class MethodFlowDecompilerTest {

    private static Code codeOf(String methodName) {
        InputStream is = MethodFlowDecompilerTest.class
                .getResourceAsStream("/testclass/FlowFixture.class");
        assertNotNull("夹具字节码资源未找到", is);
        ClassFile classFile = ByteCodeResolver.parseFromStream(is);
        assertNotNull("夹具里应有方法 " + methodName, classFile);
        for (MethodInfo method : classFile.getMethods()) {
            if (methodName.equals(method.getName())) {
                Code code = MethodFlowDecompiler.findCode(method.getAttributes());
                assertNotNull("方法 " + methodName + " 应有 Code 属性", code);
                return code;
            }
        }
        throw new AssertionError("夹具里没有方法 " + methodName);
    }

    private static String decompile(String methodName) {
        return MethodFlowDecompiler.decompileBody(codeOf(methodName), "testclass.FlowFixture");
    }

    @Test
    public void ifElseKeepsBothBranches() {
        String body = decompile("ifElse");
        assertTrue("顺序落下的块是 then 分支: " + body, body.contains("if ("));
        assertTrue("then 体应保留: " + body, body.contains("* 2"));
        assertTrue("跳转目标不是汇合点时应成为 else: " + body, body.contains("} else {"));
    }

    @Test
    public void whileLoopBecomesWhile() {
        String body = decompile("whileLoop");
        assertTrue("头块带条件应是 while: " + body, body.contains("while ("));
        assertTrue("循环体应保留: " + body, body.contains("= i +"));
        assertTrue("不该残留 goto: " + body, !body.contains("goto"));
    }

    @Test
    public void doWhileKeepsBodyBeforeCondition() {
        String body = decompile("doWhile");
        int bodyAt = body.indexOf("do {");
        int condAt = body.indexOf("} while (");
        assertTrue("尾块带条件应是 do-while: " + body, bodyAt >= 0 && condAt > bodyAt);
    }

    @Test
    public void forLoopRecoversInitConditionUpdate() {
        String body = decompile("forLoop");
        assertTrue("跳前恰好一条声明且体末尾是对同一变量的更新时还原成 for: " + body,
                body.contains("for (") && body.contains(";") && body.contains("+="));
    }

    @Test
    public void tableSwitchBecomesSwitch() {
        String body = decompile("tableSwitch");
        assertTrue("tableswitch 应还原成 switch: " + body, body.contains("switch ("));
        assertTrue("case 值应保留: " + body, body.contains("case 0:") && body.contains("case 2:"));
        assertTrue("default 分支应保留: " + body, body.contains("default:"));
    }

    @Test
    public void lookupSwitchKeepsSparseKeys() {
        String body = decompile("lookupSwitch");
        assertTrue("lookupswitch 应还原成 switch: " + body, body.contains("switch ("));
        assertTrue("稀疏键值应保留: " + body,
                body.contains("case 100:") && body.contains("case 1000:"));
    }

    @Test
    public void fieldAndInvokeKeepOwner() {
        String body = decompile("fieldAndCall");
        assertTrue("字段访问应带 this: " + body, body.contains("this.counter"));
        assertTrue("静态调用应带类名: " + body, body.contains("String.valueOf("));
    }

    @Test
    public void everyMethodProducesNonEmptyBody() {
        for (String name : new String[]{"ifElse", "whileLoop", "doWhile", "forLoop",
                "tableSwitch", "lookupSwitch", "fieldAndCall"}) {
            assertTrue("方法 " + name + " 不该产出空方法体", !decompile(name).trim().isEmpty());
        }
    }

    @Test
    public void graphStartsAtEntryAndLinksBranches() {
        ControlFlowGraph cfg = MethodFlowDecompiler.graph(codeOf("tableSwitch"));
        assertNotNull("应能建出控制流图", cfg);
        assertEquals("入口块偏移应为 0", 0, cfg.entry().getStartPc());
        assertTrue("条件分支应有后继", cfg.entry().getSuccessors().size() >= 2);
    }

    @Test
    public void backEdgeTargetsMarkLoopHeaders() {
        ControlFlowGraph cfg = MethodFlowDecompiler.graph(codeOf("whileLoop"));
        assertTrue("while 体末尾应有一条回边指回头块: " + cfg.getBackEdgeTargets(),
                !cfg.getBackEdgeTargets().isEmpty());
    }
}
