package org.snapscript.core.resume;

import org.snapscript.core.error.InternalStateException;

public class ResumeException extends InternalStateException {

   public ResumeException(String message) {
      super(message);
   }

   public ResumeException(Throwable cause) {
      super(cause);
   }

   public ResumeException(String message, Throwable cause) {
      super(message, cause);
   }
}
