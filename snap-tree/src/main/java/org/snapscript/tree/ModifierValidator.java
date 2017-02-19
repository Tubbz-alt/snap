/*
 * ModifierValidator.java December 2016
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

package org.snapscript.tree;

import static org.snapscript.core.ModifierType.ABSTRACT;
import static org.snapscript.core.ModifierType.CONSTANT;
import static org.snapscript.core.ModifierType.OVERRIDE;
import static org.snapscript.core.ModifierType.PRIVATE;
import static org.snapscript.core.ModifierType.PUBLIC;
import static org.snapscript.core.ModifierType.STATIC;
import static org.snapscript.core.ModifierType.VARIABLE;

import org.snapscript.core.InternalStateException;
import org.snapscript.core.Module;
import org.snapscript.core.Type;
import org.snapscript.core.function.Function;
import org.snapscript.core.property.Property;

public class ModifierValidator {
   
   private static final int CONSTANT_VARIABLE = CONSTANT.mask | VARIABLE.mask;
   private static final int OVERRIDE_STATIC = OVERRIDE.mask | STATIC.mask;
   private static final int ABSTRACT_STATIC = ABSTRACT.mask | STATIC.mask;
   private static final int PUBLIC_PRIVATE = PUBLIC.mask | PRIVATE.mask;
   
   public ModifierValidator() {
      super();
   }
   
   public void validate(Type type, Property property, int modifiers) { 
      if((PUBLIC_PRIVATE & modifiers) == PUBLIC_PRIVATE) {
         throw new InternalStateException("Property '" + type + '.' + property + "' is both public and private");
      }
      if((CONSTANT_VARIABLE & modifiers) == CONSTANT_VARIABLE) {
         throw new InternalStateException("Property '" + type + '.' + property + "' is both variable and constant");
      }
      if((OVERRIDE.mask & modifiers) == OVERRIDE.mask) {
         throw new InternalStateException("Property '" + type + '.' + property + "' is declared as override");
      }
      if((ABSTRACT.mask & modifiers) == ABSTRACT.mask) {
         throw new InternalStateException("Property '" + type + '.' + property + "' is declared as abstract");
      }
   }
   
   public void validate(Type type, Function function, int modifiers) {
      if((PUBLIC_PRIVATE & modifiers) == PUBLIC_PRIVATE) {
         throw new InternalStateException("Function '" + function + "' is both public and private");
      }
      if((OVERRIDE_STATIC & modifiers) == OVERRIDE_STATIC) {
         throw new InternalStateException("Function '" + function + "' is both static and override");
      }
      if((ABSTRACT_STATIC & modifiers) == ABSTRACT_STATIC) {
         throw new InternalStateException("Function '" + function + "' is both static and abstract");
      }
      if((CONSTANT.mask & modifiers) == CONSTANT.mask) {
         throw new InternalStateException("Function '" + function + "' is declared as constant");
      }
      if((VARIABLE.mask & modifiers) == VARIABLE.mask) {
         throw new InternalStateException("Function '" + function + "' is declared as variable");
      }
   }
}
