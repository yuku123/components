package com.zifang.util.core.pattern.command;

/**
 * @author zifang
 */
public abstract class AbstractCommand implements ICommand {
    @Override
    public void execute() {
        preExecute();
        execute();
        postExecute();
    }

    @Override
    public void postExecute() {

    }

    @Override
    public void preExecute() {

    }
}
