package org.snapscript.core.function.dispatch;

import static org.snapscript.core.error.Reason.INVOKE;

import java.util.List;

import org.snapscript.core.array.ArrayBuilder;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.error.ErrorHandler;
import org.snapscript.core.function.resolve.FunctionCall;
import org.snapscript.core.function.resolve.FunctionResolver;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Type;
import org.snapscript.core.variable.Value;

public class ArrayDispatcher implements FunctionDispatcher {
   
   private final FunctionResolver resolver;
   private final ArrayBuilder builder;
   private final ErrorHandler handler;
   private final String name;
   
   public ArrayDispatcher(FunctionResolver resolver, ErrorHandler handler, String name) {
      this.builder = new ArrayBuilder();
      this.resolver = resolver;
      this.handler = handler;
      this.name = name;
   }
   
   @Override
   public Constraint compile(Scope scope, Constraint constraint, Type... arguments) throws Exception {
      Type actual = constraint.getType(scope);
      Type list = builder.convert(actual);
      FunctionCall call = resolver.resolveInstance(scope, list, name, arguments);
      
      if(call == null) {
         handler.handleCompileError(INVOKE, scope, actual, name, arguments);
      }
      return call.check(constraint, arguments);
   }

   @Override
   public Call2 dispatch(Scope scope, Value value, Object... arguments) throws Exception {
      Object object = value.getValue();
      List list = builder.convert(object);
      FunctionCall call = resolver.resolveInstance(scope, list, name, arguments);
      
      if(call == null) {
         handler.handleRuntimeError(INVOKE, scope, object, name, arguments);
      }
      return new Call2(call) {
         
         public Object invoke(Scope scope, Object source, Object... arguments) throws Exception{
            if(source instanceof Value) {
               source = ((Value)source).getValue();
            }
            source = builder.convert(source);
            return call.invoke(scope, source, arguments);
         }
      };
   }
}