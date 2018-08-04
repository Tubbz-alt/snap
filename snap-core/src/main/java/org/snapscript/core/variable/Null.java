package org.snapscript.core.variable;

import org.snapscript.core.Entity;
import org.snapscript.core.error.InternalStateException;

public class Null extends Value {

   public Null() {
      super();
   }
   
   @Override
   public Entity getSource() {
      return null;
   }
   
   @Override
   public <T> T getValue(){
      return null;
   }
   
   @Override
   public void setValue(Object value){
      throw new InternalStateException("Illegal modification of null");
   } 
   
   @Override
   public String toString() {
      return String.valueOf(null);
   }
}