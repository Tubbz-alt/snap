package org.snapscript.core.scope;

import org.snapscript.core.Handle;
import org.snapscript.core.module.Module;
import org.snapscript.core.scope.index.Index;
import org.snapscript.core.scope.index.Table;
import org.snapscript.core.type.Type;
import org.snapscript.core.variable.Value;

public interface Scope extends Handle {
   Type getType();
   Scope getStack(); // extend on current scope
   Scope getScope(); // get callers scope
   Module getModule();  
   Index getIndex();
   Table getTable(); 
   State getState();   
   Value getThis();
}