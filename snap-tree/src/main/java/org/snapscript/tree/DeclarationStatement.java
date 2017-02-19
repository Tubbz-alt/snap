/*
 * DeclarationStatement.java December 2016
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

import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Module;
import org.snapscript.core.Path;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.error.ErrorHandler;
import org.snapscript.core.trace.Trace;
import org.snapscript.core.trace.TraceInterceptor;
import org.snapscript.core.trace.TraceStatement;
import org.snapscript.core.trace.TraceType;

public class DeclarationStatement implements Compilation {
   
   private final Statement declaration;   
   
   public DeclarationStatement(Modifier modifier, Declaration... declarations) {
      this.declaration = new CompileResult(modifier, declarations);     
   }
   
   @Override
   public Statement compile(Module module, Path path, int line) throws Exception {
      Context context = module.getContext();
      ErrorHandler handler = context.getHandler();
      TraceInterceptor interceptor = context.getInterceptor();
      Trace trace = TraceType.getNormal(module, path, line);
      
      return new TraceStatement(interceptor, handler, declaration, trace);
   }
   
   private static class CompileResult extends Statement {

      private final Declaration[] declarations;
      private final Modifier modifier;
      
      public CompileResult(Modifier modifier, Declaration... declarations) {
         this.declarations = declarations;
         this.modifier = modifier;
      }  
      
      @Override
      public Result compile(Scope scope) throws Exception {
         return ResultType.getDeclare();
      }
      
      @Override
      public Result execute(Scope scope) throws Exception {
         ModifierType type = modifier.getType();
         
         for(Declaration declaration : declarations) {
            declaration.create(scope, type.mask); 
         }
         return ResultType.getNormal();
      }
   }
}