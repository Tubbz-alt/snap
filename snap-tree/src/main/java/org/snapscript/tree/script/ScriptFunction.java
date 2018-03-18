package org.snapscript.tree.script;

import static org.snapscript.core.ResultType.NORMAL;

import java.util.List;

import org.snapscript.core.Constraint;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Execution;
import org.snapscript.core.Module;
import org.snapscript.core.NoExecution;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.function.Function;
import org.snapscript.core.function.FunctionHandle;
import org.snapscript.core.function.Signature;
import org.snapscript.tree.NameReference;
import org.snapscript.tree.constraint.SafeConstraint;
import org.snapscript.tree.function.FunctionBuilder;
import org.snapscript.tree.function.ParameterList;

public class ScriptFunction extends Statement {
   
   private final ParameterList parameters;
   private final FunctionBuilder builder;
   private final NameReference identifier;
   private final Constraint constraint;
   
   public ScriptFunction(Evaluation identifier, ParameterList parameters, Statement body){  
      this(identifier, parameters, null, body);
   }
   
   public ScriptFunction(Evaluation identifier, ParameterList parameters, Constraint constraint, Statement body){  
      this.constraint = new SafeConstraint(constraint);
      this.identifier = new NameReference(identifier);
      this.builder = new ScriptFunctionBuilder(body);
      this.parameters = parameters;
   }  
   
   @Override
   public void define(Scope scope) throws Exception {
      Module module = scope.getModule();
      List<Function> functions = module.getFunctions();
      Signature signature = parameters.create(scope);
      String name = identifier.getName(scope);
      FunctionHandle handle = builder.create(signature, module, constraint, name);
      Function function = handle.create(scope);
      
      functions.add(function);
      handle.define(scope); // count stack
   }
   
   @Override
   public Execution compile(Scope scope) throws Exception {
      return new NoExecution(NORMAL);
   }
}