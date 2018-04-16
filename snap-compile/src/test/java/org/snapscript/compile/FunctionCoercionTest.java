package org.snapscript.compile;

import junit.framework.TestCase;

public class FunctionCoercionTest extends TestCase {

   private static final String SOURCE =
   "function func(x: []){\n"+
   "   println(x);\n"+
   "}\n"+
   "\n"+
   "function func(x: {}){\n"+
   "   println(x);\n"+
   "}\n"+
   "\n"+
   "function func(x: {:}){\n"+
   "   println(x);\n"+
   "}\n"+
   "\n"+
   "func((a,b)->println(`${a},${b}`));\n"+
   "func((a:String,b:String)->println(`a=${a} b=${b}`));\n"+
   "\n"+
   "function func(x: (a,b)) {\n"+
   "   x(11,22);\n"+
   "}\n"+
   "\n"+
   "println([`a`, `b`, `c`]);\n"+
   "\n"+
   "function func(x: Byte[]){\n"+
   "   println(x);\n"+
   "}\n"+
   "\n"+
   "function func(x: Byte[][]){\n"+
   "   println(x);\n"+
   "}\n"+
   "\n"+
   "const b = new Byte[10];\n"+
   "const bb = new Byte[10][10];\n"+
   "\n"+
   "func(b);\n"+
   "func(bb);\n";
   
   public void testFunctionCoercion() throws Exception {;
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE);
      executable.execute();
   
   }           
}
