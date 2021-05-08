package com.zifang.util.core.io.ftp;

//import com.zifang.util.zex.helper.DateHelper;

/**
 * Ftp操作日志
 * @author Created by Administrator on 2015-04-08.
 */
public class FTPLog {
    private String host;
    private String operation;
    private int ReplyCode;
    private String localFile;
    private String remoteFile;
    private String ReplyCodeDesc;
    //private String createTime = DateHelper.currentDateTime();

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getReplyCode() {
        return ReplyCode;
    }

    public void setReplyCode(int replyCode) {
        ReplyCode = replyCode;
    }

    public String getLocalFile() {
        return localFile;
    }

    public void setLocalFile(String localFile) {
        this.localFile = localFile;
    }

    public String getRemoteFile() {
        return remoteFile;
    }

    public void setRemoteFile(String remoteFile) {
        this.remoteFile = remoteFile;
    }

    public String getReplyCodeDesc() {
        return ReplyCodeDesc;
    }

    public void setReplyCodeDesc(String replyCodeDesc) {
        ReplyCodeDesc = replyCodeDesc;
    }


    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

}
