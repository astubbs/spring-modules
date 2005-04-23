/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.dwr.create;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.ExecutionContext;
import org.w3c.dom.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletContext;

/**
 * A <code>Creator</code> implementation which allows looking up beans from an
 * existing web application context. <br>
 * It can be used in cases where you don't want to create a seperate
 * <code>BeanFactory</code> instance and use the exisitng web application
 * context instead.
 *
 * Configuration of this create is done by adding the following to the
 * <code>&lt;init&gt;</code> section of your DWR configuration:<br>
 * <code> <br>
 * &lt;create id="springWebContextCreator"
 * class="org.springmodules.dwr.create.SpringWebContextCreator" /&gt; <br>
 * </code> <br>
 * And the following to the <code>&lt;allow&gt;</code> section of your DWR
 * configuration:<br>
 * <code> <br>
 * &lt;create  <br>
 * &nbsp;&nbsp;create="springWebContextCreator" <br>
 * &nbsp;&nbsp;javascript="<b>&lt;name of javascript&gt;</b>" <br>
 * &nbsp;&nbsp;beanName="<b>&lt;name of jbean to expose&gt;</b>"/&gt; <br>
 * </code>
 *
 * @author Bram Smeets
 */
public class SpringWebContextCreator implements Creator {
    /** The logger for this class. */
    private static final Log log = LogFactory.getLog(
            SpringWebContextCreator.class);

    /** The create atttribute that specifies the name of the bean to create. */
    private static final String BEAN_NAME_ATTR = "beanName";

    /** The name of the bean to create. */
    private String beanName;
    /** The class of the bean to create. */
    private Class type;
    /** The application context to retrieve the bean. */
    private ApplicationContext applicationContext;

    /**
     * Initializes the Creator. <br>
     * The configuration argument should contain an attribute that specifies
     * the name of the bean to retrieve from the web application context.
     *
     * @param config the configuration element
     *
     * @throws IllegalArgumentException in case the specified configuration
     *                                  argument does not contain an argument
     *                                  specifying the bean to lookup
     */
    public void init(Element config) throws IllegalArgumentException {
        // make sure the bean name attribute has been specified
        if(!config.hasAttribute(BEAN_NAME_ATTR)) {
            throw new IllegalArgumentException("the attribute '" +
                    BEAN_NAME_ATTR + "' has not been specified");
        }

        // set the name of the bean to create
        beanName = config.getAttribute(BEAN_NAME_ATTR);
    }

    /**
     * Accessor for the <code>Class</code> that this Creator allows access
     * to. <br>
     * It retrieves an instance of the requested bean and returns its class.
     *
     * @return the type of this allowed class
     */
    public Class getType() {
        if(type == null) {
            // retrieve an instance and determine its class
            type = getInstance().getClass();
        }

        return type;
    }

    /**
     * Accessor for the/an instance of this Creator. <br>
     * It looks up the in the configured bean name in the web application
     * context and returns it.
     *
     * @return the request bean from the web application context
     */
    public Object getInstance() {
        // make sure the application context is set
        if(applicationContext == null) {
            ServletContext servletContext =
                    ExecutionContext.get().getServletContext();
            applicationContext = WebApplicationContextUtils.
                    getWebApplicationContext(servletContext);
        }

        // get the specified bean name from the application context
        return applicationContext.getBean(beanName);
    }
}