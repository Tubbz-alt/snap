package org.snapscript.core.dispatch;

import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.constraint.Constraint;

public interface CallDispatcher<T> {
   Constraint compile(Scope scope, Type object, Type... arguments) throws Exception;
   Value dispatch(Scope scope, T object, Object... arguments) throws Exception;
}