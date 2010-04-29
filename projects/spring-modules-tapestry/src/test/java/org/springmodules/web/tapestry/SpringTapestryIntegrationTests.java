package org.springmodules.web.tapestry;

import junit.framework.TestCase;
import org.springframework.mock.web.MockServletContext;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.context.ContextLoader;
import org.springframework.beans.factory.BeanFactory;
import org.apache.tapestry.ApplicationServlet;
import org.apache.hivemind.Registry;

/**
 * Created by IntelliJ IDEA.
 * User: stevendevijver
 * Date: Oct 16, 2005
 * Time: 11:59:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class SpringTapestryIntegrationTests extends TestCase {
    public void testSpringIntegration() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        servletContext.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, "classpath:org/springmodules/web/tapestry/applicationContext.xml");
        ContextLoader contextLoader = new ContextLoader();
        contextLoader.initWebApplicationContext(servletContext);

        MockServletConfig servletConfig = new MockServletConfig(servletContext, "servlet");
        ApplicationServlet servlet = new ApplicationServlet();
        servlet.init(servletConfig);

        Registry hiveMindRegistry = (Registry) servletContext.getAttribute("org.apache.tapestry.Registry:servlet");
        assertNotNull("HiveMind registry is not available in servlet context", hiveMindRegistry);

        BeanFactory beanFactory = (BeanFactory) hiveMindRegistry.getService("hivemind.lib.DefaultSpringBeanFactory", BeanFactory.class);
        String name = (String) beanFactory.getBean("name", String.class);
        assertNotNull(name);
        assertEquals("Steven Devijver", name);
    }
}
