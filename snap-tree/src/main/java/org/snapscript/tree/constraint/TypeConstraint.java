package org.snapscript.tree.constraint;

import java.util.List;

import org.snapscript.core.Evaluation;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.error.InternalStateException;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Type;

public class TypeConstraint extends Constraint {

   private Evaluation evaluation;
   private Constraint constraint;
   private Constraint left;
   
   public TypeConstraint(Evaluation evaluation) {
      this(evaluation, null);
   }
   
   public TypeConstraint(Evaluation evaluation, Constraint left) {
      this.evaluation = evaluation;
      this.left = left;
   }
   
   @Override
   public List<String> getImports(Scope scope) {
      if(constraint == null) {
         try {
            constraint = evaluation.compile(scope, left);
         } catch (Exception e) {
            throw new InternalStateException("Import not found", e);
         }
      }
      return constraint.getImports(scope);
   }
   
   @Override
   public List<Constraint> getGenerics(Scope scope) {
      if(constraint == null) {
         try {
            constraint = evaluation.compile(scope, left);
         } catch (Exception e) {
            throw new InternalStateException("Import not found", e);
         }
      }
      return constraint.getGenerics(scope);
   }
   
   @Override
   public Type getType(Scope scope) {
      if(constraint == null) {
         try {
            constraint = evaluation.compile(scope, left);
         } catch (Exception e) {
            throw new InternalStateException("Import not found", e);
         }
      }
      return constraint.getType(scope);
   }
   
   @Override
   public String getName(Scope scope) {
      if(constraint == null) {
         try {
            constraint = evaluation.compile(scope, left);
         } catch (Exception e) {
            throw new InternalStateException("Import not found", e);
         }
      }
      return constraint.getName(scope);
   }
   
   @Override
   public String toString() {
      return String.valueOf(constraint);
   }
}
