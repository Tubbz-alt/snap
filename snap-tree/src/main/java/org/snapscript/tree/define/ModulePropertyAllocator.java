package org.snapscript.tree.define;

import org.snapscript.core.Evaluation;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.tree.DeclarationAllocator;

public class ModulePropertyAllocator extends DeclarationAllocator {

   public ModulePropertyAllocator(Constraint constraint, Evaluation expression) {   
      super(constraint, expression);
   }
   
   @Override
   protected <T extends Value> T declare(Scope scope, String name, Type type, int modifiers) throws Exception {
      if(ModifierType.isConstant(modifiers)) {
         return (T)Value.getBlank(null, type, modifiers);
      }
      return (T)Value.getProperty(null, type, modifiers);
   }
   
   @Override
   protected <T extends Value> T assign(Scope scope, String name, Object value, Type type, int modifiers) throws Exception {
      if(ModifierType.isConstant(modifiers)) {
         return (T)Value.getConstant(value, type, modifiers);
      }
      return (T)Value.getProperty(value, type, modifiers);
   }
}