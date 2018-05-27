package org.snapscript.compile;

import org.snapscript.compile.verify.VerifyException;

public class ClassHierarchyExceptionTest extends ScriptTestCase {
   
   private static final String SOURCE = 
   "class Foo extends NoSuchClass {\n"+
   "   new(x): super(x){\n"+
   "   }\n"+
   "   override noSuchMethod(){\n"+
   "      return true;\n"+
   "   }\n"+
   "}\n"+
   "new Foo(11).noSuchMethod();\n";       
   
   public void testException() throws Exception {
      System.err.println(SOURCE);
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE);
      try{
         executable.execute();
      }catch(VerifyException e) {
         e.printStackTrace();
         assertEquals(e.getErrors().get(0).toString(), "Import not found in /default.snap at line 1");
      }
   }

}
