package org.snapscript.compile;


public class RecursiveImportTest extends ScriptTestCase {
   
   private static final String SOURCE_1 =
   "import test.Bar;\n"+
   "println(new Bar(1,2));\n";
         
   private static final String SOURCE_2 =
   "class Bar{\n"+
   "   var x,y;\n"+
   "   new(x,y){\n"+
   "      this.x=x;\n"+
   "      this.y=y;\n"+
   "   }\n"+
   "   get(){\n"+
   "      \"Bar(${x},${y})\";\n"+
   "   }\n"+
   "}";
 
   private static final String SOURCE_3 =
   "import test2.Bar.Inner;\n"+
   "println(new Bar.Inner(22));\n";
   
   private static final String SOURCE_4 =
   "class Bar{\n"+
   "   var x,y;\n"+
   "   new(x,y){\n"+
   "      this.x=x;\n"+
   "      this.y=y;\n"+
   "   }\n"+
   "   get(){\n"+
   "      \"Bar(${x},${y})\";\n"+
   "   }\n"+
   "   class Inner{\n"+
   "     var f;\n"+
   "     new(f){\n"+
   "        this.f=f;\n"+
   "     }\n"+
   "     override toString(){\n"+
   "        \"Bar.Inner(${f})\";\n"+
   "     }\n"+
   "   }\n"+
   "}";
         
   public void testImports() throws Exception {
      addScript("/test.snap", SOURCE_1);
      addScript("/test/Bar.snap", SOURCE_2);  
      addScript("/test2.snap", SOURCE_3);  
      addScript("/test2/Bar.snap", SOURCE_4);  
      assertScriptExecutes("/test.snap");
      assertScriptExecutes("/test2.snap");      
   }
}
