package com.zifang.util.expression.instruction;

import lombok.Data;

@Data
public class Instruction {

    private String instructionCode;

    private Object[] params;

    public static Instruction of(String instructionCode, Object[] params) {
        Instruction instruction = new Instruction();
        instruction.setInstructionCode(instructionCode);
        instruction.setParams(params);
        return instruction;
    }
}
