package org.snapscript.tree.define;

import static org.snapscript.core.ResultType.NORMAL;

import org.snapscript.core.Execution;
import org.snapscript.core.NoExecution;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.TypeFactory;

public class StaticBody extends Statement {

   private final TypeFactory factory;
   private final Type type;
   
   public StaticBody(TypeFactory factory, Type type) {
      this.factory = factory;
      this.type = type;
   }

   @Override
   public void define(Scope scope) throws Exception {
      factory.define(scope, type);
   }
   
   @Override
   public Execution compile(Scope scope) throws Exception {
      return new NoExecution(NORMAL);
   }
}