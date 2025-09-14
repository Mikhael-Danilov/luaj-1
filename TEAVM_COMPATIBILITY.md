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

## Compatibility Issues
1. **Reflection-heavy components**: The core Java integration features of Luaj rely heavily on reflection, which has only partial support in TeaVM.
2. **Luajava library**: This key feature for interacting with Java objects from Lua scripts will have significant limitations.
3. **Dynamic class loading**: Features that dynamically load or inspect Java classes at runtime will not work in TeaVM.

## TeaVM Target Configuration
We've created a TeaVM target in the Gradle build system:

1. Added a new `teavm` module in `settings.gradle`
2. Created a `teavm/build.gradle` file with the TeaVM plugin (version 0.12.3)
3. Configured the JavaScript generation task

## Build Results
When attempting to compile the Luaj library with TeaVM, we encountered a NullPointerException in the TeaVM compiler itself. This is likely due to the incompatibility between the reflection-heavy Luaj codebase and the limited reflection support in TeaVM.

## Recommendations
1. **Create a TeaVM-specific module**: Develop a version of Luaj that excludes or provides alternative implementations for the reflection-heavy components.
2. **Provide JavaScript-compatible alternatives**: For the luajava library functionality, implement JavaScript interoperability features using TeaVM's JSO (JavaScript Object) APIs.
3. **Document limitations**: Clearly document which features work and which don't in a TeaVM environment.
4. **Test with simpler examples**: Start with basic Lua functionality and gradually add more complex features to identify specific compatibility issues.

## Conclusion
While the core Lua VM implementation should work fine in TeaVM (as it primarily uses well-supported Java standard library features), the advanced Java integration features will have significant limitations due to the partial support for reflection in TeaVM. To make Luaj fully compatible with TeaVM, significant modifications would be needed to replace the reflection-based code with TeaVM-compatible alternatives.