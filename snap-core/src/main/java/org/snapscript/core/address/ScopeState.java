package org.snapscript.core.address;

import java.util.Iterator;

import org.snapscript.core.Address;
import org.snapscript.core.AddressTable;
import org.snapscript.core.CompoundIterator;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Value;

public class ScopeState implements State {
   
   private final Scope scope;
   private final State state;
   
   public ScopeState(Scope scope, State state) { // this can wrap multiple types
      this.state = state;
      this.scope = scope;
   }
   
   @Override
   public Iterator<String> iterator() {
      State inner = scope.getState();
      Iterator<String> first = state.iterator();
      Iterator<String> second = inner.iterator();
      
      return new CompoundIterator<String>(first, second);
   }
   
   @Override
   public boolean contains(String name) {
      Address address = address(name);
      int index = address.getIndex();
  
      if(index < 0) {
         return false;
      }
      return true;
   }
   
   @Override
   public Address address(String name){
      State inner = scope.getState();
      Address address = inner.address(name);
      int index = address.getIndex();
      
      if(index < 0) {
         return state.address(name);
      }
      return address; 
   }
   
   @Override
   public Value get(String name){
      State inner = scope.getState();
      Address address = inner.address(name);
      int index = address.getIndex();
      
      if(index < 0) {
         return (Value)state.get(address);
      } 
      return inner.get(name);
   }
   
   @Override
   public Value get(Address address){
      State inner = scope.getState();
      Object source = address.getSource();
      
      if(source == this) {
         return (Value)state.get(address);
      } 
      return inner.get(address);
   }
   
   @Override
   public void set(Address address, Value value){
      State inner = scope.getState();
      Object source = address.getSource();
      
      if(source == this) { // if its not this
         state.set(address, value);
      } else {
         inner.set(address, value);
      }
   }
   
   @Override
   public Address add(String name, Value value){ // this is called by the DeclareProperty
      return state.add(name, value);
   }
}
