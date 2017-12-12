package org.snapscript.tree.condition;

import org.snapscript.core.Result;
import org.snapscript.core.Resume;
import org.snapscript.core.Scope;
import org.snapscript.core.Yield;
import org.snapscript.tree.Suspend;

public class SwitchResume extends Suspend<Object, Integer> {
   
   private final Resume parent;
   private final Resume child;
   private final int index;
   
   public SwitchResume(Resume child, Resume parent, int index){
      this.parent = parent;
      this.child = child;
      this.index = index;
   }
   
   @Override
   public Result resume(Scope scope, Object value) throws Exception {
      Result result = child.resume(scope, value);
      
      if(result.isYield()) {
         return suspend(scope, result, parent, index);
      }
      if(result.isBreak()) {
         return Result.getNormal();
      }
      if(!result.isNormal()) {
         return result;      
      }
      return parent.resume(scope, index + 1);
   }

   @Override
   public Resume suspend(Result result, Resume resume, Integer value) throws Exception {
      Yield yield = result.getValue();
      Resume child = yield.getResume();
      
      return new SwitchResume(child, resume, value);
   }
}