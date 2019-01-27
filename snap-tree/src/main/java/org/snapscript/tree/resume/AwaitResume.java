package org.snapscript.tree.resume;

import org.snapscript.core.resume.Promise;
import org.snapscript.core.result.Result;
import org.snapscript.core.resume.Resume;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.variable.Value;
import org.snapscript.tree.Suspend;

public class AwaitResume extends Suspend<Object, Resume> {

   private final Resume child;
   private final Value state;

   public AwaitResume(Resume child, Value state){
      this.child = child;
      this.state = state;
   }

   @Override
   public Result resume(Scope scope, Object value) throws Exception {
      if(state != null) {
         Object object = state.getValue();

         if(Promise.class.isInstance(object)) {
            Promise promise = (Promise)object;
            Object result = promise.value();
            Value state = Value.getTransient(result);

            return child.resume(scope, state);
         }
         return child.resume(scope, state);
      }
      return child.resume(scope, null);
   }

   @Override
   public Resume suspend(Result result, Resume resume, Resume value) throws Exception {
      return null;
   }
}
