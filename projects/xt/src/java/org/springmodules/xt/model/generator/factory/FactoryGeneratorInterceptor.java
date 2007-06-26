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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.springframework.aop.support.AopUtils;
import org.springmodules.xt.model.generator.annotation.ConstructorArg;
import org.springmodules.xt.model.generator.annotation.ConstructorArgType;
import org.springmodules.xt.model.generator.annotation.FactoryMethod;
import org.springmodules.xt.model.generator.annotation.Property;
import org.springmodules.xt.model.generator.annotation.Value;
import org.springmodules.xt.model.generator.support.IllegalArgumentPositionException;
import org.springmodules.xt.model.generator.support.ObjectConstructionException;
import org.springmodules.xt.model.generator.support.ReturnTypeMismatchException;
import org.springmodules.xt.model.introductor.support.IllegalReturnTypeException;

/**
 * Java dynamic Proxy-based interceptor for implementing factory generation.
 *
 * @see org.springmodules.xt.model.generator.factory.DynamicFactoryGenerator
 *
 * @author Sergio Bossa
 */
public class FactoryGeneratorInterceptor implements InvocationHandler {
    
    private static final Logger logger = Logger.getLogger(FactoryGeneratorInterceptor.class);
    
    private Class productClass;
    
    // Needs to be ordered by argument position:
    private TreeMap<Integer, ConstructorArgPair> constructorArgs = new TreeMap<Integer, ConstructorArgPair>();
    // No need to be ordered:
    private HashMap<String, PropertyPair> properties = new HashMap<String, PropertyPair>();
    // General values map, holding both constructor args and properties values:
    private HashMap<String, Object> values = new HashMap<String, Object>();
    
    /**
     * Constructor.
     *
     * @param productClass The factory product class, that is, the class of the object created by the factory.
     */
    public FactoryGeneratorInterceptor(Class productClass) {
        this.productClass = productClass;
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (this.isConstructorArg(method)) {
            return this.putConstructorArg(args, method);
        } else if (this.isPropertySetter(method)) {
            return this.putProperty(args, method);
        } else if (this.isValueSetter(method)) {
            return this.putValue(args, method);
        } else if (this.isGetter(method)) {
            return this.readProperty(args, method);
        } else if (this.isFactoryMethod(method)) {
            Class returnType = method.getReturnType();
            if (!returnType.isAssignableFrom(this.productClass)) {
                throw new ReturnTypeMismatchException("Return type mismatch. Expected assignable from: " + this.productClass + ", found: " + returnType);
            } else {
                Object product = this.make();
                // Fill properties:
                for (Map.Entry<String, PropertyPair> entry : this.properties.entrySet()) {
                    String propertyName = entry.getKey();
                    Object propertyValue = entry.getValue().getValue();
                    Property.AccessType propertyAccess = entry.getValue().getAccess();
                    if (propertyAccess.equals(Property.AccessType.FIELD)) {
                        Field field = this.productClass.getDeclaredField(propertyName);
                        field.setAccessible(true);
                        field.set(product, propertyValue);
                    } else {
                        String methodName = new StringBuilder("set").append(StringUtils.capitalize(propertyName)).toString();
                        Method methodObj = this.productClass.getMethod(methodName, propertyValue.getClass());
                        methodObj.invoke(product, propertyValue);
                    }
                }
                // Return product:
                return product;
            }
        } else {
            if (AopUtils.isEqualsMethod(method)) {
                return this.doEquals(proxy, args[0]);
            } else if (AopUtils.isHashCodeMethod(method)) {
                return this.doHashCode(proxy);
            } else if (AopUtils.isToStringMethod(method)) {
                return this.doToString(proxy);
            } else {
                // Fail fast:
                throw new UnsupportedOperationException("Unsupported method called: " + method.getName());
            }
        }
    }
    
