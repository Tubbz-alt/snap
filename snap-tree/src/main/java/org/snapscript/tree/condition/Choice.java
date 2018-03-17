package org.snapscript.tree.condition;

import org.snapscript.core.Bug;
import org.snapscript.core.Constraint;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class Choice extends Evaluation {
   
   private final Evaluation condition;
   private final Evaluation positive;
   private final Evaluation negative;
   
   public Choice(Evaluation condition, Evaluation positive, Evaluation negative) {
      this.condition = condition;
      this.positive = positive;
      this.negative = negative;
   }

   @Override
   public void compile(Scope scope) throws Exception {
      condition.compile(scope);
      positive.compile(scope);
      negative.compile(scope);
   }
   
   @Bug("probably do both")
   @Override
   public Constraint validate(Scope scope, Constraint left) throws Exception {
      return positive.validate(scope, left);
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      Value result = condition.evaluate(scope, null);
      boolean value = result.getBoolean();
      
      if(value) {
         return positive.evaluate(scope, left);
      } 
      return negative.evaluate(scope, left);
   }
}