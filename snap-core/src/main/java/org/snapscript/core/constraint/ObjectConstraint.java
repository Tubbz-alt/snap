package org.snapscript.core.constraint;

import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class ObjectConstraint extends Constraint {

   private final Object object;
   
   public ObjectConstraint(Object object) {
      this.object = object;
   }
   
   @Override
   public Type getType(Scope scope){
      Module module = scope.getModule();

      if(object != null) {
         Class require = object.getClass();
         return module.getType(require); 
      }
      return null;
   }
}