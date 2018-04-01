package org.snapscript.core;

public abstract class Statement {
   
   public void create(Scope scope) throws Exception {}
   
   public boolean define(Scope scope) throws Exception {
      return true; // executable?
   }
   
   public abstract Execution compile(Scope scope) throws Exception;
}