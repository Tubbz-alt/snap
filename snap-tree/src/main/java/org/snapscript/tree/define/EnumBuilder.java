package org.snapscript.tree.define;

import static org.snapscript.core.constraint.Constraint.NONE;
import static org.snapscript.core.type.Category.ENUM;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Type;
import org.snapscript.core.module.Module;
import org.snapscript.core.property.ConstantPropertyBuilder;
import org.snapscript.core.property.Property;

public class EnumBuilder {

   private final AtomicReference<Type> reference;
   private final EnumConstantGenerator generator;
   private final ConstantPropertyBuilder builder;
   private final TypeHierarchy hierarchy;
   private final TypeName name;
   private final List values;
   
   public EnumBuilder(TypeName name, TypeHierarchy hierarchy) {
      this.reference = new AtomicReference<Type>();
      this.generator = new EnumConstantGenerator();
      this.builder = new ConstantPropertyBuilder();
      this.values = new ArrayList();
      this.hierarchy = hierarchy;
      this.name = name;
   }
   
   public Type create(Scope outer) throws Exception {
      Module module = outer.getModule();
      String alias = name.getName(outer);
      Type type = module.addType(alias, ENUM);
      
      reference.set(type);
      
      return type;
   }
   
   public Type define(Scope outer) throws Exception {
      Type type = reference.get();
      Type enclosing = outer.getType();
      Scope scope = type.getScope();
      
      if(enclosing != null) {
         String name = type.getName();
         String prefix = enclosing.getName();
         String key = name.replace(prefix + '$', ""); // get the class name
         Property property = builder.createConstant(key, type, enclosing, NONE);
         List<Property> properties = enclosing.getProperties();
         
         properties.add(property);
      }
      generator.generate(scope, type, values);
      hierarchy.extend(scope, type); // this may throw exception if missing type
      
      return type;
   }
   
   public Type compile(Scope outer) throws Exception {
      return reference.get();
   }
}