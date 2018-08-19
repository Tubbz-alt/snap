package org.snapscript.tree.compile;

import static org.snapscript.core.Reserved.TYPE_THIS;

import java.util.List;

import org.snapscript.core.Bug;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.function.Function;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.scope.State;
import org.snapscript.core.type.Type;
import org.snapscript.core.variable.Value;
import org.snapscript.tree.constraint.FunctionName;

public class TypeScopeCompiler extends ScopeCompiler{
   
   private final FunctionName identifier;
   
   public TypeScopeCompiler(FunctionName identifier) {
      this.identifier = identifier;
   }
   
   @Bug
   public Scope define(Scope scope, Type type) throws Exception{
      List<Constraint> generics = identifier.getGenerics(scope);
      Scope outer = type.getScope();
      Scope inner = outer.getStack();
      State state = inner.getState();
      int size = generics.size();

      for(int i = 0; i < size; i++) {
         Constraint constraint = generics.get(i);    
         String name = constraint.getName(inner);

         state.addConstraint(name, constraint);
      }
      return inner;
   }

   @Bug
   @Override
   public Scope compile(Scope scope, Type type, Function function) throws Exception {
      List<Constraint> generics = identifier.getGenerics(scope);
      Scope outer = type.getScope();
      Scope inner = outer.getStack();
      State state = inner.getState();
      int size = generics.size();

      compileParameters(inner, function);
      compileProperties(inner, type);    

      for(int i = 0; i < size; i++) {
         Constraint constraint = generics.get(i);    
         String name = constraint.getName(inner);

         state.addConstraint(name, constraint);
      } 
      Constraint constraint = Constraint.getConstraint(type);
      Value value = Value.getConstant(scope, constraint);
      
      state.addValue(TYPE_THIS, value);
      
      return inner;
   }
}
