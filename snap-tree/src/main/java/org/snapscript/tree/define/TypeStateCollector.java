package org.snapscript.tree.define;

import static org.snapscript.core.result.Result.NORMAL;
import static org.snapscript.core.type.Category.INSTANCE;
import static org.snapscript.core.type.Category.OTHER;
import static org.snapscript.core.type.Category.STATIC;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.snapscript.core.result.Result;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Category;
import org.snapscript.core.type.Type;
import org.snapscript.core.type.TypeBody;
import org.snapscript.core.type.TypeState;

public class TypeStateCollector extends TypeState implements TypeBody {

   private final List<TypeState> instances;   
   private final List<TypeState> statics;
   private final List<TypeState> other;
   private final List<TypeState> list;
   private final Category category;

   public TypeStateCollector(){
      this(OTHER);
   }
   
   public TypeStateCollector(Category category){
      this.statics = new CopyOnWriteArrayList<TypeState>();
      this.instances = new ArrayList<TypeState>();
      this.other = new ArrayList<TypeState>();
      this.list = new ArrayList<TypeState>();
      this.category = category;
   }

   public void update(TypeState state) throws Exception {
      if(state != null) {         
         list.add(state);
      }
   }
   
   @Override
   public Category define(Scope scope, Type type) throws Exception {
      for(TypeState state : list) {
         Category category = state.define(scope, type);
         
         if(category.isStatic()) {         
            statics.add(state);            
         } else if(category.isInstance()) {
            instances.add(state);
         } else {
            other.add(state);
         }
      }   
      if(!statics.isEmpty()) {
         return STATIC;
      }
      if(!instances.isEmpty()) {
         return INSTANCE;
      }
      return category;
   } 
   
   @Override
   public void compile(Scope scope, Type type) throws Exception {
      for(TypeState state : statics) {
         state.compile(scope, type);
      }
      for(TypeState state : instances) {
         state.compile(scope, type);
      }
      for(TypeState state : other) {
         state.compile(scope, type);
      }
   } 

   @Override
   public void allocate(Scope scope, Type type) throws Exception {
      for(TypeState state : statics) {
         state.allocate(scope, type);
      }
      for(TypeState state : other) {
         state.allocate(scope, type);
      }
      statics.clear();
   } 
   
   @Override
   public Result execute(Scope scope, Type type) throws Exception {
      Result last = NORMAL;
      
      for(TypeState state : instances) {
         state.execute(scope, type);
      }
      for(TypeState state : other) {
         last = state.execute(scope, type);
      }
      return last;
   }              
}