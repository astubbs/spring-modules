package org.springmodules.workflow.jbpm31;

import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.configuration.ObjectFactory;
import org.jbpm.graph.def.ProcessDefinition;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springmodules.workflow.jbpm31.definition.ProcessDefinitionFactoryBean;

/**
 * Created on Feb 21, 2006
 *
 * $Id: LocalJbpmConfigurationFactoryBeanTests.java,v 1.1 2006/03/02 14:56:04 costin Exp $
 * $Revision: 1.1 $
 */

/**
 * @author Costin Leau
 *
 */
public class LocalJbpmConfigurationFactoryBeanTests extends TestCase {

	LocalJbpmConfigurationFactoryBean configuration;
	Resource configurationResource;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		configuration = new LocalJbpmConfigurationFactoryBean();
		configurationResource = new ClassPathResource("jbpm.cfg.xml");
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		configuration = null;
		configurationResource = null;
	}

	public void testAfterPropertiesSet() throws Exception {
		try {
			configuration.afterPropertiesSet();
			fail("expected exception");
		}
		catch (Exception e) {
			// okay
		}

		MockControl factoryControl = MockControl.createControl(ObjectFactory.class);
		ObjectFactory mockFactory = (ObjectFactory) factoryControl.getMock();
		configuration.setObjectFactory(mockFactory);

		MockControl beanFactoryControl = MockControl.createNiceControl(BeanFactory.class);
		BeanFactory beanFactory = (BeanFactory) beanFactoryControl.getMock();

		JbpmContext context = new JbpmContext(null, mockFactory);
		factoryControl.expectAndReturn(mockFactory.createObject(JbpmContext.DEFAULT_JBPM_CONTEXT_NAME),
				context);
		beanFactoryControl.replay();
		factoryControl.replay();

		//configuration.setBeanFactory(beanFactory);
		configuration.afterPropertiesSet();
		JbpmConfiguration cfg = (JbpmConfiguration) configuration.getObject();

		assertSame(context, cfg.createJbpmContext());

		beanFactoryControl.verify();
		factoryControl.verify();
	}

	public void testNoSpringObjectFactory() throws Exception {
		//configuration.setUseSpringObjectFactory(false);

		MockControl factoryControl = MockControl.createControl(ObjectFactory.class);
		ObjectFactory mockFactory = (ObjectFactory) factoryControl.getMock();

		configuration.setObjectFactory(mockFactory);
		configuration.afterPropertiesSet();

		JbpmConfiguration cfg = (JbpmConfiguration) configuration.getObject();

		Field of = cfg.getClass().getDeclaredField("objectFactory");
		of.setAccessible(true);
		assertSame(mockFactory, getObjectFactory(cfg));
	}

	public void testLoadResource() throws Exception {
		
		configuration.setConfiguration(configurationResource);
		//configuration.setUseSpringObjectFactory(false);
		configuration.afterPropertiesSet();
		JbpmConfiguration cfg = (JbpmConfiguration) configuration.getObject();
		JbpmContext context = cfg.createJbpmContext();
	}

	/**
	 * Ugly hack to get the object factory directly from the jbpm configuration without going
	 * through the jbpmContext.
	 * 
	 * @param cfg
	 * @return
	 * @throws Exception
	 */
	private Object getObjectFactory(JbpmConfiguration cfg) throws Exception {
		Field of = cfg.getClass().getDeclaredField("objectFactory");
		of.setAccessible(true);
		return of.get(cfg);
	}

	public void testDeployDefinitions() throws Exception {

		Resource definitionLocation = new ClassPathResource("org/springmodules/workflow/jbpm31/simpleWorkflow.xml");
		ProcessDefinitionFactoryBean definition = new ProcessDefinitionFactoryBean();
		definition.setDefinitionLocation(definitionLocation);
		definition.afterPropertiesSet();

		MockControl sfCtrl = MockControl.createControl(SessionFactory.class);
		SessionFactory sf = (SessionFactory) sfCtrl.getMock();
	
		MockControl sCtrl = MockControl.createNiceControl(Session.class);
		Session session = (Session) sCtrl.getMock();
		
		MockControl queryCtrl = MockControl.createNiceControl(Query.class);
		Query query = (Query) queryCtrl.getMock();
		
		session.getNamedQuery("");
		sCtrl.setMatcher(MockControl.ALWAYS_MATCHER);
		sCtrl.setReturnValue(query);
		
		sfCtrl.expectAndReturn(sf.openSession(), session);
		sfCtrl.replay();
		sCtrl.replay();
		queryCtrl.replay();
		
		configuration.setProcessDefinitions(new ProcessDefinition[] { (ProcessDefinition)definition.getObject() });
		configuration.setConfiguration(configurationResource);
		//configuration.setUseSpringObjectFactory(false);
		configuration.setSessionFactory(sf);
		configuration.afterPropertiesSet();
		
		sfCtrl.verify();
		sCtrl.verify();
		queryCtrl.verify();
	}
}
