package org.snapscript.core.scope;

import org.snapscript.core.module.Module;
import org.snapscript.core.scope.index.ArrayTable;
import org.snapscript.core.scope.index.ScopeIndex;
import org.snapscript.core.scope.index.ScopeTable;
import org.snapscript.core.scope.index.StackIndex;
import org.snapscript.core.type.Type;
import org.snapscript.core.variable.Transient;
import org.snapscript.core.variable.Value;

public class ModelScope implements Scope {
   
   private final ScopeIndex index;
   private final ScopeTable table;
   private final ScopeState state;
   private final Module module;
   
   public ModelScope(Model model, Module module) {
      this(model, module, null);
   }
   
   public ModelScope(Model model, Module module, Scope scope) {
      this.state = new ModelState(model, scope);
      this.index = new StackIndex(scope);
      this.table = new ArrayTable();
      this.module = module;
   }
   
   @Override
   public Scope getStack() {
      return new CompoundScope(this, this);
   } 
   
   @Override
   public Value getThis() {
      return new Transient(this);
   }
   
   @Override
   public Scope getScope() {
      return this;
   } 
   
   @Override
   public ScopeIndex getIndex(){
      return index;
   }
   
   @Override
   public ScopeTable getTable() {
      return table;
   }

   @Override
   public ScopeState getState() {
      return state;
   }
   
   @Override
   public Type getHandle() {
      return null;
   }
   
   @Override
   public Type getType() {
      return null;
   }  

   @Override
   public Module getModule() {
      return module;
   }
   
   @Override
   public String toString() {
      return String.valueOf(state);
   }
}