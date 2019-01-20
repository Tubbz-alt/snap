package org.snapscript.compile;

import static org.snapscript.core.Reserved.GRAMMAR_SCRIPT;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

import com.sun.management.ThreadMXBean;
import junit.framework.TestCase;
import org.snapscript.core.Reserved;
import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxNode;

public class ClosureTest extends TestCase {

   private static final String SOURCE_1=
   "var x = (a,b)->{ return a+b;};\n"+
   "var r = x(1,33);\n"+
   "println(r);";
   
   private static final String SOURCE_2=
   "max((a,b)->{return a+b;});\n"+
   "\n"+
   "function max(f){\n"+
   "   var x = f('xx', 'bb');\n"+
   "   println(x);\n"+
   "}\n";
   
   private static final String SOURCE_3=
   "max((a,b)->a+b);\n"+
   "\n"+
   "function max(f){\n"+
   "   var x = f('xx', 'bb');\n"+
   "   println(x);\n"+
   "}\n";

   private static final String SOURCE_4=
   "var expression = x->x>0;\n"+
   "var f = -> {\n"+
   "   expression(10);\n"+
   "};\n";
   
   private static final String SOURCE_5=
   "let x = (a) -> a.run();\n"+
   "println(x);\n";

   private static final String SOURCE_6=
   "let x = async (a) -> 'x';\n"+
   "println(x.class);\n"+
   "println(x(1).class);\n"+
   "assert x(1) instanceof Promise;\n";

   private static final String SOURCE_7=
   "let x = async (a) -> {" +
   "   return await System.currentTimeMillis();\n"+
   "};\n" +
   "println(x.class);\n"+
   "x(1).thenAccept(y -> println(y)).join();\n";

   public void testClosure() throws Exception {
      DecimalFormat format = new DecimalFormat("###,###,###,###,###");
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE_1);
      System.err.println(SOURCE_1);
      ThreadMXBean bean = (ThreadMXBean) ManagementFactory.getThreadMXBean();
      Thread thread = Thread.currentThread();
      long id = thread.getId();
      System.gc();
      System.gc();
      Thread.sleep(100);
      long before = bean.getThreadAllocatedBytes(id);
      long start = System.currentTimeMillis();
      executable.execute();
      long finish = System.currentTimeMillis();
      long after = bean.getThreadAllocatedBytes(id);
      System.out.println();
      System.out.println("time=" + (finish - start) + " memory=" + format.format(after - before));
   }
   
   public void testClosureAsParameter() throws Exception {
      DecimalFormat format = new DecimalFormat("###,###,###,###,###");
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE_2);
      System.err.println(SOURCE_2);
      ThreadMXBean bean = (ThreadMXBean) ManagementFactory.getThreadMXBean();
      Thread thread = Thread.currentThread();
      long id = thread.getId();
      System.gc();
      System.gc();
      Thread.sleep(100);
      long before = bean.getThreadAllocatedBytes(id);
      long start = System.currentTimeMillis();
      executable.execute();
      long finish = System.currentTimeMillis();
      long after = bean.getThreadAllocatedBytes(id);
      System.out.println();
      System.out.println("time=" + (finish - start) + " memory=" + format.format(after - before));
   }
   
   public void testClosureEvaluationAsParameter() throws Exception {
      DecimalFormat format = new DecimalFormat("###,###,###,###,###");
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE_3);
      System.err.println(SOURCE_3);
      ThreadMXBean bean = (ThreadMXBean) ManagementFactory.getThreadMXBean();
      Thread thread = Thread.currentThread();
      long id = thread.getId();
      System.gc();
      System.gc();
      Thread.sleep(100);
      long before = bean.getThreadAllocatedBytes(id);
      long start = System.currentTimeMillis();
      executable.execute();
      long finish = System.currentTimeMillis();
      long after = bean.getThreadAllocatedBytes(id);
      System.out.println();
      System.out.println("time=" + (finish - start) + " memory=" + format.format(after - before));
   }
   
   public void testClosureInClosure() throws Exception {
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE_4);
      System.err.println(SOURCE_4);
      executable.execute();
   }
   
   public void testClosureParameters() throws Exception {
      SyntaxNode node = new SyntaxCompiler(Reserved.GRAMMAR_FILE).compile().parse("/path.snap", SOURCE_5, GRAMMAR_SCRIPT);
      System.out.println(SyntaxPrinter.print(node));
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE_5);
      System.err.println(SOURCE_5);
      executable.execute();
   }

   public void testAsyncClosure() throws Exception {
      SyntaxNode node = new SyntaxCompiler(Reserved.GRAMMAR_FILE).compile().parse("/path.snap", SOURCE_6, GRAMMAR_SCRIPT);
      System.out.println(SyntaxPrinter.print(node));
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE_6);
      System.err.println(SOURCE_6);
      executable.execute();
   }

   public void testAsyncWithBlockClosure() throws Exception {
      SyntaxNode node = new SyntaxCompiler(Reserved.GRAMMAR_FILE).compile().parse("/path.snap", SOURCE_7, GRAMMAR_SCRIPT);
      System.out.println(SyntaxPrinter.print(node));
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();
      Executable executable = compiler.compile(SOURCE_7);
      System.err.println(SOURCE_7);
      executable.execute();
   }
}