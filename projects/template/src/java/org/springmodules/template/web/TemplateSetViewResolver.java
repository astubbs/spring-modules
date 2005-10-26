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

package org.springmodules.template.web;

import java.util.*;

import org.springmodules.template.*;
import org.springmodules.template.support.*;
import org.springframework.web.servlet.*;

/**
 * This is a Spring {@link ViewResolver} implementation that uses a {@link TemplateSet} to
 * resolve Templates by names, and {@link TemplateView} to return a view for the resolved
 * template.<br/>
 *
 * Using this view resolver it is possible to use any template processor/engine that is supported
 * bye the springmodules Template module as the view technology in a web application.
 *
 * @author Uri Boness
 */
public class TemplateSetViewResolver extends TemplateSetSupport implements ViewResolver {

    private final static String LOCALE_SEPERATOR = "_";

    /**
     * Empty constructor (JavaBean support).
     */
    public TemplateSetViewResolver() {
    }

    /**
     * @see ViewResolver#resolveViewName(String, java.util.Locale)
     */
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        Template template = resolveTemplate(viewName, locale);
        if (template == null) {
            throw new IllegalArgumentException("Could not find view '" + viewName + "' in template set");
        }
        return new TemplateView(template);
    }

    /**
     * Resolves a template based on the given template name and locale. The resolving process is done as follows:<br/>
     * <ul>
     *    <li>
     *          First the local <code>toString()</code> is appended to the template name and the resulting name is passed
     *          to the internal {@link TemplateSet#getTemplate(String)} method.
     *    </li>
     *    <li>
     *          If the internal TemplateSet returned <code>null</code> (i.e. no template with that name was found) then
     *          the locale's language (i.e. {@link java.util.Locale#getLanguage()}) is appended to the original template
     *          name. Then again, the resulting name is passed to the internal {@link TemplateSet#getTemplate(String)} method.
     *    </li>
     *    <li>
     *          If the internal TemplateSet returned <code>null</code> again, then the locale's country (i.e.
     *          {@link java.util.Locale#getCountry()}) is appended to the original template name. The resulting name is
     *          then passed to the internal {@link TemplateSet#getTemplate(String)} method.
     *    </li>
     *    <li>
     *          If <code>null</code> is returned, then passing the original name to the internal
     *          {@link TemplateSet#getTemplate(String)} method. The result of this call is returned from the method.
     *    </li>
     * </ul>
     *
     * @param viewName The name of the view to resolve (actually this can be regarded as the base name of the view)
     * @param locale The locale that is associated with the requested template.
     * @return A template based on the given template base name and locale.
     */
    protected Template resolveTemplate(String viewName, Locale locale) {
        String name = new StringBuffer(viewName).append(LOCALE_SEPERATOR).append(locale.toString()).toString();
        Template template = getTemplateSet().getTemplate(name);
        if (template != null) {
            return template;
        }
        name = new StringBuffer(viewName).append(LOCALE_SEPERATOR).append(locale.getLanguage()).toString();
        template = getTemplateSet().getTemplate(name);
        if (template != null) {
            return template;
        }
        name = new StringBuffer(viewName).append(LOCALE_SEPERATOR).append(locale.getCountry()).toString();
        template = getTemplateSet().getTemplate(name);
        if (template != null) {
            return template;
        }
        return getTemplateSet().getTemplate(viewName);
    }

}
