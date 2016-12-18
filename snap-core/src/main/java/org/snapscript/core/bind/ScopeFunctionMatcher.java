package org.snapscript.core.bind;

import static org.snapscript.core.convert.Score.INVALID;

import org.snapscript.core.Bug;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Value;
import org.snapscript.core.convert.Score;
import org.snapscript.core.function.ArgumentConverter;
import org.snapscript.core.function.Function;
import org.snapscript.core.function.Signature;
import org.snapscript.core.thread.ThreadStack;

public class ScopeFunctionMatcher {
   
   private final ThreadStack stack;
   
   public ScopeFunctionMatcher(ThreadStack stack) {
      this.stack = stack;
   }
   
   @Bug("performance problem")
   public FunctionPointer match(Scope scope, String name, Object... values) throws Exception { // match function variable
      State state = scope.getState();
      Value value = state.get(name); // use VariableResolver type logic
      
      if(value != null) {
         Object object = value.getValue();
         
         if(Function.class.isInstance(object)) {
            Function function = (Function)object;
            Signature signature = function.getSignature();
            ArgumentConverter match = signature.getConverter();
            Score score = match.score(values);
            
            if(score.compareTo(INVALID) > 0) {
               return new FunctionPointer(function, stack, values);
            }
         }
      }
      return null;
   }
}
