package org.snapscript.core.constraint.transform;

import java.util.List;
import java.util.Map;

import org.snapscript.core.constraint.Constraint;
import org.snapscript.core.constraint.ConstraintMapper;
import org.snapscript.core.error.InternalStateException;
import org.snapscript.core.scope.Scope;

public class PositionMapper {

   private final Map<String, Integer> positions;
   private final ConstraintMapper mapper;
   private final ConstraintSource source;
   
   public PositionMapper(ConstraintSource source, Map<String, Integer> positions) {
      this.mapper = new ConstraintMapper();
      this.positions = positions;
      this.source = source;
   }

   public Constraint resolve(Scope scope, Constraint constraint, String name){
      Integer position = positions.get(name);

      if(position != null) {
         List<Constraint> constraints = source.getConstraints(constraint);
         int count = constraints.size();

         if(position >= count) {
            throw new InternalStateException("No generic parameter at " + position +" for " + source);
         }
         Constraint result = constraints.get(position);

         if(result == null) {
            throw new InternalStateException("No generic parameter at " + position +" for " + source);
         }
         return mapper.map(scope, result);
      }
      return mapper.map(scope, name);
   }
}
