package org.snapscript.tree.define;

import org.snapscript.core.function.Invocation;
import org.snapscript.core.function.InvocationBuilder;
import org.snapscript.core.function.Signature;
import org.snapscript.core.function.SignatureAligner;
import org.snapscript.core.result.Result;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.scope.instance.Instance;
import org.snapscript.core.type.Type;
import org.snapscript.core.type.TypeState;
import org.snapscript.tree.function.ParameterExtractor;

public class TypeInvocationBuilder implements InvocationBuilder {
   
   private ParameterExtractor extractor;
   private SignatureAligner aligner;
   private Invocation invocation;
   private TypeState state;
   private Type type;

   public TypeInvocationBuilder(TypeState state, Signature signature, Type type) {
      this.extractor = new ParameterExtractor(signature); // this seems wrong!
      this.aligner = new SignatureAligner(signature);
      this.state = state;
      this.type = type;
   }
   
   @Override
   public void define(Scope scope) throws Exception {
      extractor.define(scope); // count parameters
      state.define(scope, type); // start counting from here 
   }
   
   @Override
   public void compile(Scope scope) throws Exception {
      state.compile(scope, type);
   }

   @Override
   public Invocation create(Scope scope) throws Exception {
      if(invocation == null) {
         try {
            invocation = new TypeStateInvocation(state);
         } finally {
            state.allocate(scope, type);
         }
      }
      return invocation;
   }

   private class TypeStateInvocation implements Invocation<Instance> {
      
      private final TypeState state;
      
      public TypeStateInvocation(TypeState state) {
         this.state = state;
      }

      @Override
      public Object invoke(Scope scope, Instance object, Object... list) throws Exception {
         Type real = (Type)list[0];
         Object[] arguments = aligner.align(list);
         Scope inner = extractor.extract(object, arguments);
         Result result = state.execute(inner, real);
         
         return result.getValue();
      }
   }
}