package org.snapscript.core.scope.index;

import org.snapscript.core.module.Module;
import org.snapscript.core.scope.CompoundScope;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.scope.ScopeState;
import org.snapscript.core.type.Type;
import org.snapscript.core.variable.Value;

public class LocalScope implements Scope {
   
   private final ScopeIndex index;
   private final ScopeTable table;
   private final ScopeState state;
   private final Scope inner;
   private final Scope outer;
   
   public LocalScope(Scope inner, Scope outer) {
      this.state = new LocalState(inner);
      this.index = new StackIndex(inner);
      this.table = new ArrayTable();
      this.inner = inner;
      this.outer = outer;
   }

   @Override
   public Scope getStack() {
      return new CompoundScope(this, outer);
   }
   
   @Override
   public Scope getScope() {
      return outer;
   }
   
   @Override
   public Value getThis() {
      return outer.getThis();
   }
   
   @Override
   public Type getHandle() {
      return inner.getType();
   }

   @Override
   public Type getType() {
      return inner.getType();
   }

   @Override
   public Module getModule() {
      return inner.getModule();
   }
   
   @Override
   public ScopeTable getTable(){
      return table;
   }
   
   @Override
   public ScopeIndex getIndex(){
      return index;
   }

   @Override
   public ScopeState getState() {
      return state;
   }
   
   @Override
   public String toString() {
      return String.valueOf(state);
   }
}