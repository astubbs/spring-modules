/*
 * Copyright 2006 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.xt.model.specifications.composite.operator;

/**
 * Singleton factory for creating shared, <i>flyweight</i>, {@link BinaryOperator} objects.<br>
 * Please use this class for obtaining {@link BinaryOperator} objects, rather than directly constructing them.
 *
 * @author Sergio Bossa
 */
public class OperatorFactory {
    
   private static final OperatorFactory factory = new OperatorFactory();
   
   private BinaryOperator andOperator = new AndOperator();
   private BinaryOperator orOperator = new OrOperator();
   
   private OperatorFactory() {}
    
   public static OperatorFactory getInstance() {
       return factory;
   }
   
   /**
    * @return A {@link BinaryOperator} implementing the logical <code>AND</code> operator.
    */
   public BinaryOperator getAndOperator() {
       return this.andOperator;
   }
   
   /**
    * @return A {@link BinaryOperator} implementing the logical <code>OR</code> operator.
    */
   public BinaryOperator getOrOperator() {
       return this.orOperator;
   }
}
