package org.springmodules.hivemind;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.context.ApplicationContextException;
import org.apache.hivemind.Registry;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Rob Harrop
 */
public class RegistryFactoryBean implements FactoryBean, InitializingBean{

    private static final Log log = LogFactory.getLog(RegistryFactoryBean.class);

    private Registry registry;

    private String configLocation;

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
        this.registry = RegistryBuilder.constructDefaultRegistry();
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }
}
