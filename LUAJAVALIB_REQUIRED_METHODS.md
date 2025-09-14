# Required Methods for LuajavaLib in TeaVM

## Actually Required Methods (Based on Execution Paths)

### 1. java.lang.Class.getClasses()[Ljava/lang/Class;
- **Location**: `org.luaj.vm2.lib.jse.JavaClass.getInnerClass()` line 138
- **Usage**: Called when accessing inner classes through JavaClass
- **Reason for Failure**: This method is not implemented in TeaVM's reflection API
- **Impact**: Prevents access to inner/nested classes from Lua scripts

### 2. java.lang.ClassLoader.getSystemClassLoader()
- **Location**: `org.luaj.vm2.lib.jse.os.LuajavaLib.classForName()` method
- **Usage**: Called when binding classes with `luajava.bindClass()`
- **Reason for Failure**: Not available in browser environment for security reasons
- **Impact**: Prevents dynamic class loading from system classpath

### 3. java.lang.Class.forName(String, boolean, ClassLoader)
- **Location**: `org.luaj.vm2.lib.jse.os.LuajavaLib.classForName()` method
- **Usage**: Called when binding classes with `luajava.bindClass()`
- **Reason for Failure**: TeaVM has limited Class.forName() support that doesn't work with custom classloaders
- **Impact**: Prevents binding of classes through the luajava API

## Methods Not Actually Required (Commented Out)

The following methods are present in the source code but are commented out, so they are not currently required:

1. java.lang.reflect.Proxy.newProxyInstance() - CREATEPROXY functionality is disabled
2. java.lang.reflect.Method.getModifiers() - Used in commented out proxy code
3. java.lang.reflect.Array.getLength() and related methods - Used in commented out proxy code

## Summary

Only 3 methods are actually required for the currently active LuajavaLib functionality, and all 3 are missing from TeaVM's implementation. These missing methods prevent the core functionality of binding Java classes to Lua scripts from working in TeaVM environments.

The commented out proxy functionality would require additional methods, but since it's disabled, these are not currently blocking the basic operation of LuajavaLib.