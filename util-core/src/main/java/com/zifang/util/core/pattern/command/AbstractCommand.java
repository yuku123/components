package com.zifang.util.core.pattern.command;

public abstract class AbstractCommand implements ICommand{
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
