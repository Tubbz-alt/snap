package org.snapscript.core.bridge;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.Any;
import org.snapscript.core.ContextClassLoader;
import org.snapscript.core.Type;
import org.snapscript.core.bind.FunctionResolver;

public class PlatformClassLoader {
   
   private final AtomicReference<Constructor> reference;
   private final ClassLoader loader;
   private final Class[] types;
   
   public PlatformClassLoader() {
      this.types = new Class[]{FunctionResolver.class, Type.class};
      this.reference = new AtomicReference<Constructor>();
      this.loader = new ContextClassLoader(Any.class);
   }

   public Constructor loadConstructor(){
      Constructor constructor = reference.get();
      
      if(constructor == null) {
         try {
            Platform platform = Platform.resolvePlatform();
            Class value = loader.loadClass(platform.type);
            
            constructor = value.getDeclaredConstructor(types);
            reference.set(constructor);
         }catch(Exception e) {
            throw new IllegalStateException("Could not load constructor", e);
         }
      }
      return constructor;
   }
}