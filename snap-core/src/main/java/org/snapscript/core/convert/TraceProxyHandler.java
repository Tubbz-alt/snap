package org.snapscript.core.convert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.error.ErrorHandler;
import org.snapscript.core.trace.Trace;
import org.snapscript.core.trace.TraceInterceptor;
import org.snapscript.core.trace.TraceType;

public class TraceProxyHandler implements ProxyHandler {
   
   private final InvocationHandler delegate;
   private final Context context;
   private final Scope scope;

   public TraceProxyHandler(InvocationHandler delegate, Context context, Scope scope) {
      this.context = context;
      this.delegate = delegate;
      this.scope = scope;
   }

   @Override
   public Object invoke(Object proxy, Method method, Object[] list) throws Throwable {
      Module module = scope.getModule();
      Trace trace = TraceType.getNative(module); 
      TraceInterceptor interceptor = context.getInterceptor();
      ErrorHandler handler = context.getHandler();
      
      try {
         interceptor.before(scope, trace);
         return delegate.invoke(proxy, method, list); 
      } catch(Exception cause) {
         return handler.throwInternal(scope, cause);
      } finally {
         interceptor.after(scope, trace);
      }
   }
   
   @Override
   public Scope extract() {
      return scope;
   }  
}