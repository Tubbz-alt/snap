/*
 * Calculator.java December 2016
 *
 * Copyright (C) 2016, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.snapscript.tree.operation;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Evaluation;

public class Calculator {
   
   private final CalculatorStack<CalculationPart> tokens;
   private final CalculatorStack<Evaluation> variables;      
   private final List<CalculationPart> order;
   
   public Calculator() {
      this.tokens = new CalculatorStack<CalculationPart>();      
      this.variables = new CalculatorStack<Evaluation>();
      this.order = new ArrayList<CalculationPart>();
   }
   
   public Evaluation create(){
      while(!tokens.isEmpty()) {
         CalculationPart top = tokens.pop();
         
         if(top != null) {
            order.add(top);
         }
      }      
      for(CalculationPart token : order) {
         NumericOperator operator = token.getOperator();
   
         if(operator != null) {
            Evaluation right = variables.pop();
            Evaluation left = variables.pop();
            
            if(left != null && right != null) {             
               Evaluation evaluation = new CalculationOperation(operator, left, right);
               
               variables.push(evaluation);
            }                          
         } else {
            Evaluation evaluation = token.getEvaluation();
            
            if(token != null) {
               variables.push(evaluation);
            }
         }               
      }
      return variables.pop();
   }
   
   public void update(CalculationPart part) {
      NumericOperator operator = part.getOperator();
      
      if(operator != null) {
         while(!tokens.isEmpty()) {
            CalculationPart top = tokens.pop();
            NumericOperator other = top.getOperator();
                                 
            if(other.priority < operator.priority) {
               tokens.push(top);
               break;
            } else {
               order.add(top);
            }            
         }
         tokens.push(part);
      } else {
         order.add(part);
      }
   }
}