/*
 * DefaultMethodAccessor.java December 2016
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

package org.snapscript.core.index;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultMethodAccessor {

   private static final String DEFAULT_METHOD = "isDefault";

   private AtomicBoolean check;
   private Method access;
   
   public DefaultMethodAccessor() {
      this.check = new AtomicBoolean(true);
   }
   
   public Method access() throws Exception {
      if(check.compareAndSet(true, false)) {
         try {
            access = Method.class.getDeclaredMethod(DEFAULT_METHOD);
         } catch(Throwable e) {
            return null;
         }
      }
      return access;
    }
}
