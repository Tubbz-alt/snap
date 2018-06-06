package org.snapscript.core.link;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;

import org.snapscript.core.Context;
import org.snapscript.core.NameFormatter;
import org.snapscript.core.error.InternalStateException;
import org.snapscript.core.module.Module;
import org.snapscript.core.module.ModuleRegistry;
import org.snapscript.core.module.Path;
import org.snapscript.core.type.Type;
import org.snapscript.core.type.TypeLoader;

public class ImportManager {

   private final Map<String, Type> generics;
   private final Map<String, String> aliases;
   private final Set<String> imports;
   private final NameFormatter formatter;
   private final ImportMatcher matcher;
   private final Module parent;
   private final String from;
   private final String local;
   
   public ImportManager(Module parent, Executor executor, Path path, String from, String local) {
      this.generics = new ConcurrentHashMap<String, Type>();
      this.aliases = new ConcurrentHashMap<String, String>();
      this.imports = new CopyOnWriteArraySet<String>();
      this.matcher = new ImportMatcher(parent, executor, path, from);
      this.formatter = new NameFormatter();
      this.parent = parent;
      this.local = local;
      this.from = from;
   }
   
   public void addImport(String prefix) {
      imports.add(prefix);
   }
   
   public void addImport(String type, String alias) {
      aliases.put(alias, type);
   }
   
   public void addImport(Type type, String alias) {
      generics.put(alias, type);
   }
   
   public void addImports(Module module) {
      ImportManager manager = module.getManager();

      if(manager != null) {
         manager.aliases.putAll(aliases);
         manager.imports.addAll(imports);
      }
   }
   
   public Module getModule(String name) {
      try {
         String alias = formatter.formatLocalName(name);
         char first = alias.charAt(0);
         
         if(!Character.isUpperCase(first)) {
            return null;
         }
         if(alias.equals(local)) {
            return parent;
         }
         return getModule(name, true);
      } catch(Exception e){
         throw new InternalStateException("Could not find '" + name + "' in '" + from + "'", e);
      }
   }
   
   private Module getModule(String name, boolean load) {
      try {
         Context context = parent.getContext();
         ModuleRegistry registry = context.getRegistry();
         Module module = registry.getModule(name);

         if(module == null) {
            String alias = aliases.get(name);
            
            if(alias != null) {
               module = registry.getModule(alias);
            }
         }
         if(module == null) {
            Type type = getType(name, load); // do a quick check
            
            if(type == null && load) {
               module = matcher.importModule(imports, name);
            }
         }
         return module;
      } catch(Exception e){
         throw new InternalStateException("Could not find '" + name + "' in '" + from + "'", e);
      }
   }
   
   public Type getType(String name) {
      try {
         String alias = formatter.formatLocalName(name);
         char first = alias.charAt(0);
         
         if(!Character.isUpperCase(first)) {
            return null;
         }
         if(alias.equals(local)) {
            return null;
         }
         Type type = getType(alias, true);
        
         if(!name.equals(alias)) {
            return getType(name, false);
         }         
         return type;
      } catch(Exception e){
         throw new InternalStateException("Could not find '" + name + "' in '" + from + "'", e);
      }
   }
   
   private Type getType(String name, boolean load) {
      try {
         Context context = parent.getContext();
         TypeLoader loader = context.getLoader();
         ModuleRegistry registry = context.getRegistry();
         Type type = generics.get(name);
         
         if(type == null) {
            String alias = aliases.get(name); // fully qualified "tetris.game.Block"
            
            if(alias != null) {
               Module module = registry.getModule(alias);
               
               if(module != null) {
                  return null; // its a module!!
               }
               type = loader.loadType(alias);
            }
         }
         if(type == null) {
            type = loader.loadType(from, name);
         }
         if(type == null) {
            for(String module : imports) {
               type = loader.loadType(module, name); // this is "tetris.game.*"
               
               if(type != null) {
                  return type;
               }
            }
            type = loader.loadType(null, name); // null is "java.*"
         }
         if(type == null && load) {
            type = matcher.importType(imports, name);
         }         
         return type;
      } catch(Exception e){
         throw new InternalStateException("Could not find '" + name + "' in '" + from + "'", e);
      }
   }

}