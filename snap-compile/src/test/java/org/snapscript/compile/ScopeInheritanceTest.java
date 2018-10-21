package org.snapscript.compile;

public class ScopeInheritanceTest extends ScriptTestCase {

   private static final String SOURCE_1 =
    "for(i in 0..2){\n"+
    "   fun(i);\n"+
    "}\n"+
    "\n"+
    "func fun(i){\n"+
    "   println(i);\n"+
    "}\n";

   private static final String SOURCE_2 =
    "for(i in 0..2){\n"+
    "   fun(`value-${i}`);\n"+
    "}\n"+
    "\n"+
    "func fun(i){\n"+
    "   assert `${i.class}` == 'lang.String';\n"+
    "   assert i.startsWith('value-');\n" +
    "   println(`${i.class}`);\n"+
    "   println(i.class);\n"+
    "   println(i.substring(1));\n"+
    "}\n";

   public void testInheritance() throws Exception {
      assertScriptExecutes(SOURCE_1);
      assertScriptExecutes(SOURCE_2);
   }

}