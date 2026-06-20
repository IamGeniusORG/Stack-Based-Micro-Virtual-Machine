package com.microvm;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Entry point to compile and run the Micro-VM.
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Read target file path from arguments or default to "program.asm"
            String filePathString = args.length > 0 ? args[0] : "program.asm";
            Path filePath = Paths.get(filePathString);
            
            System.out.println("Reading assembly from: " + filePath.toAbsolutePath());
            
            // Step 1: Parse string assembly to clean executable bytecode
            byte[] bytecode = Parser.parse(filePath);
            System.out.println("Assembly successfully compiled. Total program size: " + bytecode.length + " bytes.\n");
            
            // Step 2: Initialize Virtual Machine with a stack capacity of 256
            VirtualMachine vm = new VirtualMachine(256);
            
            // Step 3: Execute program
            vm.execute(bytecode);
            
        } catch (Exception e) {
            System.err.println("Fatal VM Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
