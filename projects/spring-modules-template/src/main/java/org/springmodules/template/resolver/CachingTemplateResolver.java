package org.springmodules.template.resolver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

import org.springmodules.template.Template;

/**
 * Extends {@link org.springmodules.template.resolver.BasicTemplateResolver} to support simple template caching.
 *
 * @author Uri Boness
 */
public class CachingTemplateResolver extends BasicTemplateResolver {

    // Map<String, Template>
    private Map cache;

    private boolean cacheEnabled;

    /**
     * Constructing a new CachingTemplateResolver.
     */
    protected CachingTemplateResolver() {
        cache = Collections.synchronizedMap(new HashMap());
        cacheEnabled = true;
    }

    /**
     * Resolves and returns the template associated with the given name and encoding. This method is thread
     * though in highly concurrent environment multiple template may be created for the same key in parallel.
     *
     * @param name The name of the template
     * @param encoding The encoding of the template
     * @param locale The locale of the template
     * @return The resolved template.
     */
    public Template resolve(String name, String encoding, Locale locale) {
        if (!shouldCache(name, encoding, locale)) {
            return super.resolve(name, encoding, locale);
        }
        Template template = (Template)cache.get(name);
        if (template == null) {
            template = super.resolve(name, encoding, locale);
            cache.put(name, template);
        }
        return template;
    }

    /**
     * Clears up the internal template cache.
     */
    public void clearCache() {
        cache.clear();
    }

    /**
     * Determines whether the given template (identified by the name, encoding and locale) should be cached or not.
     * By defaut all template are cached if the <code>cacheEnabled</code> property is set to true (which is also the
     * default setting). This method can be overriden by sub-classes to provide a more sofistciated caching strategy.
     *
     * @param name The name of the template.
     * @param encoding The encoding of the template.
     * @param locale The locale of the template.
     * @return <code>true</code> if the given template should be cached, <code>fals</code> otherwise.
     */
    protected boolean shouldCache(String name, String encoding, Locale locale) {
        return cacheEnabled;
    }


    //============================================== Setter/Getter =====================================================

    /**
     * Sets whether caching is enabled or not. By default caching is enabled, but this setter provides a mechansim to
     * disable caching which can be useful during development.
     *
     * @param cacheEnabled Determines whether caching is enabled/disabled.
     */
    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

}
