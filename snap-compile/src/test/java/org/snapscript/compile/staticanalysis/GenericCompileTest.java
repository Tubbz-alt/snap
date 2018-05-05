package org.snapscript.compile.staticanalysis;

import org.snapscript.compile.ClassPathCompilerBuilder;
import org.snapscript.compile.Compiler;
import org.snapscript.compile.verify.VerifyException;

import junit.framework.TestCase;

public class GenericCompileTest extends TestCase {
   
   private static final String SOURCE_1 =
   "class Foo{\n"+
   "   foo(){\n"+
   "   }\n"+
   "}\n"+
   "class Blah extends Foo{\n"+
   "   blah(){\n"+
   "   }\n"+
   "}\n"+
   "class Bar<T: Foo>{\n"+
   "   func(): T{\n"+
   "      return new Blah();\n"+
   "\n"+
   "   }\n"+
   "}\n"+
   "var x: Bar<Blah> = new Bar<Blah>();\n"+
   "x.func().blah();\n";
   
   private static final String SOURCE_2 =
   "class Foo<T>{\n"+
   "}\n"+
   "class Blah extends Foo<String, Integer>{\n"+
   "}\n";
   
   private static final String SOURCE_3 =
   "class Boo{}\n"+
   "class Foo<T: Boo>{\n"+
   "   blah():T{\n"+
   "      return new Boo();\n"+
   "   }\n"+
   "}\n"+
   "class Blah<T:String> extends Foo<T>{\n"+
   "   func():T{\n"+
   "      return 11;\n"+
   "   }\n"+
   "}\n"+
   "println(Foo$T.class);\n"+
   "println(Blah$T.class);\n";


   public void testGenericCompile() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      System.err.println(SOURCE_1);
      try {
         compiler.compile(SOURCE_1).execute();
      }catch(VerifyException e){
         e.getErrors().get(0).getCause().printStackTrace();
      }
   }
   
   public void testGenericCompileBoundsError() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      boolean failure = false;
      try{
         System.err.println(SOURCE_2);
         compiler.compile(SOURCE_2).execute();
      }catch(Exception e){
         failure = true;
         e.printStackTrace();
      }
      assertTrue(failure);
   }
   
   public void testGenericCompileBoundsMismatchError() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      boolean failure = false;
      try{
         System.err.println(SOURCE_3);
         compiler.compile(SOURCE_3).execute();
      }catch(Exception e){
         failure = true;
         e.printStackTrace();
      }
      assertTrue(failure);
   }
}
