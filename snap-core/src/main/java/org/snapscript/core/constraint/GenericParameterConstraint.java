package org.snapscript.core.constraint;

import static java.util.Collections.EMPTY_LIST;

import java.util.List;

import org.snapscript.core.ModifierType;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Type;

public class GenericParameterConstraint extends Constraint {
   
   private final ConstraintDescription description;
   private final List<Constraint> generics;
   private final String name;
   private final Type type;
   private final int modifiers; 
   
   public GenericParameterConstraint(Type type, String name) {
      this(type, EMPTY_LIST, name);
   }
   
   public GenericParameterConstraint(Type type, String name, int modifiers) {
      this(type, EMPTY_LIST, name, modifiers);
   }
   
   public GenericParameterConstraint(Type type, List<Constraint> generics, String name) {
      this(type, generics, name, 0);
   }
   
   public GenericParameterConstraint(Type type, List<Constraint> generics, String name, int modifiers) {
      this.description = new ConstraintDescription(this, type);
      this.modifiers = modifiers;
      this.generics = generics;
      this.type = type;
      this.name = name;
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
   public String getName(Scope scope) {
      return name;
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
