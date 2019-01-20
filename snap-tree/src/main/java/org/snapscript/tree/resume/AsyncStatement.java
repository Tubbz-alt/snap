package org.snapscript.tree.resume;

import static org.snapscript.core.result.Result.NORMAL;

import org.snapscript.core.Context;
import org.snapscript.core.Execution;
import org.snapscript.core.ModifierType;
import org.snapscript.core.NoExecution;
import org.snapscript.core.PromiseWrapper;
import org.snapscript.core.Statement;
import org.snapscript.core.TaskScheduler;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.module.Module;
import org.snapscript.core.scope.Scope;

public class AsyncStatement extends Statement {

   private final PromiseWrapper wrapper;
   private final Statement statement;
   private final Execution empty;
   private final int modifiers;

   public AsyncStatement(Statement statement, int modifiers) {
      this.empty = new NoExecution(NORMAL);
      this.wrapper = new PromiseWrapper();
      this.statement = statement;
      this.modifiers = modifiers;
   }

   @Override
   public void create(Scope scope) throws Exception {
      if(statement != null) {
         statement.create(scope);
      }
   }

   @Override
   public boolean define(Scope scope) throws Exception {
      if(statement != null) {
         return statement.define(scope);
      }
      return false;
   }

   @Override
   public Execution compile(Scope scope, Constraint returns) throws Exception {
      if(statement != null) {
         Constraint actual = wrapper.fromPromise(scope, returns);
         Execution execution = statement.compile(scope, actual);

         if (ModifierType.isAsync(modifiers)) {
            Module module = scope.getModule();
            Context context = module.getContext();
            TaskScheduler scheduler = context.getScheduler();

            return new AsyncExecution(scheduler, execution);
         }
         return execution;
      }
      return empty;
   }
}
