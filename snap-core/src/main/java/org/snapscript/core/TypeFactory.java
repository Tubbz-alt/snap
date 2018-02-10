package org.snapscript.core;

public abstract class TypeFactory {

   public void compile(Scope scope, Type type) throws Exception {} // static stuff
   public void validate(Scope scope, Type type) throws Exception {}; // static stuff

   public Result execute(Scope scope, Type type) throws Exception { // instance stuff
      return Result.getNormal();
   }
}