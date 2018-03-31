package org.snapscript.core.platform;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.TypeExtractor;
import org.snapscript.core.convert.ProxyWrapper;
import org.snapscript.core.function.find.FunctionResolver;
import org.snapscript.core.stack.ThreadStack;

public class PlatformBuilder {
   
   private final AtomicReference<Platform> reference;
   private final PlatformClassLoader loader;
   private final FunctionResolver resolver;
   private final ProxyWrapper wrapper;
   private final Platform partial;
   
   public PlatformBuilder(TypeExtractor extractor, ProxyWrapper wrapper, ThreadStack stack) {
      this.resolver = new FunctionResolver(extractor, stack);
      this.loader = new PlatformClassLoader();
      this.partial = new PartialPlatform();
      this.reference = new AtomicReference<Platform>();
      this.wrapper = wrapper;
   }

   public synchronized Platform create() {
      Platform platform = reference.get(); // make sure only one created
      
      if(platform == null) {
         try {
            Constructor constructor = loader.loadConstructor();
            Object instance = constructor.newInstance(resolver, wrapper);
            
            reference.set((Platform)instance);
         }catch(Exception e) {
            reference.set(partial); 
         }
      }
      return reference.get();
   }
}