package org.luaj.vm2.teavm;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.lib.jse.JsePlatform;

public class LuajavaLibTest {
    public static void main(String[] args) {
        System.out.println("Testing LuajavaLib compatibility with TeaVM");
        
        try {

            // Create a globals environment and load LuajavaLib
            Globals globals = JsePlatform.debugGlobals();
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