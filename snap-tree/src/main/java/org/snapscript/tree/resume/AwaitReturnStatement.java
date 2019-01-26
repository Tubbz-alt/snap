package org.snapscript.tree.resume;

import org.snapscript.core.Bug;
import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Execution;
import org.snapscript.core.Statement;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.error.ErrorHandler;
import org.snapscript.core.module.Module;
import org.snapscript.core.module.Path;
import org.snapscript.core.result.Result;
import org.snapscript.core.resume.Resume;
import org.snapscript.core.resume.Yield;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.trace.Trace;
import org.snapscript.core.trace.TraceInterceptor;
import org.snapscript.core.trace.TraceStatement;
import org.snapscript.core.variable.Value;
import org.snapscript.tree.SuspendStatement;

public class AwaitReturnStatement implements Compilation {

   private final Statement control;

   public AwaitReturnStatement(Evaluation evaluation){
      this.control = new CompileResult(evaluation);
   }

   @Override
   public Statement compile(Module module, Path path, int line) throws Exception {
      Context context = module.getContext();
      ErrorHandler handler = context.getHandler();
      TraceInterceptor interceptor = context.getInterceptor();
      Trace trace = Trace.getNormal(module, path, line);

      return new TraceStatement(interceptor, handler, control, trace);
   }

   private static class CompileResult extends Statement {

      private final Evaluation evaluation;

      public CompileResult(Evaluation evaluation) {
         this.evaluation = evaluation;
      }

      @Override
      public boolean define(Scope scope) throws Exception {
         if(evaluation != null) {
            evaluation.define(scope);
         }
         return true;
      }

      @Override
      public Execution compile(Scope scope, Constraint returns) throws Exception {
         if(evaluation != null) {
            evaluation.compile(scope, null);
         }
         return new CompileExecution(evaluation);
      }
   }

   private static class CompileExecution extends SuspendStatement<Object> {

      private final Evaluation evaluation;

      @Bug("event an 'return await' statement should not block")
      public CompileExecution(Evaluation evaluation){
         this.evaluation = new AwaitExpression(evaluation);
      }

      @Override
      public Result execute(Scope scope) throws Exception {
         Result result = Result.getAwait(null, scope, this);
         Yield value = result.getValue();

         return suspend(scope, result, this, null);
      }

      @Bug("this blocks, is that correct?")
      @Override
      public Result resume(Scope scope, Object data) throws Exception {
         Value value = evaluation.evaluate(scope, null);
         Object object = value.getValue();

         return Result.getReturn(object);
      }

      @Override
      public Resume suspend(Result result, Resume resume, Object value) throws Exception {
         return new AwaitResume(resume, null);
      }
   }
}
