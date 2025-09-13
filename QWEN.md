# Luaj Project Context

## Project Overview

This is a fork of Luaj specifically targeted to be compatible with TeaVM, a Java-to-JavaScript transpiler. Luaj is a Java-based implementation of the Lua programming language, specifically targeting Lua 5.2.x. It provides a lightweight, high-performance execution environment for Lua scripts that can run on Java SE (Java Standard Edition) and JEE (Java Enterprise Edition) platforms.

Key features of Luaj:
- Java-centric implementation of the Lua VM
- Multi-platform support (JSE, JEE)
- Complete set of libraries and tools for integration
- Good performance, often faster than C-based Lua in some benchmarks
- Support for both interpreted execution and compilation to Java bytecode
- Thread safety with proper isolation between threads
- TeaVM compatibility for running in web browsers

## Project Structure

```
├── build.gradle              # Gradle build file for main module
├── settings.gradle           # Gradle settings file
├── gradle.properties         # Gradle properties file
├── examples/                 # Example code for various use cases
│   ├── jse/                  # Java SE examples
│   └── lua/                  # Lua script examples
├── src/                      # Source code for main module
│   └── core/                 # Core Lua implementation
├── luajc/                    # Separate module for luajc compiler
│   └── src/                  # Source code for luajc module
│       └── main/
│           └── java/         # Java source files for luajc
├── test/                     # Unit tests
├── lib/                      # Dependencies (downloaded during build)
├── docs/                     # Documentation
└── grammar/                  # JavaCC grammar files for Lua parser
```

## TeaVM Compatibility

This fork of Luaj has been modified to be compatible with TeaVM, which allows the Lua interpreter to run in web browsers by transpiling the Java code to JavaScript. Key modifications for TeaVM compatibility include:

- Removal of J2ME code to simplify the codebase
- Removal of Ant build system in favor of Gradle
- Updates to ensure compatibility with TeaVM's JavaScript generation
- Focus on JSE-only implementation for better transpilation results
- Moved luajc compiler to a separate module since it won't work in TeaVM environment

## Building and Running

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Gradle build tool

### Building the Project with Gradle

The project now uses Gradle for building:

```bash
# Build the entire project (main module + luajc module)
./gradlew build

# Build the project without running tests
./gradlew assemble

# Build just the main module (without luajc)
./gradlew :assemble

# Build just the luajc module
./gradlew :luajc:assemble

# Run the hello.lua example
./gradlew runLua
```

### Running Examples

To run the basic "Hello World" example:
```bash
java -cp build/libs/luaj-3.0.2.jar lua examples/lua/hello.lua
```

To run a Swing application example:
```bash
java -cp build/libs/luaj-3.0.2.jar lua examples/lua/swingapp.lua
```

To run a Java application that uses Luaj:
```bash
javac -cp build/libs/luaj-3.0.2.jar examples/jse/SampleJseMain.java
java -cp "build/libs/luaj-3.0.2.jar:examples/jse" SampleJseMain
```

## Using Luaj in Applications

### Basic Java Integration

```java
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;

public class SampleJseMain {
    public static void main(String[] args) throws Exception {
        // Create an environment to run in
        Globals globals = JsePlatform.standardGlobals();
        
        // Load and execute a script
        LuaValue chunk = globals.loadfile("script.lua");
        chunk.call();
    }
}
```

### Creating Custom Libraries

Custom libraries can be created by extending the appropriate function classes:

```java
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class hyperbolic extends TwoArgFunction {
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("sinh", new sinh());
        library.set("cosh", new cosh());
        env.set("hyperbolic", library);
        return library;
    }

    static class sinh extends OneArgFunction {
        public LuaValue call(LuaValue x) {
            return LuaValue.valueOf(Math.sinh(x.checkdouble()));
        }
    }
    
    static class cosh extends OneArgFunction {
        public LuaValue call(LuaValue x) {
            return LuaValue.valueOf(Math.cosh(x.checkdouble()));
        }
    }
}
```

### Maven Integration

For Maven-based projects, add the following dependency:

```xml
<dependency>
    <groupId>org.luaj</groupId>
    <artifactId>luaj</artifactId>
    <version>3.0.2</version>
</dependency>
```

If you need the luajc compiler functionality, you'll also need:

```xml
<dependency>
    <groupId>org.luaj</groupId>
    <artifactId>luajc</artifactId>
    <version>3.0.2</version>
</dependency>
```

## Key APIs and Concepts

### Globals
The `Globals` class holds the global state needed for executing Lua scripts and provides convenience functions for compiling and loading scripts.

### Platform Support
- `JsePlatform`: For Java SE applications, includes all standard libraries plus luajava

### Thread Safety
Luaj 3.0 supports multi-threading with these requirements:
1. Each thread must have its own `Globals` instance
2. Threads must not access `Globals` from other threads
3. Shared metatables should not be mutated once Lua code is running

### Script Engine Support
Luaj implements the JSR-223 scripting API, allowing it to be used as a standard scripting engine:

```java
ScriptEngineManager mgr = new ScriptEngineManager();
ScriptEngine e = mgr.getEngineByName("luaj");
e.put("x", 25);
e.eval("y = math.sqrt(x)");
System.out.println("y=" + e.get("y"));
```

## Development Guidelines

### Code Organization
- Core Lua functionality in `src/core/`
- Java SE extensions in `luajc/src/main/java/`

### Testing
Unit tests are organized using JUnit 3 and can be run with:
```bash
./gradlew test
```

### Documentation
API documentation is generated using Javadoc and can be built with:
```bash
./gradlew javadoc
```

## Version Information
Current version: 3.0.2

This version supports Lua 5.2.x features including:
- _ENV environments model
- yield from pcall or metatags
- Bitwise operator library

## Recent Changes
- Removed J2ME (Java Micro Edition) support to simplify the codebase and focus on modern Java platforms
- Removed Ant build system in favor of Gradle for modern Java development workflows
- Fixed `yield` keyword conflict in CoroutineLib.java by renaming the class to `yieldFunction`
- Updated Java source and target compatibility to version 8
- Modified codebase for TeaVM compatibility
- Moved luajc compiler to a separate module since it won't work in TeaVM environment