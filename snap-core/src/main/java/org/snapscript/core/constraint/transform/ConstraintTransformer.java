package org.snapscript.core.constraint.transform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.snapscript.common.Cache;
import org.snapscript.core.EntityTree;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.constraint.GenericConstraint;
import org.snapscript.core.convert.InstanceOfChecker;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Type;
import org.snapscript.core.type.TypeExtractor;

public class ConstraintTransformer {
   
   private final EntityTree<Integer, ConstraintTransform> tree;
   private final ConstraintIndexBuilder indexer;
   private final TypeExtractor extractor;
   
   public ConstraintTransformer(TypeExtractor extractor){
      this.tree = new EntityTree<Integer, ConstraintTransform>();
      this.indexer = new ConstraintIndexBuilder();
      this.extractor = extractor;
   }

   public ConstraintTransform transform(Type constraint, Type require) {
      int index = require.getOrder();
      Cache<Integer, ConstraintTransform> cache = tree.get(constraint);
      ConstraintTransform transform = cache.fetch(index);
      
      if(transform == null) {
         transform = resolve(constraint, require);
         cache.cache(index, transform);
      }
      return transform;
   }
   
   private ConstraintTransform resolve(Type constraint, Type require) {          
      if(constraint == require) { 
         ConstraintIndex index = indexer.index(require); 
         
         if(index == null) {
            throw new InternalStateException("Type '" + require+ "' count not be indexed");
         }
         return new IdentityTransform(index);
      }
      Type entry = constraint.getEntry();
      
      if(entry != null) {
         return resolveArray(constraint, require);
      }
      return resolveType(constraint, require);
   }
   
   private ConstraintTransform resolveArray(Type constraint, Type require) { // String[] -> List
      Class actual = require.getType();
      Type entry = constraint.getEntry();
      Constraint element = Constraint.getConstraint(entry);
      
      if(Iterable.class.isAssignableFrom(actual)) {         
         return resolveArray(element, require);
      }
      throw new InternalStateException("Type '" + require+ "' is not compatible with an array");
   }
   
   private ConstraintTransform resolveArray(Constraint constraint, Type require) { // String[] -> List
      ConstraintIndex index = indexer.index(require);
      List<Constraint> constraints = require.getConstraints();
      
      if(constraints.isEmpty()) {     
         List<Constraint> generics = new ArrayList<Constraint>();
         Constraint result = new GenericConstraint(require, constraints);
         
         generics.add(constraint);
         
         return new StaticTransform(result, index);
      }
      return new EmptyTransform(require); // already parameterized
   }
   
   private ConstraintTransform resolveType(Type constraint, Type require) {
      List<Constraint> constraints = require.getConstraints();
      
      if(!constraints.isEmpty()) {
         List<Constraint> path = extractor.getTypes(constraint, require);
         Constraint original = Constraint.getConstraint(constraint);
         Scope scope = constraint.getScope();
         int count = path.size();
         
         if(count <= 0) {
            throw new InternalStateException("Type '" + require + "' not in hierarchy of '" + constraint +"'");
         }
         ConstraintTransform[] transforms = new ConstraintTransform[count];
         
         for(int i = 0; i < count; i++){
            Constraint base = path.get(i);      
            Type actual = base.getType(scope);
            
            transforms[i] = resolveType(original, actual);
            original = base;
         }
         return new ChainTransform(transforms);
      }
      return new EmptyTransform(require);
   }   
   
   private ConstraintTransform resolveType(Constraint constraint, Type require) {
      Scope scope = require.getScope();
      Type actual = constraint.getType(scope);
      List<Constraint> hierarchy = actual.getTypes();
      
      for(Constraint base : hierarchy) {
         Type type = base.getType(scope);
         
         if(type == require) { // here we know how require was declared in the type
            return resolveType(constraint, base, type);
         }
      } 
      throw new InternalStateException("Type '" + require + "' not in hierarchy of '" + actual +"'");
   }
   
   private ConstraintTransform resolveType(Constraint constraint, Constraint require, Type destination) {
      Scope scope = destination.getScope();
      Type origin = constraint.getType(scope);
      ConstraintIndex originIndex = indexer.index(origin);
      ConstraintIndex requireIndex = indexer.index(destination);
      List<Constraint> generics = require.getGenerics(scope); // extends Map<T, Integer>
      int count = generics.size();
      
      if(count > 0) {
         ConstraintTransform[] transforms = new ConstraintTransform[count];
         
         for(int i = 0; i < count; i++){
            Constraint generic = generics.get(i);
            Constraint parameter = originIndex.update(constraint, generic);
            
            if(parameter == generic) {
               transforms[i] = new StaticParameterTransform(generic); // its already got a class
            }else {
               transforms[i] = new GenericParameterTransform(originIndex, generic);
            }
         }
         return new GenericTransform(destination, requireIndex, transforms);
      }
      return new StaticTransform(require, requireIndex);
   }
}
