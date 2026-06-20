package com.microvm;

/**
 * Represents the instruction set architecture (ISA) for our Micro-VM.
 */
public enum Instruction {
    HALT((byte) 0x00, false),
    PUSH((byte) 0x01, true),
    POP((byte) 0x02, false),
    
    // Arithmetic Operations
    ADD((byte) 0x03, false),
    SUB((byte) 0x04, false),
    MUL((byte) 0x06, false),
    DIV((byte) 0x07, false),
    MOD((byte) 0x08, false),
    
    // Stack Manipulation Operations
    DUP((byte) 0x09, false),
    SWAP((byte) 0x0A, false),
    
    // Variables (Registers)
    STORE((byte) 0x0C, true),
    LOAD((byte) 0x0D, true),
    
    // Logical Operations
    EQ((byte) 0x0B, false),
    
    PRINT((byte) 0x05, false);

    private final byte opcode;
    private final boolean hasOperand;

    Instruction(byte opcode, boolean hasOperand) {
        this.opcode = opcode;
        this.hasOperand = hasOperand;
    }

    public byte getOpcode() { 
        return opcode; 
    }
    
    public boolean hasOperand() { 
        return hasOperand; 
    }

    public static Instruction fromString(String token) {
        for (Instruction inst : values()) {
            if (inst.name().equalsIgnoreCase(token)) {
                return inst;
            }
        }
        throw new IllegalArgumentException("Unknown instruction token: " + token);
    }
    
    public static Instruction fromOpcode(byte opcode) {
        for (Instruction inst : values()) {
            if (inst.opcode == opcode) {
                return inst;
            }
        }
        throw new IllegalArgumentException(String.format("Unknown opcode: 0x%02X", opcode));
    }
}
