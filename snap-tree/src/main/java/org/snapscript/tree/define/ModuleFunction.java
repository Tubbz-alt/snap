package org.snapscript.tree.define;

import static org.snapscript.core.result.Result.NORMAL;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.Execution;
import org.snapscript.core.ModifierValidator;
import org.snapscript.core.NoExecution;
import org.snapscript.core.Statement;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.constraint.DeclarationConstraint;
import org.snapscript.core.function.Function;
import org.snapscript.core.function.FunctionBody;
import org.snapscript.core.function.Signature;
import org.snapscript.core.module.Module;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Type;
import org.snapscript.tree.ModifierList;
import org.snapscript.tree.annotation.AnnotationList;
import org.snapscript.tree.compile.TypeScopeCompiler;
import org.snapscript.tree.constraint.FunctionName;
import org.snapscript.tree.function.ParameterList;

public class ModuleFunction implements ModulePart {
   
   private final DeclarationConstraint constraint;
   private final AnnotationList annotations;
   private final ParameterList parameters;
   private final FunctionName identifier;
   private final ModifierList modifiers;
   private final Statement statement;
   
   public ModuleFunction(AnnotationList annotations, ModifierList modifiers, FunctionName identifier, ParameterList parameters, Statement statement){
      this(annotations, modifiers, identifier, parameters, null, statement);
   }
   
   public ModuleFunction(AnnotationList annotations, ModifierList modifiers, FunctionName identifier, ParameterList parameters, Constraint constraint, Statement statement){
      this.constraint = new DeclarationConstraint(constraint);
      this.annotations = annotations;
      this.identifier = identifier;
      this.parameters = parameters;
      this.statement = statement;
      this.modifiers = modifiers;
   }  
   
   @Override
   public Statement define(ModuleBody body, Module module) throws Exception {
      Scope scope = module.getScope();
      String name = identifier.getName(scope);
      int mask = modifiers.getModifiers();
      
      return new DefineResult(body, statement, name, mask);
   }
   
   private class DefineResult extends Statement {
   
      private final AtomicReference<FunctionBody> cache;
      private final ModuleFunctionBuilder builder;
      private final ModifierValidator validator;
      private final TypeScopeCompiler compiler;
      private final Execution execution;
      private final String name;
      private final int modifiers;
      
      public DefineResult(ModuleBody body, Statement statement, String name, int modifiers) {
         this.builder = new ModuleFunctionBuilder(body, statement);
         this.cache = new AtomicReference<FunctionBody>();
         this.execution = new NoExecution(NORMAL);
         this.validator = new ModifierValidator();
         this.compiler = new TypeScopeCompiler();
         this.modifiers = modifiers;
         this.name = name;
      }
      
      @Override
      public boolean define(Scope scope) throws Exception {
         Module module = scope.getModule();
         List<Function> functions = module.getFunctions();
         Signature signature = parameters.create(scope);
         Constraint require = constraint.getConstraint(scope, modifiers);
         FunctionBody body = builder.create(signature, module, require, name);
         Function function = body.create(scope);
         Constraint constraint = function.getConstraint();         
         
         validator.validate(module, function, modifiers);
         annotations.apply(scope, function);
         functions.add(function);
         body.define(scope); // count stack
         constraint.getType(scope);
         cache.set(body);
         
         return false;
      }      
      
      @Override
      public Execution compile(Scope scope, Constraint returns) throws Exception {
         FunctionBody body = cache.get();
         Module module = scope.getModule();
         Type type = module.getType(); // ???
         Function function = body.create(scope);
         Scope outer = compiler.compile(scope, type, function);

         body.compile(outer);
         
         return execution;
      }
   }
}