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

package org.springmodules.template.resolvers;

import java.util.*;

import org.springmodules.template.*;
import org.springframework.context.i18n.*;

/**
 * A TemplateSourceResolver that is locale aware.
 *
 * @author Uri Boness
 */
public class LocalizedTemplateSourceResolver implements TemplateSourceResolver {

    private final static String LOCALE_SEPERATOR = "_";

    private LocaleContext localeContext;
    private TemplateSourceResolver internalResolver;

    /**
     * Default constructor (javabean support).
     */
    public LocalizedTemplateSourceResolver() {
    }

    /**
     * Constructs a new LocalizedTemplateSourceResolver with a given template source resolver and
     * a locale context from which the locale will be fetched.
     *
     * @param internalResolver The internal template source resolver to which resolve request will be delegated.
     * @param localeContext The locale context which will be used to fetch the locale.
     */
    public LocalizedTemplateSourceResolver(TemplateSourceResolver internalResolver, LocaleContext localeContext) {
        this.internalResolver = internalResolver;
        this.localeContext = localeContext;
    }

    /**
     * Resolves the localized template source associated with the given base name.
     *
     * @param name the base name of the localized template source.
     * @return The localized template source associated with the given base name.
     */
    public final TemplateSource resolveTemplateSource(String name) {
        Locale locale = localeContext.getLocale();
        return resolveTemplateSource(name, locale);
    }

    /**
     * Resolves the template source associated with the given base name and which relates
     * to the given locale. This method resolves the template source as follows:
     * <ul>
     * <li>
     *     If the given locale is null then it will try to resolve a template source using the given base name
     * </li>
     * <li>
     *     Otherwise, the <code>locale.toString()</code> will be concatenated to the base name and the resulting name
     *     will be used to resolve the template source. Example: name_en_US
     * </li>
     * <li>
     *     If no source could be found, the <code>locale.getLanguage()</code> will be concatenated to the base name and
     *     the resulting name will be used to resolve the template source. Example: name_en
     * </li>
     * <li>
     *     If no source could be found, the <code>locale.getCountry()</code> will be concatenated to the base name and
     *     the resulting name will be used to resolve the template source. Example: name_US
     * </li>
     * <li>
     *     If still no source could be found, the base name will be used to resolve the template source.
     * </li>
     * </ul>
     *
     * @param name The base name of the template source.
     * @param locale The locale to which the returned template source relates.
     * @return The template source associated with the given base name and locale.
     */
    protected TemplateSource resolveTemplateSource(String name, Locale locale) {
        if (locale == null) {
            return internalResolver.resolveTemplateSource(name);
        }

        String localizedName = new StringBuffer(name).append(LOCALE_SEPERATOR).append(locale.toString()).toString();
        TemplateSource  source = internalResolver.resolveTemplateSource(localizedName);
        if (source != null) {
            return source;
        }

        localizedName = new StringBuffer(name).append(LOCALE_SEPERATOR).append(locale.getLanguage()).toString();
        source = internalResolver.resolveTemplateSource(localizedName);
        if (source != null) {
            return source;
        }

        localizedName = new StringBuffer(name).append(LOCALE_SEPERATOR).append(locale.getCountry()).toString();
        source = internalResolver.resolveTemplateSource(localizedName);
        if (source != null) {
            return source;
        }

        source = internalResolver.resolveTemplateSource(name);
        return source;
    }

    //============================================== Setter/Getter =====================================================

    public void setLocaleContext(LocaleContext localeContext) {
        this.localeContext = localeContext;
    }

    public void setInternalResolver(TemplateSourceResolver internalResolver) {
        this.internalResolver = internalResolver;
    }
}
