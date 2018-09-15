package org.snapscript.core.constraint.transform;

import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.scope.Scope;

public class ConstraintIndexRule extends ConstraintRule {

   private final ConstraintIndex index;
   private final Constraint constraint;

   public ConstraintIndexRule(Constraint constraint) {
      this(constraint, null);
   }

   public ConstraintIndexRule(Constraint constraint, ConstraintIndex index) {
      this.constraint = constraint;
      this.index = index;
   }

   @Override
   public Constraint getResult(Scope scope, Constraint returns) {
      if(index != null) {
         return index.update(scope, constraint, returns);
      }
      return returns;
   }

   @Override
   public Constraint getSource(){
      return constraint;
   }
}
