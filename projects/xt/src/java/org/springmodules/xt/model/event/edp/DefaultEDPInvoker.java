/*
 * Copyright 2007 the original author or authors.
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

package org.springmodules.xt.model.event.edp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springmodules.xt.model.event.edp.EDPInvoker;

/**
 * Default {@link org.springmodules.xt.model.event.EDPInvoker} implementation.<br>
 * The DefaultEDPInvoker will invoke the configured EDP bean (see {@link #setInvokedBean()}) if the bean has a method
 * with name equal to the configured one (see {@link #setInvokedMethodName(String )}), and that accepts a single parameter whose type
 * is the same as, or is a super class/interface of, the published event.
 * <br><br>
 * This class is <b>not</b> thread-safe.
 *
 * @author Sergio Bossa
 */
public class DefaultEDPInvoker implements EDPInvoker {
    
    /**
     * The default name of the methods that must be invoked upon published events: "handleEvent".
     */
    public static final String DEFAULT_METHOD_NAME = "handleEvent";
    
    private static final Logger logger = Logger.getLogger(DefaultEDPInvoker.class);
    
    private Object invokedBean;
    private String invokedMethodName = DEFAULT_METHOD_NAME;
    private boolean continueOnFailingInvocation = true;
    
    private Map<Class, List<Method>> methodsCache = new HashMap<Class, List<Method>>();
    
    public final Object getInvokedBean() {
        return this.invokedBean;
    }
    
    /**
     * Set the bean to invoke upon published events.
     * @param invokedBean The invoked bean.
     */
    public final void setInvokedBean(Object invokedBean) {
        this.invokedBean = invokedBean;
    }
    
    public final String getInvokedMethodName() {
        return this.invokedMethodName;
    }
    
    /**
     * Set the name of the bean method to invoke upon published events. If not set, it defaults to
     * {@link #DEFAULT_METHOD_NAME}.
     * @param invokedMethodName The invoked bean method name.
     */
    public final void setInvokedMethodName(String invokedMethodName) {
        this.invokedMethodName = invokedMethodName;
    }
    
    /**
     * Set to true if the invoker must continue invoking methods after an invocation failure.
     */
    public void setContinueOnFailingInvocation(boolean continueOnFailingInvocation) {
        this.continueOnFailingInvocation = continueOnFailingInvocation;
    }
    
    /**
     * Return true if the invoker must continue invoking methods after an invocation failure.
     */
    public boolean isContinueOnFailingInvocation() {
        return this.continueOnFailingInvocation;
    }
    
    /**
     * Invoke, if any, the bean method with name defined by {@link #setInvokedMethodName(String )}, and with a single argument whose
     * type is the same as, or is a super class/interface of, the given {@link org.springframework.context.ApplicationEvent}.
     * @param applicationEvent The published event.
     */
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (logger.isInfoEnabled()) {
            logger.info(new StringBuilder("On application event: ").append(applicationEvent).toString());
        }
        if (this.invokedBean != null) {
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("Accessing Event Driven POJO: ").append(this.invokedBean).toString());
            }
            List<Method> invokableMethods = this.methodsCache.get(applicationEvent.getClass());
            if (invokableMethods == null) {
                invokableMethods = new LinkedList<Method>();
                Method[] methods = this.invokedBean.getClass().getMethods();
                for (Method m : methods) {
                    if (m.getName().equals(this.invokedMethodName)
                    && m.getParameterTypes().length == 1
                            && m.getParameterTypes()[0].isAssignableFrom(applicationEvent.getClass())) {
                        invokableMethods.add(m);
                    }
                }
                this.methodsCache.put(applicationEvent.getClass(), new LinkedList<Method>(invokableMethods));
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug(new StringBuilder("Found cached methods for event type ")
                    .append(applicationEvent.getClass())
                    .append(" : ").append(invokableMethods)
                    .toString());
                }
            }
            for (Method m : invokableMethods) {
                try {
                    if (logger.isInfoEnabled()) {
                        logger.info(new StringBuilder("Invoking: ").append(m).toString());
                    }
                    m.invoke(this.invokedBean, applicationEvent);
                } catch (IllegalArgumentException ex) {
                    logger.error("The method " + this.invokedMethodName + " cannot be invoked.", ex);
                    if (!this.continueOnFailingInvocation) {
                        throw new IllegalStateException("The method " + this.invokedMethodName + " cannot be invoked.", ex);
                    }
                } catch (IllegalAccessException ex) {
                    logger.error("The method " + this.invokedMethodName + " cannot be invoked.", ex);
                    if (!this.continueOnFailingInvocation) {
                        throw new IllegalStateException("The method " + this.invokedMethodName + " cannot be invoked.", ex);
                    }
                } catch (InvocationTargetException ex) {
                    logger.error("The method " + this.invokedMethodName + " cannot be invoked.", ex);
                    if (!this.continueOnFailingInvocation) {
                        throw new IllegalStateException("The method " + this.invokedMethodName + " cannot be invoked.", ex);
                    }
                }
            }
        } else {
            logger.warn("No bean configured!");
        }
    }
}
