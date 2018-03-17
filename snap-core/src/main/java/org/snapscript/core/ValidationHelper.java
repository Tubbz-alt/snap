package org.snapscript.core;

import java.util.List;

import org.snapscript.core.function.Function;
import org.snapscript.core.function.Parameter;
import org.snapscript.core.function.Signature;
import org.snapscript.core.property.Property;

public class ValidationHelper {
   
   public static Scope create(Type type, Function function) {
      Signature signature = function.getSignature();
      Scope scope = type.getScope();
      
      try {
         Scope outer = scope.getStack();
         State state = outer.getState();
         Table table = outer.getTable();
         List<Parameter> parameters = signature.getParameters();
         int count = parameters.size();
         
         for(int i = 0; i < count; i++) {
            Parameter parameter = parameters.get(i);
            String name = parameter.getName();
            Constraint constraint = parameter.getType();
            Type result = constraint.getType(scope);
            Local local = Local.getReference(null, name, result);
            
            state.add(name, local);
            table.add(i, local);
         }
         List<Property> properties = type.getProperties();
         Value value = Value.getTransient(outer, type);
         
         for(Property property : properties) {
            String name = property.getName();
            
            if(!name.equals(Reserved.TYPE_THIS)) {
               Constraint constraint = property.getConstraint();
               Type result = constraint.getType(scope);
               Value field = Value.getReference(null, result);
               Value current = state.get(name);
               
               if(current == null) {
                  state.add(name, field);
               }
            }
         }
         state.add(Reserved.TYPE_THIS, value);
         return outer;
      }catch(Exception e) {
         e.printStackTrace();
         return null;
      }
   }
}