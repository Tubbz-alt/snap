package org.snapscript.core.constraint;

import static java.util.Collections.EMPTY_LIST;
import static org.snapscript.core.ModifierType.CLASS;

import org.snapscript.core.module.Module;
import org.snapscript.core.type.Type;
import org.snapscript.core.variable.Value;

public class ConstraintWrapper {
   
   public ConstraintWrapper() {
      super();
   }
   
   public Constraint toConstraint(Object value) {
      return toConstraint(value, null);
   }

   public Constraint toConstraint(Object value, String name) {  
      if(value != null) {
         if(Type.class.isInstance(value)) {
            return new TypeParameterConstraint((Type)value, EMPTY_LIST, name, CLASS.mask);
         }
         if(Module.class.isInstance(value)) {
            return new ModuleConstraint((Module)value);
         }
         if(Value.class.isInstance(value)) {         
            return new ValueConstraint((Value)value);
         } 
         if(Constraint.class.isInstance(value)) {
            return (Constraint)value;
         }
      }
      return new ObjectConstraint(value);
   }
}
