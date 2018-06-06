package org.snapscript.tree;

import org.snapscript.core.error.InternalException;

public class AssertException extends InternalException {

   public AssertException(String message) {
      super(message);
   }
   
   public AssertException(Throwable cause) {
      super(cause);
   }
   
   public AssertException(String message, Throwable cause) {
      super(message, cause);
   }
}