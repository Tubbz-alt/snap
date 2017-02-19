/*
 * InterfaceCollector.java December 2016
 *
 * Copyright (C) 2016, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.snapscript.core.convert;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.snapscript.core.Any;
import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeCache;
import org.snapscript.core.TypeExtractor;

public class InterfaceCollector {

   private final TypeCache<Class[]> cache;
   private final Class[] empty;
   
   public InterfaceCollector() {
      this.cache = new TypeCache<Class[]>();
      this.empty = new Class[]{};
   }
   
   public Class[] collect(Scope scope) {
      Type type = scope.getType();
      
      if(type != null) {
         Class[] interfaces = cache.fetch(type);
         
         if(interfaces == null) {
            Set<Class> types = traverse(type);
            Class[] result = types.toArray(empty);
            
            cache.cache(type, result);
            return result;
         }
         return interfaces;
      }
      return empty;
   }
   
   public Class[] filter(Class... types) {
      if(types.length > 0) {
         Set<Class> interfaces = new HashSet<Class>();
         
         for(Class entry : types) {
            if(entry != null) {
               if(entry.isInterface()) {
                  interfaces.add(entry);
               }
            }
         }
         return interfaces.toArray(empty);
      }
      return empty;
   }
   
   private Set<Class> traverse(Type type) {
      Module module = type.getModule();
      Context context = module.getContext();
      TypeExtractor extractor = context.getExtractor();
      Set<Type> types = extractor.getTypes(type);
      
      if(!types.isEmpty()) {
         Set<Class> interfaces = new HashSet<Class>();
      
         for(Type entry : types) {
            Class part = entry.getType();
            
            if(part != null) {
               if(part.isInterface()) {
                  interfaces.add(part);
               }
            }
         }
         interfaces.add(Any.class);
         return interfaces;
      }
      return Collections.<Class>singleton(Any.class);
   }
}
