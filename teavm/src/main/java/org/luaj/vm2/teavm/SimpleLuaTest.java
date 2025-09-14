package org.luaj.vm2.teavm;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.BaseLib;
import org.luaj.vm2.lib.MathLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.LoadState;

public class SimpleLuaTest {
    public static void main(String[] args) {
        Globals globals = new Globals();
        globals.load(new BaseLib());
        globals.load(new PackageLib());
        globals.load(new Bit32Lib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new MathLib());
        LoadState.install(globals);
        
        LuaValue chunk = globals.load("return 10 + 20", "test");
        LuaValue result = chunk.call();
        System.out.println("Result: " + result.toint());
    }
}