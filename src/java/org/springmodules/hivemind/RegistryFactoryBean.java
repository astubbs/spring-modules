package org.springmodules.hivemind;

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
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Rob Harrop
 * @author Thierry Templier
 */
public class RegistryFactoryBean implements FactoryBean, InitializingBean{

    private static final Log log = LogFactory.getLog(RegistryFactoryBean.class);

    private Registry registry;

    private List configLocations;

    public Object getObject() throws Exception {
        return this.registry;
    }

    public Class getObjectType() {
        return Registry.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
		if( configLocations!=null ) {
			RegistryBuilder builder = new RegistryBuilder();
			ClassResolver resolver = new DefaultClassResolver();

			log.info("Loading standard Hivemind modules");
			builder.processModules(resolver);
			HivemindResourceLoader pathResolver=new DefaultHivemindResourceLoader(resolver);
			for(Iterator i=configLocations.iterator();i.hasNext();) {
				String configLocation=(String)i.next();
				Resource resource=pathResolver.getResource(configLocation);
				log.info("Loading modules from "+resource.getName()+" ("+configLocation+")");
				builder.processModule(resolver, resource);
			}

			this.registry = builder.constructRegistry(Locale.getDefault());
		} else {
			this.registry = RegistryBuilder.constructDefaultRegistry();
		}
		log.info("Hivemind registry loaded");
    }

    public void setConfigLocations(List configLocations) {
        this.configLocations = configLocations;
    }
}
