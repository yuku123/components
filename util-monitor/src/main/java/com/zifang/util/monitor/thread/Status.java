package com.zifang.util.monitor.thread;

/**
 * 监控的组件的状态。
 *
 * @author lijing
 * @since 2015/11/24
 */
public class Status {

    /**
     * 状态描述。
     */
    protected String status;

    /**
     * 状态。
     */
    protected StatusLevel level;

    /**
     * 获取状态描述。
     *
     * @return 状态描述。
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态描述。
     *
     * @param status 状态描述。
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取状态。
     *
     * @return 状态。
     */
    public StatusLevel getLevel() {
        return level;
    }

    /**
     * 设置状态。
     *
     * @param level 待设置的状态。
     */
    public void setLevel(StatusLevel level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Status{" +
                "status='" + status + '\'' +
                ", level=" + level +
                '}';
    }
}
