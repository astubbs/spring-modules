package org.springmodules.hivemind;

import org.apache.hivemind.Registry;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextException;

/**
 * @author Rob Harrop
 */
public class ServiceFactoryBean implements FactoryBean, InitializingBean {

    private Registry registry;

    private String serviceName;

    private Class serviceInterface;

    public void setServiceInterface(Class serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }


    public Object getObject() throws Exception {
        return getServiceObject();
    }

    public Class getObjectType() {
        // can we use singletons and cache here?
        return getServiceObject().getClass();
    }

    public boolean isSingleton() {
        return false;
    }

    public void afterPropertiesSet() throws Exception {
        if (registry == null) {
            throw new ApplicationContextException("Property [registry] of class [" + ServiceFactoryBean.class
                    + "] is required.");
        }

        if (serviceInterface == null) {
            throw new ApplicationContextException("Property [serviceInterface] of class [" + ServiceFactoryBean.class
                    + "] is required.");
        }

        // check that the service exists for fail fast behaviour
        boolean containsService = false;

        if (serviceName == null) {
            containsService = registry.containsService(serviceInterface);
        } else {
            containsService = registry.containsService(serviceName, serviceInterface);
        }

        if (!containsService) {
            throw new ApplicationContextException("Service with interface [" + serviceInterface.getName() +
                    "] and name [" + serviceName + "] is not present in registry.");
        }
    }

    private Object getServiceObject() {
        if (serviceName == null) {
            return registry.getService(serviceInterface);
        } else {
            return registry.getService(serviceName, serviceInterface);
        }
    }
}
