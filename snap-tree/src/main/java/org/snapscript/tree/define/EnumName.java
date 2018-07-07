package org.snapscript.tree.define;

import static java.util.Collections.EMPTY_LIST;
import static org.snapscript.core.ModifierType.ENUM;

import java.util.List;

import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Type;
import org.snapscript.tree.NameReference;
import org.snapscript.tree.literal.TextLiteral;

public class EnumName implements TypeName {
   
   private final NameReference reference;

   public EnumName(TextLiteral literal) {
      this.reference = new NameReference(literal);
   }   

   @Override
   public int getModifiers(Scope scope) throws Exception{
      return ENUM.mask;
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
      return EMPTY_LIST;
   }
}