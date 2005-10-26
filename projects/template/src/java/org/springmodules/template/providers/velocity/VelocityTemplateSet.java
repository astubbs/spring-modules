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

package org.springmodules.template.providers.velocity;

import java.util.*;

import org.apache.commons.logging.*;
import org.apache.velocity.exception.*;
import org.springmodules.template.*;
import org.springmodules.template.providers.velocity.extended.*;

/**
 * An implementation of {@link TemplateSet} that uses VelocityEngine to
 * create and return the VelocityTemplate's.
 *
 * @author Uri Boness
 */
public class VelocityTemplateSet implements TemplateSet {

    private final static Log log = LogFactory.getLog(VelocityTemplateSet.class);

    // a cache to holde generated templates.
    private Map templateCach;

    // the velocity engine to use.
    private ExtendedVelocityEngine engine;

    /**
     * Constructs a new VelocityTemplateSet with a given VelocityEngine.
     *
     * @param engine The velocity engine to be used.
     */
    public VelocityTemplateSet(ExtendedVelocityEngine engine) {
        templateCach = new HashMap();
        this.engine = engine;
    }

    /**
     * @see TemplateSet#getTemplate(String)
     */
    public Template getTemplate(String name) {
        Template template = getCachedTemplate(name);
        if (template != null) {
            return template;
        }
        template = createNewTemplate(name);
        if (template != null) {
            cacheTemplate(name, template);
        }
        return template;
    }


    //=============================================== Helper Methos ====================================================

    protected Template getCachedTemplate(String name) {
        return (Template)templateCach.get(name);
    }

    protected void cacheTemplate(String name, Template template) {
        templateCach.put(name, template);
    }

    protected Template createNewTemplate(String name) {
        try {
            org.apache.velocity.Template velocityTemplate = engine.getTemplate(name);
            return new VelocityTemplateWrapperTemplate(velocityTemplate);
        } catch (ParseErrorException pee) {
            log.error("Could not parse velocity template from source \"" + name + "\"", pee);
            return null;
        } catch (ResourceNotFoundException rnfe) {
            log.error("Could not create velocity template for source \"" + name +
                "\" was not found (might have been removed)", rnfe);
            return null;
        } catch (Exception e) {
            log.error("Could not create velocity template for source '" + name + "'", e);
            return null;
        }
    }

}
