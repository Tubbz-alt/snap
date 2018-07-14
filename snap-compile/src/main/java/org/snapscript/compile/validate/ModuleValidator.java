package org.snapscript.compile.validate;

import static org.snapscript.core.type.Phase.COMPILE;

import java.util.List;

import org.snapscript.common.Progress;
import org.snapscript.core.module.Module;
import org.snapscript.core.type.Phase;
import org.snapscript.core.type.Type;

public class ModuleValidator {

   private final TypeValidator validator;
   private final long wait;
   
   public ModuleValidator(TypeValidator validator) {
      this(validator, 60000);
   }
   
   public ModuleValidator(TypeValidator validator, long wait) {
      this.validator = validator;
      this.wait = wait;
   }
   
   public void validate(Module module) throws Exception {
      Progress<Phase> progress = module.getProgress();
      
      if(!progress.wait(COMPILE, wait)) {
         throw new ValidateException("Module '" + module + "' was not compiled");
      }
      validateTypes(module);
   }
   
   private void validateTypes(Module module) throws Exception {
      List<Type> types = module.getTypes();
      String name = module.getName();
      
      
      for(Type type : types) {
         Progress<Phase> progress = type.getProgress();
         
         if(!progress.wait(COMPILE, wait)) {
            throw new ValidateException("Type '" + type +"' was not compiled");
         }
         try {
            validator.validate(type);
         }catch(Exception e) {
            throw new ValidateException("Invalid reference to '" + type +"' in '" + name + "'", e);
         }
      }
   }
}