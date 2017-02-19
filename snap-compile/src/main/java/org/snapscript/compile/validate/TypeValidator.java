/*
 * TypeValidator.java December 2016
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

package org.snapscript.compile.validate;

import static org.snapscript.core.Reserved.ANY_TYPE;
import static org.snapscript.core.Reserved.TYPE_CLASS;
import static org.snapscript.core.Reserved.TYPE_CONSTRUCTOR;
import static org.snapscript.core.Reserved.TYPE_THIS;

import java.util.List;
import java.util.Set;

import org.snapscript.core.InternalStateException;
import org.snapscript.core.Module;
import org.snapscript.core.Type;
import org.snapscript.core.TypeExtractor;
import org.snapscript.core.convert.ConstraintMatcher;
import org.snapscript.core.function.Function;
import org.snapscript.core.property.Property;

public class TypeValidator {
   
   private static final String[] PROPERTIES = { TYPE_THIS, TYPE_CLASS };
   private static final String[] TYPES = { ANY_TYPE };
   
   private final PropertyValidator properties;
   private final FunctionValidator functions;
   private final TypeExtractor extractor;
   
   public TypeValidator(ConstraintMatcher matcher, TypeExtractor extractor) {
      this.functions = new FunctionValidator(matcher, extractor);
      this.properties = new PropertyValidator(matcher);
      this.extractor = extractor;
   }
   
   public void validate(Type type) throws Exception {
      Class real = type.getType();
      
      if(real == null) {
         validateModule(type);
         validateHierarchy(type);
         validateFunctions(type);
         validateProperties(type);
      }
   }
   
   private void validateModule(Type type) throws Exception {
      Module module = type.getModule();
      
      if(module == null) {
         throw new InternalStateException("Type '" + type + "' has no module");
      }
   }
   
   private void validateHierarchy(Type type) throws Exception {
      Set<Type> types = extractor.getTypes(type);
      
      for(int i = 0; i < TYPES.length; i++) {
         String require = TYPES[i];
         int matches = 0;
         
         for(Type base : types) {
            String name = base.getName();
            
            if(name.equals(require)) {
               matches++;
            }
         }
         if(matches == 0) {
            throw new InternalStateException("Type '" + type + "' not defined");
         }
      }
   }

   private void validateProperties(Type type) throws Exception {
      List<Property> list = type.getProperties();
      
      for(int i = 0; i < PROPERTIES.length; i++) {
         String require = PROPERTIES[i];
         int matches = 0;
         
         for(Property property : list) {
            String name = property.getName();
            
            if(name.equals(require)) {
               matches++;
            }
            properties.validate(property);
         }
         if(matches == 0) {
            throw new InternalStateException("Type '" + type + "' has no property '" + require + "'");
         }
      }
   }

   private void validateFunctions(Type type) throws Exception {
      List<Function> list = type.getFunctions();
         
      for(Function function : list) {
         String name = function.getName();
         
         if(!name.equals(TYPE_CONSTRUCTOR)) {
            functions.validate(function);
         }
      }
   }
}
