package com.microvm;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Parses raw custom assembly text and converts it into a clean executable byte array.
 */
public class Parser {

    /**
     * Reads a text file of custom assembly and outputs compiled bytecode.
     * 
     * @param filePath The path to the assembly file
     * @return A primitive byte array representing the compiled program
     * @throws IOException If file reading fails
     */
    public static byte[] parse(Path filePath) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        StringBuilder sb = new StringBuilder();

        // Strip comments line by line
        for (String line : lines) {
            int commentIndex = line.indexOf("//");
            if (commentIndex != -1) {
                line = line.substring(0, commentIndex);
            }
            sb.append(line).append(" ");
        }

        // Sanitize input: Replace commas with spaces, trim, and split by any whitespace.
        String[] tokens = sb.toString().replace(",", " ").trim().split("\\s+");
        
        // If empty file
        if (tokens.length == 1 && tokens[0].isEmpty()) {
            return new byte[0];
        }

        // Allocate a generous buffer. 
        ByteBuffer buffer = ByteBuffer.allocate(tokens.length * 5);
        
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].isEmpty()) {
                continue;
            }
            
            Instruction inst = Instruction.fromString(tokens[i]);
            buffer.put(inst.getOpcode());
            
            if (inst.hasOperand()) {
                i++; // Advance to the operand token
                if (i >= tokens.length) {
                    throw new IllegalArgumentException("Missing operand for instruction: " + inst.name());
                }
                
                try {
                    int operand = Integer.parseInt(tokens[i]);
                    buffer.putInt(operand); // 4 bytes for the integer operand
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid integer operand: " + tokens[i]);
                }
            }
        }
        
        // Extract the exact sized byte array from the buffer
        byte[] program = new byte[buffer.position()];
        buffer.flip();
        buffer.get(program);
        return program;
    }
}
