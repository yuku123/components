package com.zifang.util.designpatten.command;

public abstract class AbstractCommand implements ICommand{
    @Override
    public void execute() {
        preExecute();
        postExecute();
    }

    @Override
    public void postExecute() {

    }

    @Override
    public void preExecute() {

    }
}
