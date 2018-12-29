package org.snapscript.compile.staticanalysis;

import org.snapscript.compile.ScriptTestCase;
import org.snapscript.core.Context;

public class NoSuperConstructorTest extends ScriptTestCase {
   
   private static final String SOURCE_1 =
   "import util.regex.Pattern;\n"+
   "class Foo {\n"+
   "   var x,y;\n"+
   "   new(x,y){\n"+
   "      this.x=x;\n"+
   "      this.y=y;\n"+
   "   }\n"+
   "   toString(){\n"+
   "      \"Foo(${x},${y})\";\n"+
   "   }\n"+
   "}\n";
   
   private static final String SOURCE_2 =
   "class Bar extends Foo{\n"+
   "   var text;\n"+
   "   new(text){\n"+
   "      this.text=text;\n"+
   "   }\n"+
   "   toString(){\n"+
   "      \"Bar(${text})\";\n"+
   "   }\n"+
   "}\n";
         
   private static final String SOURCE_3 =
   "class Demo extends Bar{\n"+
   "   var foo;\n"+
   "   new(x,y): super(x){\n"+
   "      this.foo = new Foo(x,y);\n"+
   "   }\n"+
   "   get(): Foo{\n"+
   "      return foo;\n"+
   "   }\n"+
   "}";

   private static final String SOURCE_4 =
   "func main(){ \n" +      
   "  var demo = new Demo(\"x\",33);\n"+
   "  var bar = new Bar(\"test\");\n"+
   "  var foo = demo.get();\n"+
   "  \n"+
   "  println(bar);\n"+
   "  println(foo);\n"+ 
   "}";
   
   public void testMissingSuperConstructor() throws Exception {
      addScript("/com/test/foo/Foo.snap", SOURCE_1);
      addScript("/com/test/foo/Bar.snap", SOURCE_2);      
      addScript("/com/test/foo/Demo.snap", SOURCE_3);  
      addScript("/com/test/foo.snap", SOURCE_4);  
      assertExpressionEvaluates("/com/test/foo.snap", "main()", "com.test.foo", new AssertionCallback() {
         public void onSuccess(Context context, Object result) throws Exception{
            throw new IllegalStateException("Missing constructor");
         }
         public void onException(Context context, Exception cause) throws Exception{
            String message = cause.getMessage();
            cause.printStackTrace();
            assertEquals(message, "Constructor 'new()' not found for 'com.test.foo.Foo'");
         }
      });
   }
}
