package org.snapscript.compile;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.snapscript.common.store.ClassPathStore;
import org.snapscript.core.Context;
import org.snapscript.core.Reserved;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.index.GenericConstraintResolver;

public class ClassBoundsResolverTest extends TestCase {

   public static class GenericFunction<X extends Boolean, Y extends List<String>>{
      public <T extends String> T doIt(T value) {
         return value;
      }
      public X goForIt(String value) {
         return null;
      }
      public Y justDoIt() {
         return null;
      }
      public List<X> getList() {
         return null;
      }
      public Iterator<Y> getIterator() {
         return null;
      }      
   }
   
   public void testBoundsResolver() throws Exception{
      GenericConstraintResolver resolver = new GenericConstraintResolver();
      Method doIt = GenericFunction.class.getDeclaredMethod("doIt", String.class);
      Method goForIt = GenericFunction.class.getDeclaredMethod("goForIt", String.class);
      Method justDoIt = GenericFunction.class.getDeclaredMethod("justDoIt");
      Method getList = GenericFunction.class.getDeclaredMethod("getList");
      Method getIterator = GenericFunction.class.getDeclaredMethod("getIterator");
      ClassPathStore store = new ClassPathStore();
      Context context = new StoreContext(store);
      Scope scope = context.getRegistry().addModule(Reserved.DEFAULT_PACKAGE).getScope(); 

      assertNotNull(getIterator);
      assertNull(resolver.resolve(getIterator.getGenericReturnType()).getName(scope));
      assertEquals(resolver.resolve(getIterator.getGenericReturnType()).getType(scope).getType(), Iterator.class);
      assertEquals(resolver.resolve(getIterator.getGenericReturnType()).getGenerics(scope).size(), 1);
      assertEquals(resolver.resolve(getIterator.getGenericReturnType()).getGenerics(scope).get(0).getName(scope), "Y");
      assertEquals(resolver.resolve(getIterator.getGenericReturnType()).getGenerics(scope).get(0).getType(scope).getType(), List.class);      
      assertEquals(resolver.resolve(getIterator.getGenericReturnType()).getGenerics(scope).get(0).getGenerics(scope).size(), 1);
      assertNull(resolver.resolve(getIterator.getGenericReturnType()).getGenerics(scope).get(0).getGenerics(scope).get(0).getName(scope));
      assertEquals(resolver.resolve(getIterator.getGenericReturnType()).getGenerics(scope).get(0).getGenerics(scope).get(0).getType(scope).getType(), String.class);
      
      assertNotNull(justDoIt);
      assertEquals(resolver.resolve(justDoIt.getGenericReturnType()).getName(scope), "Y");
      assertEquals(resolver.resolve(justDoIt.getGenericReturnType()).getType(scope).getType(), List.class);
      assertEquals(resolver.resolve(justDoIt.getGenericReturnType()).getGenerics(scope).size(), 1);
      assertNull(resolver.resolve(justDoIt.getGenericReturnType()).getGenerics(scope).get(0).getName(scope));
      assertEquals(resolver.resolve(justDoIt.getGenericReturnType()).getGenerics(scope).get(0).getType(scope).getType(), String.class);
      
      assertNotNull(getList);
      assertNull(resolver.resolve(getList.getGenericReturnType()).getName(scope));
      assertEquals(resolver.resolve(getList.getGenericReturnType()).getType(scope).getType(), List.class);
      
      assertNotNull(doIt);
      assertNull(resolver.resolve(doIt.getGenericReturnType()).getName(scope)); // its a method level generic, so ignore
      assertNull(resolver.resolve(doIt.getGenericReturnType()).getType(scope));
      
      assertNotNull(goForIt);
      assertEquals(resolver.resolve(goForIt.getGenericReturnType()).getName(scope), "X");
      assertEquals(resolver.resolve(goForIt.getGenericReturnType()).getType(scope).getType(), Boolean.class);      
   }
}
