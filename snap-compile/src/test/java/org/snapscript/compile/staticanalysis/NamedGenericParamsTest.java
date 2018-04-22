package org.snapscript.compile.staticanalysis;

import static org.snapscript.core.Reserved.DEFAULT_PACKAGE;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.snapscript.common.store.ClassPathStore;
import org.snapscript.common.store.Store;
import org.snapscript.compile.Compiler;
import org.snapscript.compile.StoreContext;
import org.snapscript.compile.StringCompiler;
import org.snapscript.core.Context;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.scope.EmptyModel;
import org.snapscript.core.scope.Model;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Type;
import org.snapscript.core.type.TypeLoader;

public class NamedGenericParamsTest extends TestCase {
   
   private static final String SOURCE = 
   "class SomeType<A: String, B, C: Integer> {\n"+
   "   getReturnA(): A{\n"+
   "      return null;\n"+
   "   }\n"+
   "   getReturnB(): B{\n"+
   "      return null;\n"+
   "   }\n"+
   "   getReturnMap(): Map<A, String>{\n"+
   "      return null;\n"+
   "   }\n"+
   "   doIt(a: A, b: B, c: String): B{\n"+
   "      return null;\n"+
   "   }\n"+
   "}\n";
   
   public static class SomeType<A extends String, B, C extends Integer> {
      public A getReturnA(){
         return null;
      }
      public B getReturnB(){
         return null;
      }
      public Map<A, String> getReturnMap(){
         return null;
      }
      public B doIt(A a, B b, String c){
         return null;
      }
   }
   
   public void testGenericParametersForScript() throws Exception {
      Store store = new ClassPathStore();
      Context context = new StoreContext(store, null);
      Compiler compiler = new StringCompiler(context);
      Model model = new EmptyModel();
      
      System.err.println(SOURCE);
      
      compiler.compile(SOURCE).execute(model, true);
      
      TypeLoader loader = context.getLoader();
      Type type = loader.resolveType(DEFAULT_PACKAGE, "SomeType");      
      Type string = loader.loadType(String.class);
      
      assertNotNull(type);
      assertNotNull(string);      
      
      List<Constraint> constraints = type.getConstraints();
      Scope scope = type.getScope();
      
      assertFalse(constraints.isEmpty());
      assertEquals(constraints.size(), 3);
      assertNotNull(constraints.get(0).getType(scope));
      assertNotNull(constraints.get(1).getType(scope));
      assertEquals(constraints.get(0).getType(scope).getName(), "String");
      assertEquals(constraints.get(1).getType(scope).getName(), "Object");
      assertEquals(constraints.get(2).getType(scope).getName(), "Integer");
      assertEquals(constraints.get(0).getName(scope), "SomeType$A");
      assertEquals(constraints.get(1).getName(scope), "SomeType$B");
      assertEquals(constraints.get(2).getName(scope), "SomeType$C");
      
      Constraint returnA = context.getBinder().bind("getReturnA").match(scope, Constraint.getConstraint(type)).compile(scope, Constraint.getConstraint(type));
      Constraint returnB = context.getBinder().bind("getReturnB").match(scope, Constraint.getConstraint(type)).compile(scope, Constraint.getConstraint(type));
      Constraint returnMap = context.getBinder().bind("getReturnMap").match(scope, Constraint.getConstraint(type)).compile(scope, Constraint.getConstraint(type));
      Constraint doIt = context.getBinder().bind("doIt").match(scope, Constraint.getConstraint(type)).compile(scope, Constraint.getConstraint(type), string, string, string);
      
      assertNotNull(returnA);
      assertNotNull(returnB);
      assertNotNull(returnMap);
      assertNotNull(doIt);
      
      assertEquals(returnA.getType(scope).getName(), "String");
      //assertNull(returnB.getType(scope));
      assertEquals(returnMap.getType(scope).getName(), "Map");
      //assertNull(doIt.getType(scope));      
      
      assertEquals(returnA.getName(scope), "A");
      assertEquals(returnB.getName(scope), "B");
      assertNull(returnMap.getName(scope));
      assertEquals(doIt.getName(scope), "B");  
      
      assertFalse(returnMap.getGenerics(scope).isEmpty());
      
      assertEquals(returnMap.getGenerics(scope).get(0).getName(scope), "A");
      assertNull(returnMap.getGenerics(scope).get(1).getName(scope));
      
      assertNotNull(returnMap.getGenerics(scope).get(0).getType(scope));
      assertNotNull(returnMap.getGenerics(scope).get(1).getType(scope));
      
      assertEquals(returnMap.getGenerics(scope).get(0).getType(scope).getName(), "String");
      assertEquals(returnMap.getGenerics(scope).get(1).getType(scope).getName(), "String");
   }
   
   
   public void testGenericParameters() throws Exception {
      Store store = new ClassPathStore();
      Context context = new StoreContext(store, null);
      TypeLoader loader = context.getLoader();
      Type type = loader.loadType(SomeType.class);
      Type string = loader.loadType(String.class);
      
      assertNotNull(type);
      assertNotNull(string);      
      
      List<Constraint> constraints = type.getConstraints();
      Scope scope = type.getScope();
      
      assertFalse(constraints.isEmpty());
      assertEquals(constraints.size(), 3);
      assertNotNull(constraints.get(0).getType(scope));
      assertNotNull(constraints.get(1).getType(scope));
      assertEquals(constraints.get(0).getType(scope).getName(), "String");
      assertEquals(constraints.get(1).getType(scope).getName(), "Object");
      assertEquals(constraints.get(2).getType(scope).getName(), "Integer");
      assertEquals(constraints.get(0).getName(scope), "A");
      assertEquals(constraints.get(1).getName(scope), "B");
      assertEquals(constraints.get(2).getName(scope), "C");      
      
      Constraint returnA = context.getBinder().bind("getReturnA").match(scope, Constraint.getConstraint(type)).compile(scope, Constraint.getConstraint(type));
      Constraint returnB = context.getBinder().bind("getReturnB").match(scope, Constraint.getConstraint(type)).compile(scope, Constraint.getConstraint(type));
      Constraint returnMap = context.getBinder().bind("getReturnMap").match(scope, Constraint.getConstraint(type)).compile(scope, Constraint.getConstraint(type));
      Constraint doIt = context.getBinder().bind("doIt").match(scope, Constraint.getConstraint(type)).compile(scope, Constraint.getConstraint(type), string, string, string);
      
      assertNotNull(returnA);
      assertNotNull(returnB);
      assertNotNull(returnMap);
      assertNotNull(doIt);
      
      assertEquals(returnA.getType(scope).getName(), "String");
      assertNull(returnB.getType(scope));
      assertEquals(returnMap.getType(scope).getName(), "Map");
      assertNull(doIt.getType(scope));
      
      assertEquals(returnA.getName(scope), "A");
      assertEquals(returnB.getName(scope), "B");
      assertNull(returnMap.getName(scope));
      assertEquals(doIt.getName(scope), "B"); 
   }
}
