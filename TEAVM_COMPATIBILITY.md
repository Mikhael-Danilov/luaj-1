# TeaVM Compatibility Analysis for Luaj

## Overview
This document summarizes the compatibility analysis of the Luaj library with TeaVM, a Java-to-JavaScript transpiler that allows running Java applications in web browsers.

## TeaVM JCL Coverage
Based on the TeaVM JCL coverage report, here are the key findings:

### Well-Supported Packages
- `java.util.function` - 100%
- `java.time.*` packages - 94-100%
- `java.util.regex` - 100%
- `java.math` - 100%
- `java.util` core - 86%
- `java.util.stream` - 100%

### Limited Support Packages
- `java.lang.reflect` - 35% (critical for Luaj's Java integration features)
- `java.net` - 18%
- `java.nio.channels` - 15%
- `java.nio.file` - 15%
- `java.security` - 14%
- `java.util.concurrent` - 21%

## Luaj Codebase Analysis
The Luaj codebase uses several Java standard library features that have limited support in TeaVM:

### Reflection Usage
Multiple files in `src/core/org/luaj/vm2/lib/jse/` use `java.lang.reflect` package extensively:
- `JavaInstance.java` - imports `java.lang.reflect.Field`
- `JavaMethod.java` - imports `java.lang.reflect.Method` and `InvocationTargetException`
- `JavaArray.java` - imports `java.lang.reflect.Array`
- `JavaClass.java` - imports `java.lang.reflect.Constructor`, `Field`, `Method`, and `Modifier`
- `CoerceLuaToJava.java` - imports `java.lang.reflect.Array`
- `LuajavaLib.java` - imports `java.lang.reflect.Array`, `InvocationHandler`, `InvocationTargetException`, `Method`, and `Proxy`
- `JavaConstructor.java` - imports `java.lang.reflect.Constructor` and `InvocationTargetException`

### TeaVM Support for Reflection Classes
- `Array` (19%) - Very limited support
- `Constructor` (63%) - Partial support
- `Field` (44%) - Limited support
- `Method` (61%) - Partial support
- `Modifier` (80%) - Good support
- `InvocationHandler` (50%) - Partial support
- `Proxy` (50%) - Partial support
- `InvocationTargetException` (100%) - Fully supported

## TeaVM Target Configuration
We've created a TeaVM target in the Gradle build system:

1. Added a new `teavm` module in `settings.gradle`
2. Created a `teavm/build.gradle` file with the TeaVM plugin (version 0.13.0-SNAPSHOT from mavenLocal())
3. Configured the JavaScript generation task

## Build Results
When attempting to compile the Luaj library with TeaVM 0.13.0-SNAPSHOT, we encountered several specific compatibility errors that confirm our analysis:

1. **System.exit not supported**: TeaVM does not support `System.exit()` calls, which are used in the `lua.java` main class
2. **Reflection limitations**: Classes like `java.lang.reflect.Proxy` are not available in TeaVM
3. **Process execution not supported**: Methods like `Runtime.exec()` and classes like `Process` are not available in TeaVM
4. **Reflection methods not supported**: Methods like `Class.getClasses()` are not available in TeaVM

These errors confirm that the reflection-heavy components of Luaj will not work properly in TeaVM.

## Recommendations
1. **Create a TeaVM-specific module**: Develop a version of Luaj that excludes or provides alternative implementations for the reflection-heavy components.
2. **Provide JavaScript-compatible alternatives**: For the luajava library functionality, implement JavaScript interoperability features using TeaVM's JSO (JavaScript Object) APIs.
3. **Document limitations**: Clearly document which features work and which don't in a TeaVM environment.
4. **Test with simpler examples**: Start with basic Lua functionality and gradually add more complex features to identify specific compatibility issues.

## Conclusion
While the core Lua VM implementation should work fine in TeaVM (as it primarily uses well-supported Java standard library features), the advanced Java integration features will have significant limitations due to the partial support for reflection in TeaVM. To make Luaj fully compatible with TeaVM, significant modifications would be needed to replace the reflection-based code with TeaVM-compatible alternatives.

The TeaVM 0.13.0-SNAPSHOT version successfully identifies these compatibility issues, confirming our analysis and providing a foundation for future work on TeaVM compatibility.