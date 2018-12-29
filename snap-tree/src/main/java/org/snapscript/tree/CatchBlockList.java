package org.snapscript.tree;

import static org.snapscript.core.result.Result.NORMAL;

import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.Execution;
import org.snapscript.core.Statement;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.convert.TypeInspector;
import org.snapscript.core.error.ErrorCauseExtractor;
import org.snapscript.core.function.Parameter;
import org.snapscript.core.result.Result;
import org.snapscript.core.scope.Scope;
import org.snapscript.core.scope.index.Address;
import org.snapscript.core.scope.index.ScopeIndex;
import org.snapscript.core.scope.index.Local;
import org.snapscript.core.scope.index.ScopeTable;
import org.snapscript.core.type.Type;
import org.snapscript.tree.function.ParameterDeclaration;

public class CatchBlockList {
   
   private final AtomicReference<Address> location;
   private final ErrorCauseExtractor extractor;
   private final TypeInspector inspector;
   private final CatchBlock[] blocks;
   private final Execution[] list;
   
   public CatchBlockList(CatchBlock... blocks) {
      this.location = new AtomicReference<Address>();
      this.extractor = new ErrorCauseExtractor();
      this.inspector = new TypeInspector();
      this.list = new Execution[blocks.length];
      this.blocks = blocks;
   }    
   
   public Result define(Scope scope) throws Exception {  
      for(int i = 0; i < blocks.length; i++){
         CatchBlock block = blocks[i];
         Statement statement = block.getStatement();
         
         if(statement != null) {
            ScopeIndex index = scope.getIndex();
            int size = index.size();
            
            try {
               ParameterDeclaration declaration = block.getDeclaration();
               Parameter parameter = declaration.get(scope, 0);
               String name = parameter.getName();
               Address address = index.index(name);
               
               location.set(address);
               statement.define(scope);
            }finally {
               index.reset(size);
            }
         }
      }
      return NORMAL;
   }
   
   public Result compile(Scope scope) throws Exception {  
      for(int i = 0; i < blocks.length; i++){
         CatchBlock block = blocks[i];
         Statement statement = block.getStatement();
         
         if(statement != null) {
            ParameterDeclaration declaration = block.getDeclaration();
            Parameter parameter = declaration.get(scope, 0);
            Constraint constraint = parameter.getConstraint();
            String name = parameter.getName();
            ScopeTable table = scope.getTable();
            Local local = Local.getConstant(null, name, constraint);
            Address address = location.get();
            
            table.addValue(address, local);
   
            list[i] = statement.compile(scope, null);
         }
      }
      return NORMAL;
   }

   public Result execute(Scope scope, Result result) throws Exception {
      Object data = result.getValue();
      
      for(int i = 0; i < blocks.length; i++){
         CatchBlock block = blocks[i];
         ParameterDeclaration declaration = block.getDeclaration();
         Parameter parameter = declaration.get(scope, 0);
         Constraint constraint = parameter.getConstraint();
         Type type = constraint.getType(scope);
         String name = parameter.getName();

         if(data != null) {
            Object cause = extractor.extract(scope, data);
            
            if(inspector.isCompatible(type, cause)) {
               ScopeTable table = scope.getTable();
               Local local = Local.getConstant(cause, name);
               Address address = location.get();
               
               table.addValue(address, local);

               return list[i].execute(scope);
            }
         }
      }
      return result;
   }
}