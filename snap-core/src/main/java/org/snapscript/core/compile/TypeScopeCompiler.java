package org.snapscript.core.compile;

import static org.snapscript.core.Reserved.TYPE_THIS;

import java.util.Set;

import org.snapscript.core.Context;
import org.snapscript.core.module.Module;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.scope.State;
import org.snapscript.core.scope.Value;
import org.snapscript.core.type.Type;
import org.snapscript.core.type.TypeExtractor;
import org.snapscript.core.function.Function;

public class TypeScopeCompiler extends ScopeCompiler{
   
   public TypeScopeCompiler() {
      super();
   }

   public Scope compile(Scope scope, Type type, Function function) throws Exception {          
      Scope outer = type.getScope();
      Scope inner = outer.getStack();

      compileParameters(inner, function);
      compileThis(inner, type);

      return inner;
   }
   
   protected void compileThis(Scope scope, Type type) {
      Value value = Value.getConstant(scope, type);
      Module module = scope.getModule();
      Context context = module.getContext();
      TypeExtractor extractor = context.getExtractor();      
      Set<Type> types = extractor.getTypes(type);      
      State state = scope.getState();
      
      for(Type base : types){
         compileProperties(scope, base);
      }
      state.add(TYPE_THIS, value);
   }
}