    private boolean isConstructorArg(Method method) {
        if (method.getName().startsWith("set") && method.isAnnotationPresent(ConstructorArg.class)) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean isPropertySetter(Method method) {
        if (method.getName().startsWith("set") && method.isAnnotationPresent(Property.class)) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean isValueSetter(Method method) {
        if (method.getName().startsWith("set") && method.isAnnotationPresent(Value.class)) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean isGetter(Method method) {
        if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean isFactoryMethod(Method method) {
        if (method.isAnnotationPresent(FactoryMethod.class)) {
            return true;
        } else {
            return false;
        }
    }
    
    private Object putProperty(final Object[] args, final Method method) {
        if (args.length != 1) {
            throw new IllegalStateException("The setter method " + method.getName() + " must have only one argument!");
        } else {
            String name = StringUtils.uncapitalize(method.getName().substring(3));
            Property annotation = method.getAnnotation(Property.class);
            Property.AccessType access = annotation.access();
            PropertyPair pair = new PropertyPair();
            pair.setValue(args[0]);
            pair.setAccess(access);
            this.properties.put(name, pair);
            this.values.put(name, args[0]);
            logger.debug(new StringBuilder("Put property with name and value: ").append(name).append(",").append(args[0]));
            return null;
        }
    }
    
    private Object putValue(final Object[] args, final Method method) {
        if (args.length != 1) {
            throw new IllegalStateException("The setter method " + method.getName() + " must have only one argument!");
        } else {
            String name = StringUtils.uncapitalize(method.getName().substring(3));
            this.values.put(name, args[0]);
            logger.debug(new StringBuilder("Put value property with name and value: ").append(name).append(",").append(args[0]));
            return null;
        }
    }
    
    private Object putConstructorArg(final Object[] args, final Method method) {
        if (args.length != 1) {
            throw new IllegalStateException("The setter method " + method.getName() + " must have only one argument!");
        } else {
            String name = StringUtils.uncapitalize(method.getName().substring(3));
            ConstructorArg annotation = method.getAnnotation(ConstructorArg.class);
            int position = annotation.position();
            Class type = null;
            if (method.isAnnotationPresent(ConstructorArgType.class)) {
                type = method.getAnnotation(ConstructorArgType.class).type();
            } else {
                type = args[0].getClass();
            }
            ConstructorArgPair pair = new ConstructorArgPair();
            pair.setValue(args[0]);
            pair.setType(type);
            this.constructorArgs.put(position, pair);
            this.values.put(name, args[0]);
            logger.debug(new StringBuilder("Put constructor arg with position and value: ").append(position).append(",").append(args[0]));
            return null;
        }
    }
    
    private Object readProperty(final Object[] args, final Method method) {
        if (args != null && args.length != 0) {
            throw new IllegalStateException("The getter method " + method.getName() + " must have no arguments!");
        }
        if (method.getReturnType().isPrimitive()) {
            throw new IllegalReturnTypeException("Return types cannot be primitives.");
        } else {
            String name = null;
            if (method.getName().startsWith("get")) {
                name = StringUtils.uncapitalize(method.getName().substring(3));
            } else {
                name = StringUtils.uncapitalize(method.getName().substring(2));
            }
            Object value = values.get(name);
            logger.debug(new StringBuilder("Read property with name and value: ").append(name).append(",").append(value));
            return value;
        }
    }
    
    private Object make() {
        logger.debug("Making object of class: " + this.productClass);
        
        int argsNr = this.constructorArgs.size();
        Object[] argsArray = new Object[argsNr];
        Class[] typesArray = new Class[argsNr];
        for (Map.Entry<Integer, ConstructorArgPair> entry : this.constructorArgs.entrySet()) {
            int position = entry.getKey();
            Object value = entry.getValue().getValue();
            Class type = entry.getValue().getType();
            if (position < 0 || position > argsNr - 1) {
                throw new IllegalArgumentPositionException("Illegal position: " + position);
            } else {
                argsArray[position] = value;
                typesArray[position] = type;
            }
        }
        
        try {
            Constructor constructor = this.productClass.getConstructor(typesArray);
            Object product = constructor.newInstance(argsArray);
            return product;
        } catch (NoSuchMethodException ex) {
            throw new ObjectConstructionException("No constructor found accepting the following array of types: "
                    + ToStringBuilder.reflectionToString(typesArray, ToStringStyle.SIMPLE_STYLE)
                    + " Have you correctly set all constructor arguments?", ex);
        } catch (InvocationTargetException ex) {
            throw new ObjectConstructionException("Exception thrown by the underlying constructor: "
                    + ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            throw new ObjectConstructionException("Cannot access a constructor with the following array of types: "
                    + ToStringBuilder.reflectionToString(typesArray, ToStringStyle.SIMPLE_STYLE)
                    + " Have you correctly set all constructor arguments?", ex);
        } catch (InstantiationException ex) {
            throw new ObjectConstructionException("Unable to instantiate the following class: "
                    + this.productClass, ex);
        }
    }

    private boolean doEquals(Object proxy, Object other) {
        if (other != null && Proxy.isProxyClass(other.getClass())) {
            return Proxy.getInvocationHandler(proxy) == Proxy.getInvocationHandler(other);
        } else {
            return false;
        }
    }
    
    private int doHashCode(Object proxy) {
        return Proxy.getInvocationHandler(proxy).hashCode();
    }
    
    private String doToString(Object proxy) {
        return Proxy.getInvocationHandler(proxy).toString();
    }
    
    /*** Inner classes ***/
    
    private class ConstructorArgPair {
        
        private Object value;
        private Class type;
        
        public Object getValue() {
            return this.value;
        }
        
        public void setValue(Object value) {
            this.value = value;
        }
        
        public Class getType() {
            return this.type;
        }
        
        public void setType(Class type) {
            this.type = type;
        }
    }
    
    private class PropertyPair {
        
        private Object value;
        private Property.AccessType access;
        
        public Object getValue() {
            return this.value;
        }
        
        public void setValue(Object value) {
            this.value = value;
        }
        
        public Property.AccessType getAccess() {
            return this.access;
        }
        
        public void setAccess(Property.AccessType access) {
            this.access = access;
        }
    }
}
