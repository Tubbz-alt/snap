package org.snapscript.core.function.dispatch;

import static org.snapscript.core.constraint.Constraint.NONE;

import java.util.Map;

import org.snapscript.core.Context;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.convert.proxy.ProxyWrapper;
import org.snapscript.core.error.ErrorHandler;
import org.snapscript.core.function.Connection;
import org.snapscript.core.function.resolve.FunctionCall;
import org.snapscript.core.function.resolve.FunctionConnection;
import org.snapscript.core.function.resolve.FunctionResolver;
import org.snapscript.core.module.Module;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Type;
import org.snapscript.core.variable.Value;

public class MapDispatcher implements FunctionDispatcher {
   
   private final FunctionResolver resolver;
   private final ErrorHandler handler;
   private final String name;      
   
   public MapDispatcher(FunctionResolver resolver, ErrorHandler handler, String name) {
      this.resolver = resolver;
      this.handler = handler;
      this.name = name;
   }
   
   @Override
   public Constraint compile(Scope scope, Constraint constraint, Type... arguments) throws Exception {
      Type type = constraint.getType(scope);
      FunctionCall local = resolver.resolveInstance(scope, type, name, arguments);
      
      if(local != null) {
         return local.check(scope, constraint, arguments);
      }
      return NONE;      
   }
   
   @Override
   public Connection connect(Scope scope, Value value, Object... arguments) throws Exception {
      Map map = value.getValue();
      FunctionCall call = bind(scope, map, arguments);
      
      if(call == null) {
         handler.failRuntimeInvocation(scope, map, name, arguments);
      }
      return new FunctionConnection(call);
   }
   
   private FunctionCall bind(Scope scope, Map map, Object... arguments) throws Exception {
      Module module = scope.getModule();
      FunctionCall local = resolver.resolveInstance(scope, map, name, arguments);
      
      if(local == null) {
         Object value = map.get(name);
         
         if(value != null) {
            Context context = module.getContext();
            ProxyWrapper wrapper = context.getWrapper();
            Object function = wrapper.fromProxy(value);
            Value reference = Value.getTransient(function);
            
            return resolver.resolveValue(reference, arguments);
         }
      }
      return local;
   }
}