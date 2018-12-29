package org.snapscript.core.variable.bind;

import static org.snapscript.core.scope.index.AddressType.MODULE;

import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.constraint.ModuleConstraint;
import org.snapscript.core.module.Module;
import org.snapscript.core.scope.index.Address;
import org.snapscript.core.variable.Constant;
import org.snapscript.core.variable.Value;

public class ModuleResult implements VariableResult {
   
   private final Constraint constraint;
   private final Value value;
   
   public ModuleResult(Module module) {
      this.constraint = new ModuleConstraint(module);
      this.value = new Constant(module, constraint);
   }
   
   @Override
   public Address getAddress(int offset) {
      Module module = value.getValue();
      String name = module.getName();
      
      return MODULE.getAddress(name, offset);
   }

   @Override
   public Constraint getConstraint(Constraint left) {
      return constraint;
   }

   @Override
   public Value getValue(Object left) {
      return value;
   }

}
