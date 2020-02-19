package com.zifang.util.zex.designpattern.command;

/**
 * 定义命令执行
 * **/
public interface ICommand extends  IPostExecutor,IPreExecutor{
    void execute();
}
