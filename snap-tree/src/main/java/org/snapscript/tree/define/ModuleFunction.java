package org.snapscript.tree.define;

import java.util.List;

import org.snapscript.core.Constraint;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.function.Function;
import org.snapscript.core.function.FunctionHandle;
import org.snapscript.core.function.Signature;
import org.snapscript.tree.ModifierList;
import org.snapscript.tree.NameReference;
import org.snapscript.tree.annotation.AnnotationList;
import org.snapscript.tree.constraint.SafeConstraint;
import org.snapscript.tree.function.ParameterList;

public class ModuleFunction implements ModulePart {
   
   private final AnnotationList annotations;
   private final ParameterList parameters;
   private final NameReference reference;
   private final Constraint constraint;
   private final Statement statement;
   
   public ModuleFunction(AnnotationList annotations, ModifierList modifiers, Evaluation identifier, ParameterList parameters, Statement statement){  
      this(annotations, modifiers, identifier, parameters, null, statement);
   }
   
   public ModuleFunction(AnnotationList annotations, ModifierList modifiers, Evaluation identifier, ParameterList parameters, Constraint constraint, Statement statement){  
      this.constraint = new SafeConstraint(constraint);
      this.reference = new NameReference(identifier);
      this.annotations = annotations;
      this.parameters = parameters;
      this.statement = statement;
   }  
   
   @Override
   public Statement define(ModuleBody body) throws Exception {
      return new DefineResult(body, statement);
   }
   
   private class DefineResult extends Statement {
   
      private final ModuleFunctionBuilder builder;
      
      public DefineResult(ModuleBody body, Statement statement) {
         this.builder = new ModuleFunctionBuilder(body, statement);
      }
      
      @Override
      public void compile(Scope scope) throws Exception {
         Module module = scope.getModule();
         List<Function> functions = module.getFunctions();
         Signature signature = parameters.create(scope);
         String name = reference.getName(scope);
         Type returns = constraint.getType(scope);
         FunctionHandle handle = builder.create(signature, module, returns, name);
         Function function = handle.compile(scope);
         
         annotations.apply(scope, function);
         functions.add(function);
         handle.create(scope); // count stack
      }
   }
}