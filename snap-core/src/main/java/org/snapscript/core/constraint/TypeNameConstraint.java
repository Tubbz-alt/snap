package org.snapscript.core.constraint;

import org.snapscript.core.Context;
import org.snapscript.core.module.Module;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Type;
import org.snapscript.core.type.TypeLoader;

public class TypeNameConstraint extends Constraint {

   private String module;
   private String name;
   private Type type;
   
   public TypeNameConstraint(String name, String module) {
      this.module = module;
      this.name = name;
   }

   @Override
   public Type getType(Scope scope) {
      if(type == null) {
         Module parent = scope.getModule();
         Context context = parent.getContext();         
         TypeLoader loader = context.getLoader();
         
         type = loader.loadType(name, module);
      }
      return type;
   }
}
