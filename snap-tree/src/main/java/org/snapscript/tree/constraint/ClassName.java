package org.snapscript.tree.constraint;

import static org.snapscript.core.ModifierType.CLASS;

import java.util.List;

import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.link.ImportManager;
import org.snapscript.core.module.Module;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Type;
import org.snapscript.tree.NameReference;
import org.snapscript.tree.define.TypeName;
import org.snapscript.tree.literal.TextLiteral;

public class ClassName implements TypeName {
   
   private final NameReference reference;
   private final GenericList generics;

   public ClassName(TextLiteral literal, GenericList generics) {
      this.reference = new NameReference(literal);
      this.generics = generics;
   }
   
   @Override
   public int getModifiers(Scope scope) throws Exception{
      return CLASS.mask;
   }
   
   @Override
   public String getName(Scope scope) throws Exception{ // called from outer class
      String name = reference.getName(scope);
      Type parent = scope.getType();
      
      if(parent != null) {
         String prefix = parent.getName();
         
         if(prefix != null) {
            return prefix + '$'+name;
         }
      }
      return name;
   }
   
   @Override
   public List<Constraint> getGenerics(Scope scope) throws Exception {
      List<Constraint> constraints = generics.getGenerics(scope);
      Module module = scope.getModule();
      ImportManager manager = module.getManager();
      
      for(Constraint constraint : constraints) {
         Type type = constraint.getType(scope);         
         String alias = constraint.getName(scope);
      
         if(alias != null) {
            Type parent = scope.getType();
            String prefix = parent.getName();
            
            manager.addImport(type, prefix +'$' +alias);            
         }
      }
      return constraints;
   }
}