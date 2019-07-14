package com.zifang.util.designpatten.command;

/**
 * 定义命令执行
 * **/
public interface ICommand extends  IPostExecutor,IPreExecutor{
    void execute();
}
