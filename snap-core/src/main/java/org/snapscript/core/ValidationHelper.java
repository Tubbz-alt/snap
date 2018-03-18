package org.snapscript.core;

import static org.snapscript.core.Reserved.TYPE_THIS;

import java.util.List;
import java.util.Set;

import org.snapscript.core.function.Function;
import org.snapscript.core.function.Parameter;
import org.snapscript.core.function.Signature;
import org.snapscript.core.property.Property;

/** we should cache the member data so we can fill the state quickly */
@Bug("wtf???")
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
         Value value = Value.getTransient(outer, type);
         Set<Type> types = scope.getModule().getContext().getExtractor().getTypes(type);
         //extract(type, outer, state);
         for(Type t:types){
            extract(t, outer, state);
         }
         state.add(TYPE_THIS, value);
         return outer;
      }catch(Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   private static void extract(Type type, Scope outer, State state) {
      List<Property> properties = type.getProperties();
      
      for(Property property : properties) {
         String name = property.getName();
         
         if(!name.equals(TYPE_THIS)) {
            Constraint constraint = property.getConstraint();
            Type result = constraint.getType(outer);
            Value field = Value.getReference(null, result);
            Value current = state.get(name);
            
            if(current == null) {
               state.add(name, field);
            }
         }
      }
   }
}
