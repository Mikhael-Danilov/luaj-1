# TeaVM Compatibility Analysis: LuajavaLib Missing Classes and Methods

## Executive Summary
When attempting to use LuajavaLib in a TeaVM environment, several Java classes and methods that are not available in TeaVM are encountered. These missing features prevent LuajavaLib from functioning correctly in browser environments.

## Missing Methods and Classes

### 1. java.lang.Class.getClasses()[Ljava/lang/Class;
- **Location**: `org.luaj.vm2.lib.jse.JavaClass.getInnerClass()` line 138
- **Purpose**: Returns an array of Class objects reflecting all the classes and interfaces declared as members of the class represented by this Class object.
- **Impact**: Prevents access to inner classes and interfaces declared within a class.
- **TeaVM Status**: Not implemented in TeaVM's limited reflection support.

### 2. java.lang.ClassLoader.getSystemClassLoader()
- **Location**: `org.luaj.vm2.lib.jse.os.LuajavaLib.classForName()` method
- **Purpose**: Returns the system class loader for delegation.
- **Impact**: Prevents dynamic class loading from the system classpath.
- **TeaVM Status**: Not available due to browser security restrictions.

### 3. java.lang.Class.forName(String, boolean, ClassLoader)
- **Location**: `org.luaj.vm2.lib.jse.os.LuajavaLib.classForName()` method
- **Purpose**: Returns the Class object associated with the class or interface with the given string name, using the given class loader.
- **Impact**: Prevents dynamic class loading with custom class loaders.
- **TeaVM Status**: Limited implementation available, but not with custom class loaders.

### 4. java.lang.reflect.Proxy.newProxyInstance()
- **Location**: Commented out in `org.luaj.vm2.lib.jse.os.LuajavaLib` (CREATEPROXY case)
- **Purpose**: Returns an instance of a proxy class for the specified interfaces that dispatches method invocations to the specified invocation handler.
- **Impact**: Would prevent creation of dynamic proxy objects.
- **TeaVM Status**: Not implemented due to complexity of dynamic proxy generation.

### 5. java.lang.reflect.Method.getModifiers()
- **Location**: Used in proxy invocation handler code
- **Purpose**: Returns the Java language modifiers for the method represented by this Method object.
- **Impact**: Would affect detection of varargs methods in proxy handling.
- **TeaVM Status**: Limited modifier support.

## Additional Reflection Limitations

### Missing Array Operations
- **java.lang.reflect.Array** methods for creating and manipulating arrays dynamically
- **Impact**: Would affect array handling in Java-to-Lua coercion

### Constructor and Method Discovery
- Limited ability to discover constructors and methods through reflection
- **Impact**: Would affect dynamic method invocation capabilities

## Security and Browser Restrictions

### File System Access
- No access to file system for loading classes from files
- **Impact**: Class loading must be from compiled-in classes only

### Network Restrictions
- Browser CORS policies prevent loading classes from remote URLs
- **Impact**: No dynamic downloading of classes

## Workarounds and Alternatives

### 1. Pre-compiled Class Whitelist
- Create a whitelist of allowed classes that are compiled into the TeaVM application
- Use a custom class loading mechanism that only allows access to pre-approved classes

### 2. Limited Reflection API
- Implement a simplified reflection API that only supports the most essential operations
- Remove support for inner class discovery and dynamic proxy creation

### 3. Static Binding Approach
- Replace dynamic class loading with static binding of commonly used classes
- Pre-register frequently used Java classes with the Lua environment

## Recommendations

1. **Document Limitations**: Clearly document that full LuajavaLib functionality is not available in TeaVM environments
2. **Provide Subset**: Create a TeaVM-compatible subset of LuajavaLib functionality
3. **Alternative APIs**: Consider alternative approaches for Java integration in browser environments
4. **Security Considerations**: The limitations actually provide security benefits in browser environments

## Conclusion
The missing classes and methods in TeaVM are primarily related to advanced reflection capabilities and dynamic class loading features that are intentionally restricted in browser environments for security reasons. While this limits the full functionality of LuajavaLib, it also prevents potential security vulnerabilities that could arise from unrestricted access to the Java runtime environment.