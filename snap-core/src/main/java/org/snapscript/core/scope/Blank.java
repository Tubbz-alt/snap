package org.snapscript.core.scope;

import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.error.InternalStateException;
import org.snapscript.core.type.Type;

public class Blank extends Value {
   
   private final AtomicReference<Object> reference;
   private final Type type;
   private final int modifiers;
   
   public Blank(Object value, Type type, int modifiers) {
      this.reference = new AtomicReference<Object>(value);
      this.modifiers = modifiers;
      this.type = type;
   }
   
   @Override
   public boolean isConstant() {
      return reference.get() != null;
   }
   
   @Override
   public boolean isProperty() {
      return modifiers != -1;
   }
   
   @Override
   public int getModifiers() {
      return modifiers;
   }
   
   @Override
   public Type getType(Scope scope) {
      return type;
   }
   
   @Override
   public <T> T getValue() {
      return (T)reference.get();
   }
   
   @Override
   public void setValue(Object value){
      if(!reference.compareAndSet(null, value)) {
         throw new InternalStateException("Illegal modification of constant");
      }
   } 
   
   @Override
   public String toString() {
      return String.valueOf(reference);
   }
}