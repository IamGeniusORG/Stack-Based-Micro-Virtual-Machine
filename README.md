<div align="center">
  <h1>⚙️ Micro-VM: Stack-Based Execution Engine</h1>
  <p><i>A lightweight, high-performance Stack-Based Virtual Machine written entirely in pure Java.</i></p>
</div>

---

## 📖 Overview
Micro-VM is an educational yet completely operational Virtual Machine built from scratch. It defines a custom, lightweight **Instruction Set Architecture (ISA)**, parses raw assembly code into pristine executable bytecode arrays, and simulates an isolated environment using an internal Instruction Pointer (IP) and an integer operand stack.

The codebase is engineered to demonstrate the lowest-level concepts of interpreters and compilers in a clean, professional, and accessible way.

## ✨ Features
- **Zero Dependencies**: Pure Java implementation. No Maven, no Gradle, no heavy frameworks.
- **Custom Assembly Parser**: Converts human-readable text like `PUSH 5` or `ADD` directly into raw primitive `byte[]` arrays.
- **Robust Execution Loop**: Runs bytecodes sequentially via an internal stack engine and handles errors (e.g. Stack Underflow, Division by zero) gracefully.
- **Step-by-Step Tracing**: Outputs the exact state of the IP and memory stack at every execution tick for deep observability.

## 🛠️ Instruction Set Architecture (ISA)
| Opcode | Instruction | Description | Operands |
| --- | --- | --- | --- |
| `0x01` | **`PUSH`** | Pushes an integer onto the top of the stack | `[integer]` |
| `0x02` | **`POP`** | Removes the top item from the stack | *None* |
| `0x03` | **`ADD`** | Pops two items, adds them, and pushes the result | *None* |
| `0x04` | **`SUB`** | Pops two items, subtracts them, and pushes the result | *None* |
| `0x06` | **`MUL`** | Pops two items, multiplies them, and pushes the result | *None* |
| `0x07` | **`DIV`** | Pops two items, divides them, and pushes the result | *None* |
| `0x08` | **`MOD`** | Pops two items, gets the modulo, and pushes the result | *None* |
| `0x09` | **`DUP`** | Duplicates the item currently at the top of the stack | *None* |
| `0x0A` | **`SWAP`**| Swaps the positions of the top two items on the stack | *None* |
| `0x0B` | **`EQ`**  | Pops two items, if equal pushes `1`, otherwise `0` | *None* |
| `0x0C` | **`STORE`**| Pops top item and stores it in the given register (0-3)| `[index]` |
| `0x0D` | **`LOAD`** | Copies value from given register (0-3) and pushes it | `[index]` |
| `0x0E` | **`JMP`**  | Jumps the execution to a specific Label | `[label]` |
| `0x0F` | **`JZ`**   | Pops top item, if it's `0`, jumps to the Label | `[label]` |
| `0x10` | **`JNZ`**  | Pops top item, if it's NOT `0`, jumps to the Label | `[label]` |
| `0x05` | **`PRINT`**| Prints the top item of the stack to the console | *None* |
| `0x00` | **`HALT`**| Terminates the VM execution loop safely | *None* |

## 🚀 Quick Start (Compile & Run)
You can compile and run this application natively using the standard Java Development Kit.

```bash
# 1. Clone the repository
git clone https://github.com/yourusername/micro-vm.git
cd micro-vm

# 2. Compile the Java Source Files
mkdir bin
javac -d bin src/com/microvm/*.java

# 3. Run a custom assembly program (examples included!)
java -cp bin com.microvm.Main examples/01_math_basics.asm
```

## 📂 Project Structure
```text
.
├── examples/                 # Folder containing test .asm programs
│   ├── 01_math_basics.asm    # Demonstrates ADD, SUB, MUL, DIV
│   ├── 02_stack_tricks.asm   # Demonstrates DUP and SWAP
│   ├── 03_logic_equality.asm # Demonstrates EQ operator
│   ├── 04_variables.asm      # Demonstrates STORE and LOAD with registers
│   └── 05_loops_and_jumps.asm# Demonstrates Turing Complete Control Flow!
├── program.asm               # Default assembly sandbox
├── src/com/microvm/
│   ├── Instruction.java      # Defines the ISA and mapping logic
│   ├── Parser.java           # Reads .asm files and builds byte arrays
│   ├── VirtualMachine.java   # Core VM Loop and Stack Management
│   └── Main.java             # Entry point
```

## 💻 Sample Execution Trace
Running `examples/01_math_basics.asm`:
```text
Reading assembly from: ...\examples\01_math_basics.asm
Assembly successfully compiled. Total program size: 14 bytes.

--- VM Execution Started ---
IP: 0005 | PUSH 10      | Stack: [10]
IP: 0010 | PUSH 5       | Stack: [10, 5]
IP: 0011 | MUL          | Stack: [50]
IP: 0016 | PUSH 2       | Stack: [50, 2]
IP: 0017 | DIV          | Stack: [25]
>> PRINT OUT: 25
IP: 0018 | PRINT        | Stack: [25]
IP: 0019 | HALT         | Stack: [25]
--- VM Execution Finished ---
```
