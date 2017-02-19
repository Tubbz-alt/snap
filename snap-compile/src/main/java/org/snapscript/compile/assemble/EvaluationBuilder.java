/*
 * EvaluationBuilder.java December 2016
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

package org.snapscript.compile.assemble;

import static org.snapscript.tree.Instruction.EXPRESSION;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.core.Evaluation;
import org.snapscript.core.FilePathConverter;
import org.snapscript.core.Path;
import org.snapscript.core.PathConverter;
import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;

public class EvaluationBuilder {

   private final Cache<String, Evaluation> cache;
   private final PathConverter converter;
   private final SyntaxCompiler compiler;
   private final Assembler assembler;
   private final Executor executor;
   private final int limit;
   
   public EvaluationBuilder(Assembler assembler, Executor executor){
      this(assembler, executor, 200);
   }
   
   public EvaluationBuilder(Assembler assembler, Executor executor, int limit) {
      this.cache = new LeastRecentlyUsedCache<String, Evaluation>();
      this.converter = new FilePathConverter();
      this.compiler = new SyntaxCompiler();
      this.assembler = assembler;
      this.executor = executor;
      this.limit = limit;
   }
   
   public Evaluation create(String source, String module) throws Exception{
      Evaluation evaluation = cache.fetch(source);

      if(evaluation == null) {
         Executable executable = new Executable(source, module);
         FutureTask<Evaluation> task = new FutureTask<Evaluation>(executable);
         
         if(executor != null) {
            executor.execute(task); // reduce android stack size using another thread
         } else {
            task.run();
         }
         return task.get();
      }
      return evaluation;
   }
   
   private class Executable implements Callable<Evaluation> {
      
      private final String source;
      private final String module;
      
      public Executable(String source, String module) {
         this.source = source;
         this.module = module;
      }
      
      @Override
      public Evaluation call() throws Exception {
         SyntaxParser parser = compiler.compile();
         SyntaxNode node = parser.parse(module, source, EXPRESSION.name);
         Path path = converter.createPath(module);
         Evaluation evaluation = assembler.assemble(node, path);
         int length = source.length();
         
         if(length < limit) {
            cache.cache(source, evaluation);
         }
         return evaluation;
      }
   }
}
