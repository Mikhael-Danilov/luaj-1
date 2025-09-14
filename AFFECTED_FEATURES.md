# Luaj Features Affected by Missing Classes in TeaVM

This document details which specific Luaj features are affected by the missing classes and methods in TeaVM, based on the error messages from our compatibility testing.

## 1. System.exit(I)V Method

### Affected Features:
- **Main lua.java launcher**: The main entry point calls `System.exit()` when displaying usage information or when exiting normally
- **OS Library (os.exit)**: The `os.exit()` function in Lua scripts will not work

### Files Affected:
- `lua.java` (lines 62, 122, 168)
- `org/luaj/vm2/lib/OsLib.java` (line 409)

### Impact:
- Lua scripts cannot exit the program using `os.exit()`
- Command-line usage/help display will cause runtime errors

## 2. java.lang.reflect.Proxy Class

### Affected Features:
- **Luajava Library**: The reflection-based library that allows Lua scripts to interact with Java objects
- **Dynamic Proxy Creation**: Creating proxy objects that implement Java interfaces

### Files Affected:
- `org/luaj/vm2/lib/jse/LuajavaLib.java` (line 146)

### Impact:
- Lua scripts cannot create dynamic proxy objects
- Limited Java object interaction capabilities

## 3. java.lang.Runtime.exec(String) Method

### Affected Features:
- **IO Library (popen)**: Process execution through `io.popen()` function
- **OS Library (os.execute)**: Process execution through `os.execute()` function

### Files Affected:
- `org/luaj/vm2/lib/jse/JseIoLib.java` (line 98)
- `org/luaj/vm2/lib/jse/JseProcess.java` (line 58)
- `org/luaj/vm2/lib/jse/JseOsLib.java` (line 97)

### Impact:
- Lua scripts cannot execute external programs
- Process I/O operations will fail

## 4. java.lang.Process Class

### Affected Features:
- **Process Management**: Handling of external processes started through Lua
- **Process I/O**: Reading from and writing to process streams

### Files Affected:
- `org/luaj/vm2/lib/jse/JseIoLib.java` (lines 100, 101)
- `org/luaj/vm2/lib/jse/JseProcess.java` (lines 78, 85)

### Impact:
- Process operations will fail completely
- No ability to interact with external programs

## 5. java.lang.Class.getClasses() Method

### Affected Features:
- **Java Class Reflection**: Accessing inner classes of Java objects from Lua
- **Java Object Navigation**: Getting nested class information from Java objects

### Files Affected:
- `org/luaj/vm2/lib/jse/JavaClass.java` (line 138)
- `org/luaj/vm2/lib/jse/JavaInstance.java` (line 62)

### Impact:
- Cannot access inner/nested classes from Lua scripts
- Limited Java reflection capabilities

## Summary of Affected Luaj Features

### Completely Broken Features:
1. **Process Execution**: `io.popen()`, `os.execute()` - External program execution
2. **Dynamic Proxies**: Creating proxy objects through luajava library
3. **Program Termination**: `os.exit()`, normal program exit

### Partially Broken Features:
1. **Java Reflection**: Limited access to inner classes and some reflection capabilities
2. **Luajava Library**: Core functionality works but with missing features like proxy creation

### Unaffected Features:
1. **Core Lua VM**: Parser, interpreter, and standard Lua data types
2. **Basic Libraries**: Most of the standard Lua libraries (table, string, math, etc.)
3. **Lua Script Execution**: Basic script execution without external dependencies

## Recommendations

1. **Create TeaVM-specific implementations**:
   - Replace `System.exit()` calls with JavaScript-compatible alternatives
   - Provide stub implementations for process-related functions
   - Create JavaScript-based proxy mechanisms for common use cases

2. **Feature Detection**:
   - Add runtime checks to gracefully handle missing features
   - Provide informative error messages when unsupported features are used

3. **Documentation**:
   - Clearly document which features work and which don't in TeaVM environments
   - Provide examples of TeaVM-compatible Lua scripts

4. **Alternative Approaches**:
   - Consider using TeaVM's JavaScript interoperability for external program interaction
   - Implement web-based alternatives for process execution (e.g., calling REST APIs instead of executing programs)