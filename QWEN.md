# Luaj Project Context

## Project Overview

Luaj is a Java-based implementation of the Lua programming language, specifically targeting Lua 5.2.x. It provides a lightweight, high-performance execution environment for Lua scripts that can run on various Java platforms including JSE (Java Standard Edition) and JEE (Java Enterprise Edition).

Key features of Luaj:
- Java-centric implementation of the Lua VM
- Multi-platform support (JSE, JEE)
- Complete set of libraries and tools for integration
- Good performance, often faster than C-based Lua in some benchmarks
- Support for both interpreted execution and compilation to Java bytecode
- Thread safety with proper isolation between threads

## Project Structure

```
├── build.xml                 # Main Ant build file
├── build-libs.xml            # Dependency management for build tools
├── build.gradle              # Gradle build file (newly added)
├── settings.gradle           # Gradle settings file (newly added)
├── gradle.properties         # Gradle properties file (newly added)
├── examples/                 # Example code for various use cases
│   ├── jse/                  # Java SE examples
│   ├── lua/                  # Lua script examples
│   └── maven/                # Maven integration example
├── src/                      # Source code
│   ├── core/                 # Core Lua implementation
│   └── jse/                  # Java SE specific extensions
├── test/                     # Unit tests
├── lib/                      # Dependencies (downloaded during build)
├── docs/                     # Documentation
└── grammar/                  # JavaCC grammar files for Lua parser
```

## Building and Running

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Apache Ant build tool (for Ant build)
- Gradle build tool (for Gradle build)

### Building the Project with Ant

The project uses Apache Ant for building:

```bash
# Build JSE version (JME support has been removed due to compatibility issues)
ant jar-jse

# Build JSE version with sources
ant jar-jse-sources

# Clean build artifacts
ant clean
```

### Building the Project with Gradle

The project now also supports Gradle for building:

```bash
# Build the project
./gradlew build

# Build the project without running tests
./gradlew assemble

# Run the hello.lua example
./gradlew runLua
```

### Running Examples

To run the basic "Hello World" example:
```bash
java -cp luaj-jse-3.0.2.jar lua examples/lua/hello.lua
```

To run a Swing application example:
```bash
java -cp luaj-jse-3.0.2.jar lua examples/lua/swingapp.lua
```

To run a Java application that uses Luaj:
```bash
javac -cp luaj-jse-3.0.2.jar examples/jse/SampleJseMain.java
java -cp "luaj-jse-3.0.2.jar;examples/jse" SampleJseMain
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
    <artifactId>luaj-jse</artifactId>
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
- Java SE extensions in `src/jse/`

### Testing
Unit tests are organized using JUnit 3 and can be run with:
```bash
ant test
```

### Documentation
API documentation is generated using Javadoc and can be built with:
```bash
ant doc
```

## Version Information
Current version: 3.0.2

This version supports Lua 5.2.x features including:
- _ENV environments model
- yield from pcall or metatags
- Bitwise operator library

## Known Limitations
- Debug code may not be completely removed by some obfuscators
- Tail calls are not tracked in debug information
- Values associated with weak keys may linger longer than expected
- Shared metatables are shared across `Globals` instances in the same class loader

## Recent Changes
- Removed JME (Java Micro Edition) support due to compatibility issues with modern Java versions
- Fixed `yield` keyword conflict in CoroutineLib.java by renaming the class to `yieldFunction`
- Added Gradle build support for modern Java development workflows
- Updated Java source and target compatibility to version 8