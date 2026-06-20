// Saving and Loading Variables (Registers)
// We have 4 registers: 0, 1, 2, and 3

// Let's calculate: (A + B) * A
// A = 5
// B = 10

PUSH 5
STORE 0     // Save 5 to Register 0 (A)

PUSH 10
STORE 1     // Save 10 to Register 1 (B)

// Now load A and B back to the stack to add them
LOAD 0
LOAD 1
ADD         // Stack now has 15

// Now load A again to multiply
LOAD 0      // Stack now has 15, 5
MUL         // Stack now has 75

PRINT       // Print the result
HALT
