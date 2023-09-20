package com.zifang.util.core.parser.xml;

import javax.xml.bind.annotation.XmlElement;

/**
 * 请求头对象
 */
public class ReqHeader {

    /**
     * 系统编号
     */
    private String sysId;

    /**
     * 鉴权码
     */
    private String authCode;

    /**
     * 流水号
     */
    private String reqNo;

    public String getSysId() {
        return sysId;
    }

    @XmlElement(name = "SYSID")
    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getAuthCode() {
        return authCode;
    }

    @XmlElement(name = "AUTHCODE")
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getReqNo() {
        return reqNo;
    }

    @XmlElement(name = "REQNO")
    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }


}