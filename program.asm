// ==============================================================
// 🚀 MICRO-VM ULTIMATE SHOWCASE: FIBONACCI SEQUENCE GENERATOR
// ==============================================================
// This program calculates and prints the first 10 Fibonacci numbers.
// It demonstrates our Turing-Complete ISA using Registers, Math, 
// and Conditional Loops!

// --- 1. INITIALIZE VARIABLES ---
PUSH 0
STORE 0     // Register 0 (A) = 0

PUSH 1
STORE 1     // Register 1 (B) = 1

PUSH 10
STORE 2     // Register 2 (Loop Counter) = 10

// --- 2. LOOP EXECUTION ---
FIB_LOOP:
    // Print the current Fibonacci number (A)
    LOAD 0
    PRINT

    // Calculate Next Number = A + B
    LOAD 0
    LOAD 1
    ADD
    STORE 3 // Register 3 (Temp) = A + B

    // Shift Variables: A = B, B = Temp
    LOAD 1
    STORE 0 // A = B
    
    LOAD 3
    STORE 1 // B = Temp

    // Decrement our loop counter by 1
    LOAD 2
    PUSH 1
    SUB
    STORE 2 // Counter = Counter - 1

    // If Counter is Not Zero (JNZ), jump back to the start of the loop!
    LOAD 2
    JNZ FIB_LOOP 

// --- 3. END OF PROGRAM ---
// Print a success code to show we finished
PUSH 9999
PRINT
HALT
