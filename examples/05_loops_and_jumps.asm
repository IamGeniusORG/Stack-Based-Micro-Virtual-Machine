// Demonstrates loops and conditional jumps
// We will count down from 5 to 0

PUSH 5
STORE 0       // Save 5 to Register 0 (Counter)

LOOP_START:
    // Load counter and print it
    LOAD 0
    PRINT

    // Decrement counter
    LOAD 0
    PUSH 1
    SUB
    STORE 0       // Save new value back to Register 0

    // Check if counter is Not Zero
    LOAD 0
    JNZ LOOP_START // If it is not 0, jump back up!

// When the loop finishes, we print a secret code
PUSH 777
PRINT
HALT
