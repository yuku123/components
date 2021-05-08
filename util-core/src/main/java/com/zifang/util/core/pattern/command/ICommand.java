package com.zifang.util.core.pattern.command;

/**
 * 定义命令执行
 *
 * @author zifang
 **/
public interface ICommand extends IPostExecutor, IPreExecutor {
    void execute();
}
