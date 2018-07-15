package org.snapscript.compile.link;

import org.snapscript.compile.ScriptTestCase;
import org.snapscript.compile.verify.VerifyException;

public class MissingImportTest extends ScriptTestCase {
   
   private static final String SOURCE_1 =  
   "class Tree {\n"+
   "\n"+
   "   sortOrder(): List<Node>{\n"+
   "      return sortOrder(1);\n"+
   "   }\n"+
   "   sortOrder(a, b): List<Node>{}\n"+
   "}\n";
   
  
   private static final String SOURCE_2 =
   "func main(){\n" +      
   "  var tree = Tree();\n"+
   "  tree.sortOrder();\n"+
   "}\n";

   
   public void testMissingFunction() throws Exception {
      addScript("/com/test/foo/Tree.snap", SOURCE_1);
      addScript("/com/test/foo.snap", SOURCE_2);     

      try {
         assertExpressionEvaluates("/com/test/foo.snap", "main()", "com.test.foo");
      } catch(VerifyException e) { // this is wrong, should not throw error
         String message = e.getErrors().get(0).getDescription();
         System.err.println(message);
         e.printStackTrace();
         assertEquals(message, "No type found for 'Node' in 'com.test.foo' in /com/test/foo/Tree.snap at line 3");   
      } 
   }
}
