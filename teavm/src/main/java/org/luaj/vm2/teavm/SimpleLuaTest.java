package org.luaj.vm2.teavm;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

public class SimpleLuaTest {
    public static void main(String[] args) {
        // Create a minimal environment with only the most basic libraries
        Globals globals = new Globals();
        globals.load(new BaseLib());
        globals.load(new PackageLib());
        
        // Set up the global environment
        LoadState.install(globals);
        
        // Simple Lua code that doesn't use incompatible features
        String script = "print('Hello from Lua in TeaVM!')";
        
        // Load and execute the script
        LuaValue chunk = globals.load(script, "test");
        LuaValue result = chunk.call();
        
        System.out.println("Lua interpreter working in TeaVM");
    }
}