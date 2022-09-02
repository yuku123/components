package com.zifang.util.core.pattern.command;

/**
 * @author zifang
 */
public interface IReceiver {

    /**
     * @param iCommand
     */
    void except(ICommand iCommand);
}