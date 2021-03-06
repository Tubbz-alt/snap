package org.snapscript.core;

import org.snapscript.core.constraint.transform.ConstraintTransformer;
import org.snapscript.core.convert.ConstraintMatcher;
import org.snapscript.core.convert.proxy.ProxyWrapper;
import org.snapscript.core.error.ErrorHandler;
import org.snapscript.core.function.bind.FunctionBinder;
import org.snapscript.core.function.resolve.FunctionResolver;
import org.snapscript.core.link.PackageLinker;
import org.snapscript.core.module.ModuleRegistry;
import org.snapscript.core.platform.PlatformProvider;
import org.snapscript.core.resume.TaskScheduler;
import org.snapscript.core.stack.ThreadStack;
import org.snapscript.core.trace.TraceInterceptor;
import org.snapscript.core.type.TypeExtractor;
import org.snapscript.core.type.TypeLoader;

public interface Context extends Any {
   ThreadStack getStack();
   ErrorHandler getHandler();
   TaskScheduler getScheduler();
   TypeExtractor getExtractor();
   ResourceManager getManager();
   ModuleRegistry getRegistry();
   ConstraintMatcher getMatcher();
   ContextValidator getValidator();
   TraceInterceptor getInterceptor();
   ExpressionEvaluator getEvaluator();
   ConstraintTransformer getTransformer();
   FunctionResolver getResolver();
   PlatformProvider getProvider();
   PackageLinker getLinker();
   ProxyWrapper getWrapper();
   FunctionBinder getBinder();
   TypeLoader getLoader();

   
}