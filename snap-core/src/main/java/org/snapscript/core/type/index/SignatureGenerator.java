package org.snapscript.core.type.index;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.module.Module;
import org.snapscript.core.type.Type;
import org.snapscript.core.type.annotation.Annotation;
import org.snapscript.core.type.annotation.AnnotationConverter;
import org.snapscript.core.error.InternalStateException;
import org.snapscript.core.function.FunctionSignature;
import org.snapscript.core.function.Parameter;
import org.snapscript.core.function.ParameterBuilder;
import org.snapscript.core.function.Signature;

public class SignatureGenerator {
   
   private final AnnotationConverter converter;
   private final ParameterBuilder builder;
   private final TypeIndexer indexer;
   
   public SignatureGenerator(TypeIndexer indexer) {
      this.converter = new AnnotationConverter();
      this.builder = new ParameterBuilder();
      this.indexer = indexer;
   }

   public Signature generate(Type type, Method method) {
      Class[] types = method.getParameterTypes();
      Object[][] annotations = method.getParameterAnnotations();
      Module module = type.getModule();
      boolean variable = method.isVarArgs();
      
      try {
         List<Parameter> parameters = new ArrayList<Parameter>();
   
         for(int i = 0; i < types.length; i++){
            boolean last = i + 1 == types.length;
            Type match = indexer.loadType(types[i]);
            Parameter parameter = builder.create(match, i, variable && last);
            Object[] list = annotations[i];
            
            if(list.length > 0) {
               List<Annotation> actual = parameter.getAnnotations();
               
               for(int j = 0; j < list.length; j++) {
                  Object value = list[j];
                  Object result = converter.convert(value);
                  Annotation annotation = (Annotation)result;
                  
                  actual.add(annotation);
               }
            }
            parameters.add(parameter);
         }
         return new FunctionSignature(parameters, module, method, true, variable);
      } catch(Exception e) {
         throw new InternalStateException("Could not create function for " + method, e);
      }
   }
   
   public Signature generate(Type type, Constructor constructor) {
      Class[] types = constructor.getParameterTypes();
      Object[][] annotations = constructor.getParameterAnnotations();
      Module module = type.getModule();
      boolean variable = constructor.isVarArgs();
      
      try {
         List<Parameter> parameters = new ArrayList<Parameter>();
   
         for(int i = 0; i < types.length; i++){
            boolean last = i + 1 == types.length;
            Type match = indexer.loadType(types[i]);
            Parameter parameter = builder.create(match, i, variable && last);
            Object[] list = annotations[i];
            
            if(list.length > 0) {
               List<Annotation> actual = parameter.getAnnotations();
               
               for(int j = 0; j < list.length; j++) {
                  Object value = list[j];
                  Object result = converter.convert(value);
                  Annotation annotation = (Annotation)result;
                  
                  actual.add(annotation);
               }
            }
            parameters.add(parameter);
         }
         return new FunctionSignature(parameters, module, constructor, true, variable);
      } catch(Exception e) {
         throw new InternalStateException("Could not create constructor for " + constructor, e);
      }
   }
}