package org.luaj.vm2.teavm;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.BaseLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.jse.os.LuajavaLib;
import org.luaj.vm2.LoadState;

public class LuajavaLibTest {
    public static void main(String[] args) {
        System.out.println("Testing LuajavaLib compatibility with TeaVM");
        
        try {
            // Try to instantiate LuajavaLib
            LuajavaLib lib = new LuajavaLib();
            System.out.println("LuajavaLib instantiated successfully");
            
            // Create a globals environment and load LuajavaLib
            Globals globals = new Globals();
            globals.load(new BaseLib());
            globals.load(new PackageLib());
            globals.load(lib);
            LoadState.install(globals);
            
            // Try to use luajava.bindClass from Lua
            String script = "print('Testing luajava.bindClass')\n" +
                           "local System = luajava.bindClass('java.lang.System')\n" +
                           "print('System class bound successfully')\n" +
                           "return System";
            
            LuaValue chunk = globals.load(script, "test");
            LuaValue result = chunk.call();
            
            System.out.println("LuajavaLib test completed successfully");
        } catch (Throwable e) {
            System.out.println("Failed to use LuajavaLib: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}