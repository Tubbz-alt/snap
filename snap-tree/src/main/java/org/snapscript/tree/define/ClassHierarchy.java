package org.snapscript.tree.define;

import java.util.List;

import org.snapscript.core.InternalStateException;
import org.snapscript.core.ModifierType;
import org.snapscript.core.constraint.AnyConstraint;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.constraint.ConstraintVerifier;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Type;
import org.snapscript.tree.constraint.ClassConstraint;
import org.snapscript.tree.constraint.TraitConstraint;

public class ClassHierarchy implements TypeHierarchy {
      
   private final ConstraintVerifier verifier;
   private final TraitConstraint[] traits; 
   private final ClassConstraint base;
   private final Constraint any;
   
   public ClassHierarchy(TraitConstraint... traits) {
      this(null, traits);     
   }
   
   public ClassHierarchy(ClassConstraint base, TraitConstraint... traits) {
      this.verifier = new ConstraintVerifier();
      this.any = new AnyConstraint();
      this.traits = traits;
      this.base = base;
   }

   @Override
   public void define(Scope scope, Type type) throws Exception {
      List<Constraint> types = type.getTypes();
      
      if(base == null) {
         types.add(any);
      } else {
         Type match = base.getType(scope);
         
         if(match == null) {
            throw new InternalStateException("Invalid super class for type '" + type + "'");
         }
         int modifiers = match.getModifiers();
         
         if(!ModifierType.isClass(modifiers)) {
            throw new InternalStateException("Invalid super class '" + match + "' for type '" + type + "'");
         }
         types.add(base);  
      }
      for(int i = 0; i < traits.length; i++) {
         Constraint trait = traits[i];
         Type match = trait.getType(scope);
         
         if(match == null) {
            throw new InternalStateException("Invalid trait for type '" + type + "'");
         }
         int modifiers = match.getModifiers();
         
         if(!ModifierType.isTrait(modifiers)) {
            throw new InternalStateException("Invalid trait '" + match + "' for type '" + type + "'");
         }
         types.add(trait);
      }
   }
   
   @Override
   public void compile(Scope scope, Type type) throws Exception {
      List<Constraint> types = type.getTypes();
      
      for(Constraint base : types) {
         try {
            verifier.verify(scope, base);
         } catch(Exception e) {
            throw new InternalStateException("Invalid super class for type '" + type + "'", e); 
         }
      }
   }
}