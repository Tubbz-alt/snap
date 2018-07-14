package org.snapscript.core.constraint;

import static java.util.Collections.EMPTY_LIST;

import java.util.List;

import org.snapscript.core.ModifierType;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Type;

public class TypeConstraint extends Constraint {
   
   private final ConstraintDescription description;
   private final List<Constraint> generics;
   private final Type type;
   private final int modifiers;

   public TypeConstraint(Type type) {
      this(type, EMPTY_LIST);
   }
   
   public TypeConstraint(Type type, List<Constraint> generics) {
      this(type, generics,  0);
   }
   
   public TypeConstraint(Type type, List<Constraint> generics, int modifiers) {
      this.description = new ConstraintDescription(this, type);
      this.modifiers = modifiers;
      this.generics = generics;
      this.type = type;
   }
   
   @Override
   public List<Constraint> getGenerics(Scope scope) {
      return generics;
   }

   @Override
   public Type getType(Scope scope) {
      return type;
   }

   @Override
   public boolean isVariable(){
      return !ModifierType.isConstant(modifiers);
   }
   
   @Override
   public boolean isConstant(){
      return ModifierType.isConstant(modifiers);
   }
   
   @Override
   public boolean isClass(){
      return ModifierType.isClass(modifiers);
   }
   
   @Override
   public String toString() {
      return description.toString();
   }
}
