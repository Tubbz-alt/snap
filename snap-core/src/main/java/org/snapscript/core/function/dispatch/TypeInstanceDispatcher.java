package org.snapscript.core.function.dispatch;

import org.snapscript.core.scope.Scope;
import org.snapscript.core.scope.Value;
import org.snapscript.core.type.Type;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.error.ErrorHandler;
import org.snapscript.core.function.search.FunctionCall;
import org.snapscript.core.function.search.FunctionSearcher;

public class TypeInstanceDispatcher implements FunctionDispatcher<Object> {
   
   private final FunctionSearcher binder;
   private final ErrorHandler handler;
   private final String name;    
   
   public TypeInstanceDispatcher(FunctionSearcher binder, ErrorHandler handler, String name) {
      this.handler = handler;
      this.binder = binder;
      this.name = name;
   }
   
   @Override
   public Constraint compile(Scope scope, Type object, Type... arguments) throws Exception {
      FunctionCall call = binder.searchInstance(scope, object, name, arguments);
      
      if(call == null) {
         handler.handleCompileError(scope, object, name, arguments);
      }
      return call.check();
   }
   
   @Override
   public Value dispatch(Scope scope, Object object, Object... arguments) throws Exception {
      FunctionCall call = binder.searchInstance(scope, object, name, arguments);
      
      if(call == null) {
         handler.handleRuntimeError(scope, object, name, arguments);
      }
      return call.call();
   }
}