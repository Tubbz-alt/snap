package org.snapscript.core.function.dispatch;

import org.snapscript.core.module.Module;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.scope.Value;
import org.snapscript.core.type.Type;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.error.ErrorHandler;
import org.snapscript.core.function.search.FunctionCall;
import org.snapscript.core.function.search.FunctionSearcher;

public class LocalDispatcher implements FunctionDispatcher<Object> {
   
   private final FunctionSearcher binder;
   private final ErrorHandler handler;
   private final String name;  
   
   public LocalDispatcher(FunctionSearcher binder, ErrorHandler handler, String name) {
      this.handler = handler;
      this.binder = binder;
      this.name = name;
   }

   @Override
   public Constraint compile(Scope scope, Type object, Type... arguments) throws Exception {
      FunctionCall call = bind(scope, object, arguments);
      
      if(call == null) {
         handler.handleCompileError(scope, name, arguments);
      }
      return call.check();
   }
   
   @Override
   public Value dispatch(Scope scope, Object object, Object... arguments) throws Exception {
      FunctionCall call = bind(scope, object, arguments);
      
      if(call == null) {
         handler.handleRuntimeError(scope, name, arguments);
      }
      return call.call();
   }
   
   private FunctionCall bind(Scope scope, Object object, Object... arguments) throws Exception {
      Module module = scope.getModule();
      FunctionCall local = binder.searchModule(scope, module, name, arguments);
      
      if(local == null) {
         FunctionCall closure = binder.searchScope(scope, name, arguments); // function variable
         
         if(closure != null) {
            return closure;   
         }
      }
      return local;  
   }
   
   private FunctionCall bind(Scope scope, Type object, Type... arguments) throws Exception {
      Module module = scope.getModule();
      FunctionCall local = binder.searchModule(scope, module, name, arguments);
      
      if(local == null) {
         FunctionCall closure = binder.searchScope(scope, name, arguments); // function variable
         
         if(closure != null) {
            return closure;   
         }
      }
      return local;  
   }
   
}