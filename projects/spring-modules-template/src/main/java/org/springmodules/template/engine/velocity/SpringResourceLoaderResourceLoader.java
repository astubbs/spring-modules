package org.springmodules.template.engine.velocity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.springframework.core.io.DefaultResourceLoader;

/**
 * A velocity ResourceLoader implementation that serves and uses itself as a template source registry
 * to find template sources by a names.
 *
 * @author Uri Boness
 */
public class SpringResourceLoaderResourceLoader extends ResourceLoader {

    private final static Log log = LogFactory.getLog(SpringResourceLoaderResourceLoader.class);

    private org.springframework.core.io.ResourceLoader resourceLoader;

    /**
     * Constructs a new SpringResourceLoaderResourceLoader with {@link org.springframework.core.io.DefaultResourceLoader} as the internal spring
     * resource loader.
     */
    public SpringResourceLoaderResourceLoader() {
        this(new DefaultResourceLoader());
    }

    /**
     * Creates a new SpringResourceLoaderResourceLoader with a given {@link org.springframework.core.io.ResourceLoader template resolver}.
     *
     * @param resourceLoader The spring resource loader this loader will use to load the template resources.
     */
    public SpringResourceLoaderResourceLoader(org.springframework.core.io.ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#init(org.apache.commons.collections.ExtendedProperties)
     */
    public void init(ExtendedProperties configuration) {
        // do nothing...
    }

    /**
     * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#getResourceStream(String)
     */
    public InputStream getResourceStream(String source) throws ResourceNotFoundException {
        try {
            return resourceLoader.getResource(source).getInputStream();
        } catch (IOException ioe) {
            throw new ResourceNotFoundException("Could not get stream for resource '" + source + "'", ioe);
        }
    }

    /**
     * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#isSourceModified(org.apache.velocity.runtime.resource.Resource)
     */
    public boolean isSourceModified(Resource resource) {
        String name = resource.getName();
        File file = null;
        try {
            file = resourceLoader.getResource(name).getFile();
        } catch (IOException ioe) {
            log.error("Could not check whether the resource '" + name + "' was modified. Assuming it was not...", ioe);
            return false;
        }
        return file != null && file.lastModified() != resource.getLastModified();
    }

    /**
     * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#getLastModified(org.apache.velocity.runtime.resource.Resource)
     */
    public long getLastModified(Resource resource) {
        String name = resource.getName();
        File file = null;
        try {
            org.springframework.core.io.Resource springResource = resourceLoader.getResource(name);
            if (springResource == null) {
                return 0;
            }
            file = springResource.getFile();
        } catch (IOException ioe) {
            log.error("Could not get the last modified date of resource '" + name +
                "' was modified. Assuming it was not...", ioe);
            return 0;
        }
        return file.lastModified();
    }

}
