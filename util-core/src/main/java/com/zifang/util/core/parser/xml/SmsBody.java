package com.zifang.util.core.parser.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;

/**
 * 请求内容
 * @author xuejiangtao
 * Jul 25, 2013 9:34:16 PM
 */
@Data
public class SmsBody {

    /**
     * 短信内容
     */
    private String content;

    /**
     * 手机号
     */
    private String sourceAddr;

    /**
     * 服务代码
     */
    private String destAddr;

    public String getContent() {
        return content;
    }

    @XmlElement(name="CONTENT")
    public void setContent(String content) {
        this.content = content;
    }

    public String getSourceAddr() {
        return sourceAddr;
    }

    @XmlElement(name="SOURCEADDR")
    public void setSourceAddr(String sourceAddr) {
        this.sourceAddr = sourceAddr;
    }

    public String getDestAddr() {
        return destAddr;
    }

    @XmlElement(name="DESTADDR")
    public void setDestAddr(String destAddr) {
        this.destAddr = destAddr;
    }



}