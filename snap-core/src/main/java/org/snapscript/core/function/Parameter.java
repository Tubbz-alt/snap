package org.snapscript.core.function;

import static org.snapscript.core.scope.index.AddressType.LOCAL;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.annotation.Annotation;
import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.scope.index.Address;

public class Parameter {
   
   private final List<Annotation> annotations;
   private final Constraint constraint;
   private final Address address;
   private final String name;
   private final boolean constant;
   private final boolean variable;
   
   public Parameter(String name, Constraint constraint, int index, boolean constant){
      this(name, constraint, index, constant, false);
   }
   
   public Parameter(String name, Constraint constraint, int index, boolean constant, boolean variable){
      this.address = new Address(LOCAL, name, index);
      this.annotations = new ArrayList<Annotation>();
      this.constraint = constraint;
      this.variable = variable;
      this.constant = constant;
      this.name = name;
   }
   
   public Parameter getParameter(int position) {
      int index = address.getOffset();
      
      if(position != index) {
         return new Parameter(name, constraint, position, constant, variable);
      }
      return this;
   }
   
   public List<Annotation> getAnnotations() {
      return annotations;
   }
   
   public Constraint getConstraint() {
      return constraint;
   }
   
   public Address getAddress() {
      return address;
   }
   
   public boolean isConstant() {
      return constant;
   }
   
   public boolean isVariable() { // var-arg ... 
      return variable;
   }
   
   public String getName() {
      return name;
   }
}