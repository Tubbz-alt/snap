package org.snapscript.tree.dispatch;

import java.util.Map;
import java.util.concurrent.Callable;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.core.convert.ProxyWrapper;
import org.snapscript.core.error.ErrorHandler;

public class MapDispatcher implements InvocationDispatcher {
   
   private final Object object;
   private final Scope scope;      
   
   public MapDispatcher(Scope scope, Object object) {
      this.object = object;
      this.scope = scope;
   }

   @Override
   public Value dispatch(String name, Object... arguments) throws Exception {
      Callable<Value> call = bind(name, arguments);
      
      if(call == null) {
         Module module = scope.getModule();
         Context context = module.getContext();
         ErrorHandler handler = context.getHandler();

         handler.throwInternalException(scope, object, name, arguments);
      }
      return call.call();
   }
   
   private Callable<Value> bind(String name, Object... arguments) throws Exception {
      Module module = scope.getModule();
      Context context = module.getContext();
      FunctionBinder binder = context.getBinder();
      Callable<Value> local = binder.bind(scope, object, name, arguments);
      
      if(local == null) {
         Map map = (Map)object;
         Object object = map.get(name);
         
         if(object != null) {
            ProxyWrapper wrapper = context.getWrapper();
            Object function = wrapper.fromProxy(object);
            Value reference = Value.getTransient(function);
            
            return binder.bind(reference, arguments);
         }
      }
      return local;
   }
}