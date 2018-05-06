package org.snapscript.core.constraint;

import org.snapscript.core.ModifierType;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Type;

public class StaticConstraint extends Constraint {
   
   private final ConstraintDescription description;
   private final Type type;
   private final int modifiers;
   
   public StaticConstraint(Type type) {
      this(type, 0);
   }
   
   public StaticConstraint(Type type, int modifiers) {
      this.description = new ConstraintDescription(this, type);
      this.modifiers = modifiers;
      this.type = type;
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
