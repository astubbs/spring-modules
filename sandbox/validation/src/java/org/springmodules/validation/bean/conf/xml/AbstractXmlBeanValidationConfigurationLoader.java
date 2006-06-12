/*
 * Copyright 2004-2005 the original author or authors.
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

package org.springmodules.validation.bean.conf.xml;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.core.io.Resource;
import org.springmodules.validation.bean.conf.AbstractResourceBasedBeanValidationConfigurationLoader;
import org.springmodules.validation.bean.conf.BeanValidationConfiguration;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An {@link AbstractResourceBasedBeanValidationConfigurationLoader} implementation that serves as a base class
 * for all xml baed bean validation configuration loaders.
 *
 * @author Uri Boness
 */
public abstract class AbstractXmlBeanValidationConfigurationLoader extends AbstractResourceBasedBeanValidationConfigurationLoader {

    private final static Log logger = LogFactory.getLog(AbstractXmlBeanValidationConfigurationLoader.class);

    /**
     * Creates a {@link Document} from the given resource and delegates the call to
     * {@link #loadConfiguration(Class, org.w3c.dom.Document)}.
     *
     * @see AbstractResourceBasedBeanValidationConfigurationLoader#loadConfiguration(Class, org.springframework.core.io.Resource)
     */
    protected final BeanValidationConfiguration loadConfiguration(Class clazz, Resource resource) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(true);
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(resource.getInputStream());
            return loadConfiguration(clazz, document);
        } catch (IOException ioe) {
            logger.error("Could not read resource '" + resource.getDescription() + "'", ioe);
            return null;
        } catch (ParserConfigurationException pce) {
            logger.error("Could not parse xml resource '" + resource.getDescription() + "'", pce);
            return null;
        } catch (SAXException se) {
            logger.error("Could not parse xml resource '" + resource.getDescription() + "'", se);
            return null;
        } catch (Throwable t) {
            logger.error("Could not parse xml resource '" + resource.getDescription() + "'", t);
            return null;
        }
    }

    /**
     * Creates a {@link Document} from the given resource and delegates the call to
     * {@link #supports(Class, org.w3c.dom.Document)}.
     *
     * @see AbstractResourceBasedBeanValidationConfigurationLoader#supports(Class, org.springframework.core.io.Resource)
     */
    protected final boolean supports(Class clazz, Resource resource) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(true);
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(resource.getInputStream());
            return supports(clazz, document);
        } catch (IOException ioe) {
            logger.error("Could not read resource '" + resource.getDescription() + "'", ioe);
            return false;
        } catch (ParserConfigurationException pce) {
            logger.error("Could not parse xml resource '" + resource.getDescription() + "'", pce);
            return false;
        } catch (SAXException se) {
            logger.error("Could not parse xml resource '" + resource.getDescription() + "'", se);
            return false;
        } catch (Throwable t) {
            logger.error("Could not parse xml resource '" + resource.getDescription() + "'", t);
            return false;
        }
    }

    /**
     * Loads the bean validation configuration for the given class from the given configuration document.
     *
     * @param clazz The class for which the bean validation configuration will be loaded.
     * @param document The configuration document.
     * @return The loaded bean validation configuration.
     * @see #loadConfiguration(Class, org.springframework.core.io.Resource)
     */
    protected abstract BeanValidationConfiguration loadConfiguration(Class clazz, Document document);

    /**
     * Checks whether the given class is supported by this loader based on the given configuration document.
     *
     * @param clazz The class to be checked.
     * @param document The configuration document.
     * @return <code>true</code> if the given class is supported, <code>false</code> otherwise.
     * @see #supports(Class, org.springframework.core.io.Resource)
     */
    protected abstract boolean supports(Class clazz, Document document);

}
