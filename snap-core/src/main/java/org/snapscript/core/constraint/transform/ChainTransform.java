package org.snapscript.core.constraint.transform;

import org.snapscript.core.constraint.Constraint;

public class ChainTransform implements ConstraintTransform {

   private final ConstraintTransform[] path;

   public ChainTransform(ConstraintTransform[] path){
      this.path = path;
   }

   @Override
   public ConstraintRule apply(Constraint left){
      ConstraintRule rule = null;

      for(ConstraintTransform transform : path) {
         rule = transform.apply(left);
         left = rule.getSource();
      }
      return rule;
   }
}
