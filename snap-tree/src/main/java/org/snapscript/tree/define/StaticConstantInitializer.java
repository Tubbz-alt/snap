/*
 * StaticConstantInitializer.java December 2016
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

package org.snapscript.tree.define;

import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Type;

public class StaticConstantInitializer extends StaticInitializer {
   
   private final StaticConstantCollector collector;
   
   public StaticConstantInitializer() {
      this.collector = new StaticConstantCollector();
   }

   @Override
   protected Result compile(Type type) throws Exception { 
      collector.collect(type);
      return ResultType.getNormal();
   }
}
