package org.springmodules.hivemind;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Registry;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.util.AbstractResource;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.hivemind.util.FileResource;
import org.apache.hivemind.util.URLResource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;

/**
 * @author Rob Harrop
 * @author Thierry Templier
 */
public class RegistryFactoryBean implements FactoryBean, InitializingBean {

    private static final Log log = LogFactory.getLog(RegistryFactoryBean.class);

    private Registry registry;

    private org.springframework.core.io.Resource[] configLocations;

    public Object getObject() throws Exception {
        return this.registry;
    }

    public Class getObjectType() {
        return Registry.class;
    }

    public boolean isSingleton() {
        return true;
    }

	/**
	 * Wrap the spring resource into an hivemind resource.
	 * @param resolver the hivemind class resolver
	 * @param configLocation the configuration location resource
	 * @return the wrapped resource
	 */
	private Resource wrapSpringResource(ClassResolver resolver,org.springframework.core.io.Resource configLocation) {
		if( configLocation instanceof ClassPathResource ) {
			ClassPathResource classpathConfigLocation=(ClassPathResource)configLocation;
			return new ClasspathResourceWrapper(resolver,classpathConfigLocation);
		} else {
			return new DefaultResourceWrapper(configLocation);
		}
	}

    public void afterPropertiesSet() throws Exception {
		if( configLocations!=null ) {
			RegistryBuilder builder = new RegistryBuilder();
			ClassResolver resolver = new DefaultClassResolver();

			log.info("Loading standard Hivemind modules");
			builder.processModules(resolver);
			for(int index=0;index<configLocations.length;index++) {
				builder.processModule(resolver, wrapSpringResource(resolver,configLocations[index]));
			}

			this.registry = builder.constructRegistry(Locale.getDefault());
		} else {
			this.registry = RegistryBuilder.constructDefaultRegistry();
		}
		log.info("Hivemind registry loaded");
    }

    public void setConfigLocations(org.springframework.core.io.Resource[] configLocations) {
        this.configLocations = configLocations;
    }

}
