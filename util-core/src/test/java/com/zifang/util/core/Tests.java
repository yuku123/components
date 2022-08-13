package com.zifang.util.core;

import com.zifang.util.core.io.parser.xml.TitleRequest;
import com.zifang.util.core.io.parser.xml.XmlBeanUtil;
import com.zifang.util.core.util.FileUtil;
import com.zifang.util.core.util.GsonUtil;
import org.junit.Test;

public class Tests {

    @Test
    public void test1() {
        String content = FileUtil.readFile("test.xml");
        TitleRequest titleRequest = XmlBeanUtil.xmlToBean(content, TitleRequest.class);
        System.out.println(GsonUtil.objectToJsonStr(titleRequest));
    }
}
