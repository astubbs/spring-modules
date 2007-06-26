/*
 * Copyright 2006 - 2007 the original author or authors.
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

package org.springmodules.xt.model.generator.factory;

import java.lang.reflect.Proxy;
import org.springmodules.xt.model.generator.DynamicGenerator;

/**
 * <p>
 * Dynamically generate factory objects.<br>
 * For dynamically generating a factory object you simply have to provide 
 * an interface with a number of setter methods, representing constructor arguments
 * and properties to use for constructing the <i>product object</i>, and a factory method
 * returning the class (or superclass) of that product object.
 * </p>
 * <p>
 * More specifically, the factory interface supports the following methods, that must obey the following rules:
 * <ul>
 *      <li><b>Setter</b> methods must be annotated either with:
 *          <ul>
 *          <li>The {@link org.springmodules.xt.model.generator.annotation.ConstructorArg} annotation if they are constructor arguments 
 *          (plus the optional {@link org.springmodules.xt.model.generator.annotation.ConstructorArgType} annotation).</li>
 *          <li>The {@link org.springmodules.xt.model.generator.annotation.Property} annotation if they are properties to be set.</li>
 *          <li>The {@link org.springmodules.xt.model.generator.annotation.Value} annotation if they are just simple values to use later for other purposes.</li>
 *          </ul>
 *      </li>
 *      <li>The <b>factory</b> method must be annotated with the {@link org.springmodules.xt.model.generator.annotation.FactoryMethod}
 *          annotation.</li>
 *      <li>Moreover, there can be <b>getter</b> methods corresponding to the setter methods above.</li>
 * </ul>
 * Any other method, if called, will throw an exception.
 * </p>
 * <p>
 * Once generated, the factory can be used by calling the setter methods, for initializing object constructor arguments and properties,
 * and finally the factory method.
 * </p>
 * <p>
 * Please <b>note</b> that this generator generates a new factory object for every call to its {@link #generate()} method, and the factory
 * generates a new product object for every call to its factory method.
 * </p>
 * <p>
 * <b>Restrictions</b>: The only restriction is that the getter methods of your factory cannot return primitive values; so, you
 * have to use wrapper objects instead when you need to return a primitive type.
 * </p>
 * <p>This class is <b>thread-safe</b>.</p>
 *
 * @param F The type of the dynamically generated factory object.
 * @param P The type of the product object created by the factory.
 *
 * @author Sergio Bossa
 */
public class DynamicFactoryGenerator<F, P> implements DynamicGenerator<F> {
    
    private Class<F> factoryClass;
    private Class<P> productClass;
    
    /**
     * Constructor.
     * @param factoryClass The class of the dynamically generated factory object: it must be an interface.
     * @param productClass The class of the object created by the factory: the type returned by the factory method in the
     * factory object must be the same, or a super-type, of this product class.
     */
    public DynamicFactoryGenerator(Class<F> factoryClass, Class<P> productClass) {
        if (! factoryClass.isInterface()) {
            throw new IllegalArgumentException("The factory class must be an interface!");
        }
        else {
            this.factoryClass = factoryClass;
            this.productClass = productClass;
        }
    }
    
    /**
     * Generate a new factory.
     */
    public F generate() {
        FactoryGeneratorInterceptor interceptor = new FactoryGeneratorInterceptor(this.productClass);
        F factory = (F) Proxy.newProxyInstance(this.factoryClass.getClassLoader(), new Class[]{this.factoryClass}, interceptor);
        return factory; 
    }
}
