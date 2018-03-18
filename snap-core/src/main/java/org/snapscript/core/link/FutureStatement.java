package org.snapscript.core.link;

import java.util.concurrent.FutureTask;

import org.snapscript.core.Execution;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Path;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;

public class FutureStatement extends Statement {
   
   private final FutureTask<Statement> result;
   private final Path path;
   
   public FutureStatement(FutureTask<Statement> result, Path path) {
      this.result = result;
      this.path = path;
   }

   @Override
   public void create(Scope scope) throws Exception {
      Statement definition = result.get();
      
      if(definition == null) {
         throw new InternalStateException("Could not define '" + path + "'");
      }
      definition.create(scope);
   }
   
   @Override
   public void define(Scope scope) throws Exception {
      Statement definition = result.get();
      
      if(definition == null) {
         throw new InternalStateException("Could not compile '" + path + "'");
      }
      definition.define(scope);
   }
   
   @Override
   public Execution compile(Scope scope) throws Exception {
      Statement definition = result.get();
      
      if(definition == null) {
         throw new InternalStateException("Could not validate '" + path + "'");
      }
      return definition.compile(scope);
   }
}