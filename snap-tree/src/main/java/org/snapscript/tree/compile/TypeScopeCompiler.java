package org.snapscript.tree.compile;

import static org.snapscript.core.Reserved.TYPE_THIS;

import java.util.List;

import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.function.Function;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.scope.State;
import org.snapscript.core.type.Type;
import org.snapscript.core.variable.Value;
import org.snapscript.tree.constraint.GenericList;

public class TypeScopeCompiler extends ScopeCompiler{
   
   protected final GenericList generics;
   
   public TypeScopeCompiler(GenericList generics) {
      this.generics = generics;
   }
   
   @Override
   public Scope define(Scope scope, Type type) throws Exception{
      List<Constraint> constraints = generics.getGenerics(scope);
      Scope outer = type.getScope();
      Scope inner = outer.getStack();
      State state = inner.getState();
      int size = constraints.size();

      for(int i = 0; i < size; i++) {
         Constraint constraint = constraints.get(i);    
         String name = constraint.getName(inner);

         state.addConstraint(name, constraint);
      }
      return inner;
   }

   @Override
   public Scope compile(Scope scope, Type type, Function function) throws Exception {
      List<Constraint> constraints = generics.getGenerics(scope);
      Scope outer = type.getScope();
      Scope inner = outer.getStack();
      State state = inner.getState();
      int size = constraints.size();

      compileParameters(inner, function);
      compileProperties(inner, type);    

      for(int i = 0; i < size; i++) {
         Constraint constraint = constraints.get(i);    
         String name = constraint.getName(inner);

         state.addConstraint(name, constraint);
      } 
      Constraint constraint = Constraint.getConstraint(type);
      Value value = Value.getConstant(scope, constraint);
      
      state.addValue(TYPE_THIS, value);
      
      return inner;
   }
}
