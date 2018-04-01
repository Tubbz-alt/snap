package org.snapscript.core.error;

import static org.snapscript.core.result.Result.NORMAL;

import org.snapscript.core.type.Type;
import org.snapscript.core.module.Module;
import org.snapscript.core.result.Result;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.stack.ThreadStack;
import org.snapscript.core.trace.Trace;
import org.snapscript.core.type.TypeExtractor;

public class InternalErrorHandler {

   private final InternalErrorFormatter formatter;
   private final InternalErrorBuilder builder;
   
   public InternalErrorHandler(TypeExtractor extractor, ThreadStack stack) {
      this(extractor, stack, true);
   }
   
   public InternalErrorHandler(TypeExtractor extractor, ThreadStack stack, boolean replace) {
      this.builder = new InternalErrorBuilder(stack, replace);
      this.formatter = new InternalErrorFormatter(extractor);
   }
   
   public Result handleInternalError(Scope scope, Object value) {
      throw builder.createError(value);
   }
   
   public Result handleInternalError(Scope scope, Throwable cause, Trace trace) {
      String message = formatter.format(cause, trace);
      throw builder.createError(message);
   }

   public Result handleCompileError(Scope scope, String name, Type... list) {
      String message = formatter.format(name, list);
      throw builder.createException(message);
   }
   
   public Result handleCompileError(Scope scope, Type type, String name, Type... list) {
      String message = formatter.format(type, name, list);
      throw builder.createException(message);
   }
   
   public Result handleRuntimeError(Scope scope, String name, Object... list) {
      String message = formatter.format(name, list);
      throw builder.createException(message);
   }
   
   public Result handleRuntimeError(Scope scope, Object value, String name, Object... list) {
      String message = formatter.format(value, name, list);
      throw builder.createException(message);
   }
   
   public Result handleRuntimeError(Scope scope, Type type, String name, Object... list) {
      String message = formatter.format(type, name, list);
      throw builder.createException(message);
   }
   
   public Result handleRuntimeError(Scope scope, Module module, String name, Object... list) {
      String message = formatter.format(module, name, list);
      throw builder.createException(message);
   }
}