package org.snapscript.compile.staticanalysis;

import org.snapscript.compile.ClassPathCompilerBuilder;
import org.snapscript.compile.Compiler;

import junit.framework.TestCase;

public class ArrayFunctionBindTest extends TestCase {

   private static final String SOURCE =
   "module Functions{\n"+
   "   func(x: String[]){\n"+
   "      return 'String[]';\n"+
   "   }\n"+  
   "   func(x: String[][]){\n"+
   "      return 'String[][]';\n"+
   "   }\n"+  
   "   func(x: Float[]){\n"+
   "      return 'Float[]';\n"+
   "   }\n"+ 
   "   func(x: Float[][]){\n"+
   "      return 'Float[][]';\n"+
   "   }\n"+
   "}\n"+
   "var int: Integer[] =[2];\n"+
   "assert Functions.func(int) == 'Float[]';\n";
         
   public void testArrayBindingTest() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      System.err.println(SOURCE);
      compiler.compile(SOURCE).execute();
   }   
}
