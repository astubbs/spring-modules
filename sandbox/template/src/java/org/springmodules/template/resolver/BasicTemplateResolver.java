package org.springmodules.template.resolver;

import org.springmodules.template.TemplateResolver;
import org.springmodules.template.TemplateEngine;
import org.springmodules.template.Template;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

/**
 * @author Uri Boness
 */
public class BasicTemplateResolver implements TemplateResolver, ResourceLoaderAware {

    protected final static String DEFAULT_ENCODING = "UTF-8";

    private TemplateEngine engine;

    private ResourceLoader resourceLoader;

    protected BasicTemplateResolver() {
        this(null);
    }

    protected BasicTemplateResolver(TemplateEngine engine) {
        this(engine, new DefaultResourceLoader());
    }

    protected BasicTemplateResolver(TemplateEngine engine, ResourceLoader resourceLoader) {
        this.engine = engine;
        this.resourceLoader = resourceLoader;
    }

    public final Template resolve(String name) {
        return resolve(name, DEFAULT_ENCODING);
    }

    public Template resolve(String name, String encoding) {
        Resource resource = loadTemplateResource(name, encoding);
        return engine.createTemplate(resource, encoding);
    }

    protected Resource loadTemplateResource(String name, String encoding) {
        return resourceLoader.getResource(name);
    }

    //============================================== Setter/Getter =====================================================

    /**
     * Sets the template engine to be used to load and process the resolved template.
     *
     * @param engine The template engine to be used to load and process the resolved template.
     */
    public void setEngine(TemplateEngine engine) {
        this.engine = engine;
    }

    /**
     * Returns the template engine that is used to load and process the resolved tempalatel
     *
     * @return The template engine that is used to load and process the resolved tempalatel
     */
    public TemplateEngine getEngine() {
        return engine;
    }

    /**
     * Sets the resource loader that will be used to load the template resources.
     *
     * @param resourceLoader The resource loader that will be used to load the template resources.
     * @see org.springframework.context.ResourceLoaderAware#setResourceLoader(org.springframework.core.io.ResourceLoader)
     */
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    protected ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

}
