package org.snapscript.core.type;

import java.util.Collections;
import java.util.Set;

import org.snapscript.common.Cache;
import org.snapscript.common.CopyOnWriteCache;
import org.snapscript.core.type.Type;

public class TypeExtractor {
   
   private final Cache<Class, Type> matches;
   private final TypeTraverser traverser;
   private final TypeLoader loader;
   
   public TypeExtractor(TypeLoader loader) {
      this.matches = new CopyOnWriteCache<Class, Type>();
      this.traverser = new TypeTraverser();
      this.loader = loader;
   }
   
   public Type getType(Class type) {
      Type match = matches.fetch(type);
      
      if(match == null) {
         Type actual = loader.loadType(type);
         
         if(actual != null) {
            matches.cache(type, actual);
         }
         return actual;
      }
      return match;
   }
   
   public Type getType(Object value) {
      if(value != null) {
         Class type = value.getClass();
         
         if(Handle.class.isAssignableFrom(type)) {
            Handle handle = (Handle)value;
            return handle.getHandle();
         }
         Type match = matches.fetch(type);
         
         if(match == null) {    
            Type actual = loader.loadType(type);
            
            if(actual != null) {
               matches.cache(type, actual);
            }
            return actual;
         }
         return match;
      }
      return null;
   }

   
   public Set<Type> getTypes(Object value) {
      Type type = getType(value);
      
      if(type != null) {
         return traverser.findHierarchy(type);
      }
      return Collections.emptySet();
   }   
   
   public Set<Type> getTypes(Type type) {
      return traverser.findHierarchy(type);
   }
   
   public Type getType(Type parent, String name) {
      return traverser.findEnclosing(parent, name);
   }
}