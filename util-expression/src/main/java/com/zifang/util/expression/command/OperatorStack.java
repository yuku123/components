package com.zifang.util.expression.command;

import java.util.LinkedList;

public class OperatorStack {

    private  LinkedList<Object> stack = null;

    public OperatorStack(){
        stack = new LinkedList<>();
    }

    public void push(Object o){
        stack.push(o);
    }

    public Object pop(){
        return stack.pop();
    }


}
