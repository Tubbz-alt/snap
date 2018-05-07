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

public class GenericTransformerTest extends TestCase {   
   
   private static final String SOURCE_1 =
   "class One<A>{\n"+
   "   func():A{\n"+
   "   }\n"+
   "}\n"+
   "class Two<A, B> extends One<B>{\n"+
   "   blah():A{\n"+
   "   }\n"+
   "}\n"+
   "class Three extends Two<String, Integer>{\n"+
   "}\n"+
   "class Four<A: Boolean> extends Two<String, A>{\n"+
   "}\n";   
       
   
   private static final String SOURCE_2 =
   "class One<A>{\n"+
   "   func():A{\n"+
   "   }\n"+
   "   boo():Map<String, A>{\n"+
   "   }\n"+
   "   basic():String{\n"+
   "   }\n"+
   "}\n"+
   "class Two<A, B> extends One<List<B>>{\n"+
   "   blah():A{\n"+
   "   }\n"+
   "}\n"+
   "class Three extends Two<Integer, List<Integer>>{\n"+
   "}\n";
   
   private static final String SOURCE_3 =
   "class Duh<K,V>{\n"+
   "   duh():K{\n"+
   "   }\n"+
   "}\n"+
   "class Blah extends Duh<String,Integer>{\n"+
   "}\n"+
   "class Foo<A,B> extends Blah{\n"+
   "}\n";
   
   public void testRedundantTransformation() throws Exception {
      Store store = new ClassPathStore();
      Context context = new StoreContext(store, null);
      Compiler compiler = new StringCompiler(context);
      Model model = new EmptyModel();
      
      System.err.println(SOURCE_3);
      
      compiler.compile(SOURCE_3).execute(model, true);
      
      TypeLoader loader = context.getLoader();
      Type fromType = loader.resolveType(DEFAULT_PACKAGE, "Foo");      
      Type toType = loader.resolveType(DEFAULT_PACKAGE, "Duh");    
      
      Scope scope = fromType.getScope();
      Constraint constraint = Constraint.getConstraint(fromType);
      Constraint duhResult = context.getResolver().resolveInstance(scope, toType, "duh").check(constraint);
      
      assertNotNull(duhResult);
      assertNotNull(duhResult.getType(scope));
   }
   
   public void testGenericTransformation() throws Exception {
      Store store = new ClassPathStore();
      Context context = new StoreContext(store, null);
      Compiler compiler = new StringCompiler(context);
      Model model = new EmptyModel();
      
      System.err.println(SOURCE_1);
      
      compiler.compile(SOURCE_1).execute(model, true);
      
      TypeLoader loader = context.getLoader();
      Type type3 = loader.resolveType(DEFAULT_PACKAGE, "Three");
      Type type4 = loader.resolveType(DEFAULT_PACKAGE, "Four");        
      Type type1 = loader.resolveType(DEFAULT_PACKAGE, "One");
      Type typeInteger = loader.loadType(Integer.class);
      Type typeBoolean = loader.loadType(Boolean.class);

      Scope scope3 = type3.getScope();
      Constraint constraint3 = Constraint.getConstraint(type3);
      Constraint result3 = context.getResolver().resolveInstance(scope3, type1, "func").check(constraint3);
      
      assertNotNull(result3);
      assertNotNull(result3.getType(scope3));
      assertEquals(result3.getType(scope3), typeInteger);   
      
      Scope scope4 = type4.getScope();
      Constraint constraint4 = Constraint.getConstraint(type4);
      Constraint result4 = context.getResolver().resolveInstance(scope4, type1, "func").check(constraint4);
      
      assertNotNull(result4);
      assertNotNull(result4.getType(scope4));
      assertEquals(result4.getType(scope3), typeBoolean);     
   }
   
   public void testNestedGenericTransformation() throws Exception {
      Store store = new ClassPathStore();
      Context context = new StoreContext(store, null);
      Compiler compiler = new StringCompiler(context);
      Model model = new EmptyModel();
      
      System.err.println(SOURCE_2);
      
      compiler.compile(SOURCE_2).execute(model, true);
      
      TypeLoader loader = context.getLoader();
      Type type3 = loader.resolveType(DEFAULT_PACKAGE, "Three");      
      Type type1 = loader.resolveType(DEFAULT_PACKAGE, "One");
      Type typeList = loader.loadType(List.class);
      Type typeMap = loader.loadType(Map.class);      
      Type typeInteger = loader.loadType(Integer.class);
      Type typeString = loader.loadType(String.class);      
      
      Scope scope = type3.getScope();
      Constraint constraint = Constraint.getConstraint(type3);
      Constraint funcResult = context.getResolver().resolveInstance(scope, type1, "func").check(constraint);
      
      assertNotNull(funcResult);
      assertNotNull(funcResult.getType(scope));
      assertEquals(funcResult.getType(scope), typeList);
      assertEquals(funcResult.getGenerics(scope).size(), 1);
      assertEquals(funcResult.getGenerics(scope).get(0).getType(scope), typeList);
      assertEquals(funcResult.getGenerics(scope).get(0).getGenerics(scope).size(), 1);
      assertEquals(funcResult.getGenerics(scope).get(0).getGenerics(scope).get(0).getType(scope), typeInteger);

      Constraint booResult = context.getResolver().resolveInstance(scope, type1, "boo").check(constraint);
      
      assertNotNull(booResult);
      assertNotNull(booResult.getType(scope));
      assertEquals(booResult.getType(scope), typeMap);
      assertEquals(booResult.getGenerics(scope).size(), 2);
      assertEquals(booResult.getGenerics(scope).get(0).getType(scope), typeString);
      assertEquals(booResult.getGenerics(scope).get(1).getType(scope), typeList);
      assertEquals(booResult.getGenerics(scope).get(1).getGenerics(scope).size(), 1);
      assertEquals(booResult.getGenerics(scope).get(1).getGenerics(scope).get(0).getType(scope), typeList);
      assertEquals(booResult.getGenerics(scope).get(1).getGenerics(scope).get(0).getGenerics(scope).size(), 1);         
      assertEquals(booResult.getGenerics(scope).get(1).getGenerics(scope).get(0).getGenerics(scope).get(0).getType(scope), typeInteger);    
      
      Constraint basicResult = context.getResolver().resolveInstance(scope, type1, "basic").check(constraint);
      
      assertNotNull(basicResult);
      assertNotNull(basicResult.getType(scope));
      assertEquals(basicResult.getType(scope), typeString);
   }


}
