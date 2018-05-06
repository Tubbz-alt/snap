package org.snapscript.core.constraint.transform;

import org.snapscript.core.constraint.Constraint;

public class IdentityTransform implements ConstraintTransform{
   
   private final ConstraintIndex index;
   
   public IdentityTransform(ConstraintIndex index){
      this.index = index;
   }
   
   @Override
   public ConstraintHandle apply(Constraint source){
      return new ConstraintHandle(source, index);
   }
}