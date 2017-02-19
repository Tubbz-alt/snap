/*
 * ExceptionStatement.java December 2016
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

package org.snapscript.core.link;

import org.snapscript.core.InternalStateException;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;

public class ExceptionStatement extends Statement {
   
   private final Exception cause;
   private final String message;
   
   public ExceptionStatement(String message, Exception cause) {
      this.message = message;
      this.cause = cause;
   }
   
   public Result define(Scope scope) throws Exception {
      throw new InternalStateException(message, cause);
   }
                  
   public Result compile(Scope scope) throws Exception {
      throw new InternalStateException(message, cause);
   }
   
   public Result execute(Scope scope) throws Exception {
      throw new InternalStateException(message, cause);
   }
}