/*
 * StringInterner.java December 2016
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

package org.snapscript.parse;

import java.util.HashMap;
import java.util.Map;

public class StringInterner {

   private final Map<Range, String> cache;
   private final String empty;
   
   public StringInterner() {
      this.cache = new HashMap<Range, String>();
      this.empty = new String();
   }

   public String intern(char[] source) {
      return intern(source, 0, source.length);
   }

   public String intern(char[] source, int start, int length) {
      Range key = new Range(source, start, length);

      if(length > 0) {
         String value = cache.get(key);
   
         if (value == null) {
            String text = key.toString();
            String internal = text.intern(); // reduce calls to intern
            
            cache.put(key, internal);
            return internal;
         }
         return value;
      }
      return empty;
   }
   
   private class Range{
      
      private char[] source;
      private int start;
      private int length;
      private int hash;
      
      public Range(char[] source, int start, int length) {
         this.source = source;
         this.length = length;
         this.start = start;
      }
      
      @Override
      public int hashCode() {
         if(hash == 0) {
            for (int i = 0; i < length; i++) {
                hash = 31 * hash + source[i +start];
            }
         }
         return hash;
      }
      
      @Override
      public boolean equals(Object value) {
         Range key = (Range)value;
         
         if(length != key.length) {
            return false;
         }
         if(hash != key.hash) {
            return false;
         }
         for(int i = 0; i < length; i++) {
            if(source[i+start] != key.source[i+key.start]) {
               return false;
            }
         }
         return true;
      }
      
      @Override
      public String toString() {
         return new String(source, start, length);
      }
   }
}
