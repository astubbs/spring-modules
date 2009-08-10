package org.springmodules.jcr.jackrabbit;

import javax.jcr.Repository;

import junit.framework.TestCase;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

public class RepositoryFactoryBeanTests extends TestCase {

	RepositoryFactoryBean factory;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factory = new RepositoryFactoryBean();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'org.springmodules.jcr.jeceira.RepositoryFactoryBean.resolveConfigurationResource()'
	 */
	public void testResolveConfigurationResource() throws Exception {

		factory.resolveConfigurationResource();
		final DefaultResourceLoader loader = new DefaultResourceLoader();
		final Resource res = loader.getResource("/repository.xml");
		assertEquals(res, factory.getConfiguration());
		assertEquals(".", factory.getHomeDir().getFilename());

	}

	/*
	 * Test method for 'org.springmodules.jcr.jeceira.RepositoryFactoryBean.createRepository()'
	 */
	public void testCreateRepository() throws Exception {
		factory.afterPropertiesSet();
		final Repository rep = (Repository) factory.getObject();
		assertEquals(rep.getDescriptor("jcr.repository.name"), "Jackrabbit");

		assertEquals(true, factory.getObject() instanceof Repository);
		assertEquals(true, factory.isSingleton());
		assertEquals(Repository.class, factory.getObjectType());
		factory.destroy();

	}
}
