package org.snapscript.tree.closure;

import static org.snapscript.core.constraint.Constraint.NONE;

import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.ClosureScopeCompiler;
import org.snapscript.core.Compilation;
import org.snapscript.core.Evaluation;
import org.snapscript.core.LocalScopeExtractor;
import org.snapscript.core.Module;
import org.snapscript.core.Path;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.function.Function;
import org.snapscript.core.function.FunctionHandle;
import org.snapscript.core.function.Signature;
import org.snapscript.tree.Expression;
import org.snapscript.tree.ExpressionStatement;

public class Closure implements Compilation {
   
   private final ClosureParameterList parameters;
   private final ExpressionStatement compilation;
   private final Statement statement;
   
   public Closure(ClosureParameterList parameters, Statement statement){  
      this(parameters, statement, null);
   }  
   
   public Closure(ClosureParameterList parameters, Expression expression){
      this(parameters, null, expression);
   }
   
   public Closure(ClosureParameterList parameters, Statement statement, Expression expression){
      this.compilation = new ExpressionStatement(expression);
      this.parameters = parameters;
      this.statement = statement;
   }
   
   @Override
   public Evaluation compile(Module module, Path path, int line) throws Exception {
      Statement closure = statement;
      
      if(closure == null) {
         closure = compilation.compile(module, path, line);
      }
      return new CompileResult(parameters, closure, module);
   }
  
   private static class CompileResult extends Evaluation {
   
      private final AtomicReference<FunctionHandle> reference;
      private final LocalScopeExtractor extractor;
      private final ClosureParameterList parameters;
      private final ClosureScopeCompiler compiler;
      private final ClosureBuilder builder;
      private final Module module;

      public CompileResult(ClosureParameterList parameters, Statement closure, Module module){
         this.reference = new AtomicReference<FunctionHandle>();
         this.extractor = new LocalScopeExtractor(false, true);
         this.builder = new ClosureBuilder(closure, module);
         this.compiler = new ClosureScopeCompiler();
         this.parameters = parameters;
         this.module = module;
      }
      
      @Override
      public void define(Scope scope) throws Exception {
         Scope parent = module.getScope();
         Signature signature = parameters.create(parent);
         Scope capture = extractor.extract(scope);
         FunctionHandle handle = builder.create(signature, capture); // creating new function each time

         handle.define(capture);
         reference.set(handle);
      }
      
      @Override
      public Constraint compile(Scope scope, Constraint left) throws Exception {
         Type type = scope.getType();
         FunctionHandle handle = reference.get();
         Scope capture = extractor.extract(scope);
         Function function = handle.create(capture);   
         Constraint constraint = function.getConstraint();
         Scope combined = compiler.compile(capture, type, function);
         
         constraint.getType(scope);
         handle.compile(combined);
        
         return NONE;
      }
      
      @Override
      public Value evaluate(Scope scope, Object left) throws Exception {
         FunctionHandle handle = reference.get();
         Scope capture = extractor.extract(scope);
         Function function = handle.create(capture);
         
         return Value.getTransient(function);
      }
   }
}