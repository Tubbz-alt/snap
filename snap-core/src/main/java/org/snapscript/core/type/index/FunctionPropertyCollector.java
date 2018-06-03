package org.snapscript.core.type.index;

import static org.snapscript.core.Reserved.PROPERTY_GET;
import static org.snapscript.core.Reserved.PROPERTY_IS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.snapscript.core.function.Function;
import org.snapscript.core.function.Parameter;
import org.snapscript.core.function.Signature;
import org.snapscript.core.property.Property;

public class FunctionPropertyCollector {

   private static final String[] PREFIXES = { PROPERTY_GET, PROPERTY_IS };
   
   private final FunctionPropertyBuilder builder;
   
   public FunctionPropertyCollector() {
      this.builder = new FunctionPropertyBuilder();
   }
   
   public List<Property> collect(List<Function> functions, Set<String> ignore) throws Exception {
      List<Property> properties = new ArrayList<Property>();
      Set<String> done = new HashSet<String>(ignore);
      
      for(Function function : functions) {
         Signature signature = function.getSignature();
         List<Parameter> names = signature.getParameters();
         int count = names.size();
         
         if(count == 0) {
            String name = extract(function);
   
            if(done.add(name)) {
               Property property = builder.create(function, name);
               
               if(property != null) {
                  properties.add(property);
               }
            }
         }
      }
      return properties;
   }
   
   private String extract(Function function) throws Exception {
      String name = function.getName();
      
      for(String prefix : PREFIXES) {
         String property = PropertyNameExtractor.getProperty(name, prefix);
      
         if(property != null) {
            return property;
         }
      }
      return name;
   }
}