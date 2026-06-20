package com.microvm;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * The core execution engine representing our Micro-VM.
 */
public class VirtualMachine {
    private final int[] stack;
    private final int[] registers;
    private int stackPointer;
    private int instructionPointer;
    private boolean running;

    /**
     * Initializes a new Virtual Machine.
     * @param stackSize The maximum capacity of the internal stack
     */
    public VirtualMachine(int stackSize) {
        this.stack = new int[stackSize];
        this.registers = new int[4]; // 4 General Purpose Registers (0, 1, 2, 3)
        this.stackPointer = -1; // -1 means stack is empty
        this.instructionPointer = 0;
        this.running = false;
    }

    /**
     * Executes the given bytecode array.
     * @param program The compiled primitive byte array to run
     */
    public void execute(byte[] program) {
        this.instructionPointer = 0;
        this.running = true;
        ByteBuffer buffer = ByteBuffer.wrap(program);

        System.out.println("--- VM Execution Started ---");
        
        while (running && instructionPointer < program.length) {
            buffer.position(instructionPointer);
            byte opcode = buffer.get();
            instructionPointer++;
            
            Instruction inst = Instruction.fromOpcode(opcode);
            int operand = 0;
            
            if (inst.hasOperand()) {
                operand = buffer.getInt();
                instructionPointer += 4; // Int is 4 bytes
            }

            executeInstruction(inst, operand);
            printState(inst, operand);
        }
        
        System.out.println("--- VM Execution Finished ---");
    }

    /**
     * Handles the execution of a single parsed instruction.
     */
    private void executeInstruction(Instruction inst, int operand) {
        int a, b;
        switch (inst) {
            case PUSH:
                stack[++stackPointer] = operand;
                break;
            case POP:
                ensureStack(1, "POP");
                stackPointer--;
                break;
            case ADD:
                ensureStack(2, "ADD");
                b = stack[stackPointer--];
                a = stack[stackPointer];
                stack[stackPointer] = a + b;
                break;
            case SUB:
                ensureStack(2, "SUB");
                b = stack[stackPointer--];
                a = stack[stackPointer];
                stack[stackPointer] = a - b;
                break;
            case MUL:
                ensureStack(2, "MUL");
                b = stack[stackPointer--];
                a = stack[stackPointer];
                stack[stackPointer] = a * b;
                break;
            case DIV:
                ensureStack(2, "DIV");
                b = stack[stackPointer--];
                a = stack[stackPointer];
                if (b == 0) throw new ArithmeticException("Division by zero");
                stack[stackPointer] = a / b;
                break;
            case MOD:
                ensureStack(2, "MOD");
                b = stack[stackPointer--];
                a = stack[stackPointer];
                if (b == 0) throw new ArithmeticException("Modulo by zero");
                stack[stackPointer] = a % b;
                break;
            case DUP:
                ensureStack(1, "DUP");
                a = stack[stackPointer];
                stack[++stackPointer] = a;
                break;
            case SWAP:
                ensureStack(2, "SWAP");
                b = stack[stackPointer];
                a = stack[stackPointer - 1];
                stack[stackPointer] = a;
                stack[stackPointer - 1] = b;
                break;
            case STORE:
                if (operand < 0 || operand > 3) throw new IllegalArgumentException("Invalid register index: " + operand);
                ensureStack(1, "STORE");
                registers[operand] = stack[stackPointer--];
                break;
            case LOAD:
                if (operand < 0 || operand > 3) throw new IllegalArgumentException("Invalid register index: " + operand);
                stack[++stackPointer] = registers[operand];
                break;
            case JMP:
                instructionPointer = operand;
                break;
            case JZ:
                ensureStack(1, "JZ");
                a = stack[stackPointer--];
                if (a == 0) instructionPointer = operand;
                break;
            case JNZ:
                ensureStack(1, "JNZ");
                a = stack[stackPointer--];
                if (a != 0) instructionPointer = operand;
                break;
            case EQ:
                ensureStack(2, "EQ");
                b = stack[stackPointer--];
                a = stack[stackPointer];
                stack[stackPointer] = (a == b) ? 1 : 0;
                break;
            case PRINT:
                ensureStack(1, "PRINT");
                System.out.println(">> PRINT OUT: " + stack[stackPointer]);
                break;
            case HALT:
                running = false;
                break;
        }
    }

    /**
     * Fails fast if the stack does not have the required number of items.
     */
    private void ensureStack(int requiredItems, String operation) {
        if (stackPointer + 1 < requiredItems) {
            throw new RuntimeException("Stack Underflow during " + operation + ". Required: " + requiredItems + ", Available: " + (stackPointer + 1));
        }
    }

    /**
     * Helper to print internal state (IP, Instruction, and Stack)
     */
    private void printState(Instruction inst, int operand) {
        String instStr = inst.name() + (inst.hasOperand() ? " " + operand : "");
        System.out.printf("IP: %04d | %-12s | Stack: ", instructionPointer, instStr);
        
        if (stackPointer < 0) {
            System.out.print("[]");
        } else {
            int[] currentStack = Arrays.copyOfRange(stack, 0, stackPointer + 1);
            System.out.print(Arrays.toString(currentStack));
        }
        System.out.println(" | Regs: " + Arrays.toString(registers));
    }
}
