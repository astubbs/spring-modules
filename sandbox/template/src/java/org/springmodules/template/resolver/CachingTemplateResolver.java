package org.springmodules.template.resolver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springmodules.template.Template;

/**
 * Extends {@link org.springmodules.template.resolver.BasicTemplateResolver} to support simple template caching.
 *
 * @author Uri Boness
 */
public class CachingTemplateResolver extends BasicTemplateResolver {

    // Map<String, Template>
    private Map cache;

    /**
     * Constructing a new CachingTemplateResolver.
     */
    protected CachingTemplateResolver() {
        cache = Collections.synchronizedMap(new HashMap());
    }

    /**
     * Resolves and returns the template associated with the given name and encoding. This method is thread
     * though in highly concurrent environment multiple template may be created for the same key in parallel.
     *
     * @param name The name of the template
     * @param encoding The encoding of the template
     * @return The resolved template.
     */
    public Template resolve(String name, String encoding) {
        Template template = (Template)cache.get(name);
        if (template == null) {
            template = super.resolve(name, encoding);
            cache.put(name, template);
        }
        return template;
    }

}
