package com.zifang.util.expression;

import com.zifang.util.expression.instruction.CommandExecutor;
import com.zifang.util.expression.instruction.Instruction;
import com.zifang.util.expression.instruction.OperatorStack;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CommandTest {

    @Test
    public void test0(){

        OperatorStack stack = new OperatorStack();
        List<Instruction> commandList = new ArrayList<>();

        CommandExecutor commandExecutor = new CommandExecutor();
        Object r = commandExecutor.execute(stack, commandList);

    }

}
