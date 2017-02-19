/*
 * ClosureFunctionFinder.java December 2016
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

package org.snapscript.core.closure;

import static org.snapscript.core.Reserved.METHOD_EQUALS;
import static org.snapscript.core.Reserved.METHOD_HASH_CODE;
import static org.snapscript.core.Reserved.METHOD_TO_STRING;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.snapscript.common.Cache;
import org.snapscript.common.CopyOnWriteCache;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.function.Function;
import org.snapscript.core.function.Parameter;
import org.snapscript.core.function.Signature;

public class ClosureFunctionFinder {
   
   private final Cache<Class, Function> functions;
   private final Set<Class> failures;
   private final TypeLoader loader;
   
   public ClosureFunctionFinder(TypeLoader loader) {
      this.functions = new CopyOnWriteCache<Class, Function>();
      this.failures = new CopyOnWriteArraySet<Class>();
      this.loader = loader;
   }
   
   public Function find(Class actual) throws Exception {
      if(actual.isInterface()) { 
         if(failures.contains(actual)) {
            return null;
         }
         Function function = functions.fetch(actual);
         
         if(function == null) {
            Type type = loader.loadType(actual);
            Function match = find(type);
            
            if(match != null) {
               functions.cache(actual, match);
               return match;
            }
            failures.add(actual);
         }
         return function;
      }
      return null;
   }

   public Function find(Type type) throws Exception {
      List<Function> functions = type.getFunctions();
      int size = functions.size();
      
      if(size > 0) {
         List<Function> matches = new ArrayList<Function>();
         
         for(Function function : functions) {
            int modifiers = function.getModifiers();
            
            if(ModifierType.isAbstract(modifiers)) {
               if(match(function)) {
                  matches.add(function);
               }
            }
         }
         int count = matches.size();
         
         if(count == 1) {
            return matches.get(0);
         }
      } 
      return null;
   }
   
   private boolean match(Function function) throws Exception {
      String name = function.getName();
      Signature signature = function.getSignature();
      List<Parameter> parameters = signature.getParameters();
      int width = parameters.size();
      
      if(name.equals(METHOD_HASH_CODE)) {
         return width != 0;
      } 
      if(name.equals(METHOD_EQUALS)) {
         return width != 1;
      } 
      if(name.equals(METHOD_TO_STRING)) {
         return width != 0;
      }
      return true;
   }
      
   
}
