package org.snapscript.tree.define;

import java.util.List;

import org.snapscript.core.Statement;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.constraint.DeclarationConstraint;
import org.snapscript.core.function.Signature;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Type;
import org.snapscript.tree.ModifierChecker;
import org.snapscript.tree.ModifierList;
import org.snapscript.tree.constraint.FunctionName;
import org.snapscript.tree.function.ParameterList;

public class MemberFunctionAssembler {
   
   private final DeclarationConstraint constraint;
   private final ParameterList parameters;
   private final ModifierChecker checker;
   private final FunctionName identifier;
   private final ModifierList list;
   private final Statement body;
   
   public MemberFunctionAssembler(ModifierList list, FunctionName identifier, ParameterList parameters, Constraint constraint, Statement body){
      this.constraint = new DeclarationConstraint(constraint);
      this.checker = new ModifierChecker(list);
      this.identifier = identifier;
      this.parameters = parameters;
      this.list = list;
      this.body = body;
   } 

   public MemberFunctionBuilder assemble(Scope scope, int mask) throws Exception {
      int modifiers = list.getModifiers();
      String name = identifier.getName(scope);
      List<Constraint> generics = identifier.getGenerics(scope);
      Signature signature = parameters.create(scope, generics);
      Constraint require = constraint.getConstraint(scope, modifiers | mask);
      
      if(checker.isStatic()) {
         return new StaticFunctionBuilder(signature, body, require, name, modifiers | mask);
      }
      return new InstanceFunctionBuilder(signature, body, require, name, modifiers | mask);
      
   }
}