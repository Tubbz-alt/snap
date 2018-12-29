package org.snapscript.core.scope;

import org.snapscript.core.module.Module;
import org.snapscript.core.scope.index.ArrayTable;
import org.snapscript.core.scope.index.Index;
import org.snapscript.core.scope.index.StackIndex;
import org.snapscript.core.scope.index.Table;
import org.snapscript.core.type.Type;
import org.snapscript.core.variable.Transient;
import org.snapscript.core.variable.Value;

public class ModelScope implements Scope {
   
   private final Module module;
   private final Index index;
   private final Table table;
   private final State state;
   
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
   public Index getIndex(){
      return index;
   }
   
   @Override
   public Table getTable() {
      return table;
   }

   @Override
   public State getState() {
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