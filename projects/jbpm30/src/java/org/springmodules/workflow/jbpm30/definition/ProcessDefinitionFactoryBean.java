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

package org.springmodules.workflow.jbpm30.definition;

import java.io.InputStream;

import org.jbpm.graph.def.ProcessDefinition;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * FactoryBean used for loading ProcessDefinition using Spring Resource mechanism.
 * <p/>
 * @author Rob Harrop
 * @author Costin Leau 
 */
public class ProcessDefinitionFactoryBean implements FactoryBean, InitializingBean {

    private ProcessDefinition processDefinition;

    private Resource definitionLocation;

    private boolean deployProcessDefinitionOnStartup;

    public void setDefinitionLocation(Resource definitionLocation) {
        this.definitionLocation = definitionLocation;
    }

    public void setDeployProcessDefinitionOnStartup(boolean deployProcessDefinitionOnStartup) {
        this.deployProcessDefinitionOnStartup = deployProcessDefinitionOnStartup;
    }

    public void afterPropertiesSet() throws Exception {
        if (this.definitionLocation == null) {
            throw new FatalBeanException("Property [definitionLocation] of class [" +
                    ProcessDefinitionFactoryBean.class.getName() + "] is required.");
        }


        InputStream inputStream = null;
        try {
            inputStream = this.definitionLocation.getInputStream();
            this.processDefinition = ProcessDefinition.parseXmlInputStream(inputStream);

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public Object getObject() throws Exception {
        return this.processDefinition;
    }

    public Class getObjectType() {
        return (processDefinition == null) ? ProcessDefinition.class : processDefinition.getClass();
    }

    public boolean isSingleton() {
        return true;
    }
}
