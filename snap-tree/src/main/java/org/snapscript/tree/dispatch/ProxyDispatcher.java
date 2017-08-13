package org.snapscript.tree.dispatch;

import java.lang.reflect.Proxy;
import java.util.concurrent.Callable;

import org.snapscript.core.Bug;
import org.snapscript.core.Context;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeExtractor;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.core.bind.FunctionBinder;

@Bug("This should work for delegates only")
public class ProxyDispatcher implements InvocationDispatcher {
   
   private final Proxy object;
   private final Scope scope;      
   
   public ProxyDispatcher(Scope scope, Object object) {
      this.object = (Proxy)object;
      this.scope = scope;
   }

   @Override
   public Value dispatch(String name, Object... arguments) throws Exception {
      Module module = scope.getModule();
      Context context = module.getContext();
      FunctionBinder binder = context.getBinder();
      Callable<Result> call = binder.bind(scope, object, name, arguments);
      
      if(call == null) {
         TypeExtractor extractor = context.getExtractor();
         Type type = extractor.getType(object);
         
         throw new InternalStateException("Method '" + name + "' not found for '" + type + "'");
      }
      Result result = call.call();
      Object value = result.getValue();

      return ValueType.getTransient(value);
   }
}