package org.snapscript.tree.annotation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.snapscript.core.Evaluation;
import org.snapscript.core.annotation.Annotation;
import org.snapscript.core.annotation.MapAnnotation;
import org.snapscript.core.error.InternalStateException;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.variable.Value;
import org.snapscript.tree.construct.MapEntryData;

public class AnnotationDeclaration extends Evaluation {

   private AnnotationName name;
   private MapEntryData entries;
   private Value value;
   
   public AnnotationDeclaration(AnnotationName name) {
      this(name, null);
   }
   
   public AnnotationDeclaration(AnnotationName name, MapEntryData entries) {
      this.entries = entries;
      this.name = name;
   }

   @Override
   public Value evaluate(Scope scope, Value left) throws Exception {
      if(value == null) {
         Annotation annotation = create(scope, left);
         
         if(annotation == null) {
            throw new InternalStateException("Could not create annotation");
         }
         value = Value.getTransient(annotation);
      }
      return value;
   }
   
   private Annotation create(Scope scope, Value left) throws Exception {
      Map<String, Object> attributes = new LinkedHashMap<String, Object>();
      
      if(entries != null) {
         Value value = entries.evaluate(scope, left);
         Map<Object, Object> map = value.getValue();
         Set<Object> keys = map.keySet();
         
         for(Object key : keys) {
            String name = String.valueOf(key);
            Object attribute = map.get(name);
            
            attributes.put(name, attribute);
         }
      }
      Value value = name.evaluate(scope, left);
      String name = value.getString();
      
      return new MapAnnotation(name, attributes);
   }
}