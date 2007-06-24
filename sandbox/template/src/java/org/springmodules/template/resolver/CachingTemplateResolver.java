package org.springmodules.template.resolver;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import org.springmodules.template.Template;

/**
 * @author Uri Boness
 */
public class CachingTemplateResolver extends AbstractTemplateResolver {

    // Map<String, Template>
    private Map cache;

    protected CachingTemplateResolver() {
        cache = Collections.synchronizedMap(new HashMap());
    }

    /**
     * Resolves and returns the template associated with the given name and encoding. This method is thread
     * though in highly concurrent environment multiple template may be created for the same key in parallel.
     *
     * @param name
     * @param encoding
     * @return
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
