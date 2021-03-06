package org.snapscript.tree.define;

import static org.snapscript.core.type.Category.OTHER;

import org.snapscript.core.function.Function;
import org.snapscript.core.function.FunctionBody;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.type.Category;
import org.snapscript.core.type.Type;
import org.snapscript.core.type.TypeState;
import org.snapscript.tree.compile.TypeScopeCompiler;
import org.snapscript.tree.constraint.GenericList;

public class FunctionBodyCompiler extends TypeState {

   private final TypeScopeCompiler compiler;
   private final FunctionBody body;
   
   public FunctionBodyCompiler(GenericList generics, FunctionBody body) {
      this.compiler = new TypeScopeCompiler(generics);
      this.body = body;
   }

   @Override
   public Category define(Scope scope, Type type) throws Exception {
      Scope outer = compiler.define(scope, type);

      body.define(outer);
      return OTHER;
   }

   @Override
   public void compile(Scope scope, Type type) throws Exception {
      Function function = body.create(scope);
      Scope outer = compiler.compile(scope, type, function);

      body.compile(outer);
   }
}
