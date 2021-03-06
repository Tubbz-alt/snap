package org.snapscript.core.scope;

import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.variable.Value;

public interface ScopeState extends Iterable<String> {
   Value getValue(String name);
   Constraint getConstraint(String name);
   void addValue(String name, Value value);
   void addConstraint(String name, Constraint constraint);
}