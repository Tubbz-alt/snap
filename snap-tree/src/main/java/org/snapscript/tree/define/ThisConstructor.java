package org.snapscript.tree.define;

import static org.snapscript.core.ModifierType.ENUM;
import static org.snapscript.core.ModifierType.MODULE;
import static org.snapscript.core.ModifierType.TRAIT;

import org.snapscript.core.Execution;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.constraint.StaticConstraint;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Type;
import org.snapscript.core.type.TypeBody;
import org.snapscript.core.type.TypePart;
import org.snapscript.core.type.TypeState;
import org.snapscript.parse.StringToken;
import org.snapscript.tree.ArgumentList;
import org.snapscript.tree.construct.CreateObject;

public class ThisConstructor extends TypePart {
   
   private final ArgumentList arguments;
   
   public ThisConstructor(StringToken token) {
      this(token, null);
   }
   
   public ThisConstructor(ArgumentList arguments) {
      this(null, arguments);
   }
   
   public ThisConstructor(StringToken token, ArgumentList arguments) {
      this.arguments = arguments;
   }
   
   @Override
   public TypeState define(TypeBody body, Type type, Scope scope) throws Exception {  
      Execution execution = new StaticBody(body, type);
      Constraint constraint = new StaticConstraint(type);
      CreateObject evaluation = new CreateObject(constraint, arguments, TRAIT.mask | ENUM.mask | MODULE.mask);
      
      return new ThisState(execution, evaluation);
   }   
}