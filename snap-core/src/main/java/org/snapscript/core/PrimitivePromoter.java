/*
 * PrimitivePromoter.java December 2016
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

package org.snapscript.core;

public class PrimitivePromoter {

   public PrimitivePromoter() {
      super();
   }
   
   public Class promote(Class type) {
      if (type == int.class) {
         return Integer.class;
      }
      if (type == double.class) {
         return Double.class;
      }
      if (type == float.class) {
         return Float.class;
      }
      if (type == boolean.class) {
         return Boolean.class;
      }
      if (type == byte.class) {
         return Byte.class;
      }
      if (type == short.class) {
         return Short.class;
      }
      if (type == long.class) {
         return Long.class;
      }
      if (type == char.class) {
         return Character.class;
      }
      if (type == void.class) {
         return Void.class;
      }
      return type;
   }
}
