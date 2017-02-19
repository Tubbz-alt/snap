/*
 * SyntaxParser.java December 2016
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

public class SyntaxParser {   
   
   private final SyntaxTreeBuilder builder;
   private final GrammarResolver resolver;
   
   public SyntaxParser(GrammarResolver resolver, GrammarIndexer indexer) {
      this.builder = new SyntaxTreeBuilder(indexer);
      this.resolver = resolver;
   }   

   public SyntaxNode parse(String resource, String expression, String name) {
      GrammarCache cache = new GrammarCache();
      
      if(expression == null) {
         throw new IllegalArgumentException("Expression for '" + resource + "' is null");
      }
      Grammar grammar = resolver.resolve(name);
      
      if(grammar == null) {
         throw new IllegalArgumentException("Grammar '" + name + "' is not defined");
      }               
      SyntaxTree tree = builder.create(resource, expression, name);
      int length = tree.length();
      
      if(length > 0) {
         GrammarMatcher matcher = grammar.create(cache, length);
         SyntaxChecker checker = tree.check();
         
         if(matcher.check(checker, 0)) { // two phase for performance
            SyntaxBuilder builder = tree.build();
               
            if(matcher.build(builder, 0)) {
               builder.commit();
               return tree.commit();
            }
            throw new IllegalArgumentException("Grammar '" + name + "' failed to build");
         }
         checker.validate(); // syntax errors
      }
      return null;
   } 
}
