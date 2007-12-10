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

package org.springmodules.xt.ajax.component.dynamic;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springmodules.template.Template;
import org.springmodules.template.TemplateResolver;
import org.springmodules.xt.ajax.component.Component;


/**
 * Component that uses a generic template to build dynamic XHTML content.<br>
 * It uses the Spring Modules Template project: refer to <a href="https://springmodules.dev.java.net/">Spring Modules docs</a>
 * for more information about it and for a list of supported templating technologies.
 *
 * @author Sergio Bossa
 */
public class TemplateComponent implements Component {
    
    private static final long serialVersionUID = 26L;
    
    private TemplateResolver templateResolver;
    private String templateName;
    private Map templateModel = new HashMap();
    
    private String characterEncoding;
    private Locale locale;
    
    /**
     * Component constructor.
     * 
     * @param templateResolver The resolver to use for loading the template.
     * @param templateName The template name.
     */
    public TemplateComponent(TemplateResolver templateResolver, String templateName) {
        this.templateResolver = templateResolver;
        this.templateName = templateName;
    }
    
    public String render() {
        Template template = this.templateResolver.resolve(this.templateName, this.characterEncoding, this.locale);
        StringWriter writer = new StringWriter();
        
        template.generate(writer, this.templateModel);
        
        return writer.toString();
    }

    /**
     * Set the template model, that is, the variables used inside the template.
     * 
     * @param templateModel The variables model.
     */
    public void setTemplateModel(Map templateModel) {
        this.templateModel = templateModel;
    }
    
    /**
     * Set the template character encoding.
     * 
     * @param characterEncoding The template character encoding.
     */
    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }
    
    /**
     * Set the template locale.
     * 
     * @param locale The template locale.
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
