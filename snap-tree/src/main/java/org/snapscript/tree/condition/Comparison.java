package org.snapscript.tree.condition;

import static org.snapscript.core.constraint.Constraint.BOOLEAN;
import static org.snapscript.core.variable.Value.NULL;

import org.snapscript.core.Evaluation;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.variable.Value;
import org.snapscript.parse.StringToken;

public class Comparison extends Evaluation {   
   
   private final RelationalOperator operator;
   private final Evaluation left;
   private final Evaluation right;
   
   public Comparison(Evaluation left) {
      this(left, null, null);
   }
   
   public Comparison(Evaluation left, StringToken operator, Evaluation right) {
      this.operator = RelationalOperator.resolveOperator(operator);
      this.left = left;
      this.right = right;
   }

   @Override
   public void define(Scope scope) throws Exception {
      if(right != null) {
         right.define(scope);
      }
      left.define(scope);
   }  
   
   @Override
   public Constraint compile(Scope scope, Constraint context) throws Exception {
      if(right != null) {
         right.compile(scope, null);
      }
      left.compile(scope, null);
      return BOOLEAN;
   }
   
   @Override
   public Value evaluate(Scope scope, Value context) throws Exception {
      if(right != null) {
         Value leftResult = left.evaluate(scope, NULL);
         Value rightResult = right.evaluate(scope, NULL);
         
         return operator.operate(scope, leftResult, rightResult);
      }
      return left.evaluate(scope, NULL);
   }      
}