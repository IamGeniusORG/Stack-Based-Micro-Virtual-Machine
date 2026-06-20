package com.microvm;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses raw custom assembly text and converts it into a clean executable byte array.
 * Supports a two-pass assembly process to resolve jump labels.
 */
public class Parser {

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

        // Sanitize input
        String[] tokens = sb.toString().replace(",", " ").trim().split("\\s+");
        
        if (tokens.length == 1 && tokens[0].isEmpty()) {
            return new byte[0];
        }

        // Pass 1: Compute Byte Offsets and Register Labels
        Map<String, Integer> labels = new HashMap<>();
        int currentByteOffset = 0;
        
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (token.isEmpty()) continue;
            
            // If token is a label definition (ends with colon)
            if (token.endsWith(":")) {
                String labelName = token.substring(0, token.length() - 1);
                labels.put(labelName, currentByteOffset);
                continue;
            }
            
            Instruction inst = Instruction.fromString(token);
            currentByteOffset += 1; // 1 byte for opcode
            
            if (inst.hasOperand()) {
                currentByteOffset += 4; // 4 bytes for integer operand
                i++; // Skip the operand token in this pass
            }
        }

        // Pass 2: Generate Bytecode
        ByteBuffer buffer = ByteBuffer.allocate(currentByteOffset);
        
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (token.isEmpty() || token.endsWith(":")) {
                continue; // Skip empty tokens and label definitions
            }
            
            Instruction inst = Instruction.fromString(token);
            buffer.put(inst.getOpcode());
            
            if (inst.hasOperand()) {
                i++;
                if (i >= tokens.length) {
                    throw new IllegalArgumentException("Missing operand for instruction: " + inst.name());
                }
                
                String operandStr = tokens[i];
                int operand;
                
                // If the operand is a known label, use its byte offset. Otherwise parse as Integer.
                if (labels.containsKey(operandStr)) {
                    operand = labels.get(operandStr);
                } else {
                    try {
                        operand = Integer.parseInt(operandStr);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Unknown label or invalid integer operand: " + operandStr);
                    }
                }
                
                buffer.putInt(operand);
            }
        }
        
        byte[] program = new byte[buffer.position()];
        buffer.flip();
        buffer.get(program);
        return program;
    }
}
