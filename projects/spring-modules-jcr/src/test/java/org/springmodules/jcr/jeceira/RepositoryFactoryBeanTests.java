package org.springmodules.jcr.jeceira;

import javax.jcr.Repository;

import junit.framework.TestCase;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import com.jeceira.config.ConfigManager;

public class RepositoryFactoryBeanTests extends TestCase {

	RepositoryFactoryBean factory;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factory = new RepositoryFactoryBean();
		factory.setRepositoryName("jeceira-repo");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'org.springmodules.jcr.jeceira.RepositoryFactoryBean.resolveConfigurationResource()'
	 */
	public void testResolveConfigurationResource() throws Exception {

		try {
			factory.setRepositoryName(null);
			factory.afterPropertiesSet();
			fail("expected exception");
		} catch (final Exception e) {
			// it's okay
		}

		factory.setRepositoryName("jeceira-repo");
		factory.resolveConfigurationResource();
		final ConfigManager config = factory.getConfigManager();
		final DefaultResourceLoader loader = new DefaultResourceLoader();
		final Resource res = loader.getResource("/jeceira.xml");
		assertEquals(res, factory.getConfiguration());

		factory = new RepositoryFactoryBean();
		factory.setConfigManager(config);
		factory.setRepositoryName("jeceira-repo");
		factory.resolveConfigurationResource();
		assertNull(factory.getConfiguration());
	}

	/*
	 * Test method for 'org.springmodules.jcr.jeceira.RepositoryFactoryBean.createRepository()'
	 */
	public void testCreateRepository() throws Exception {
		factory.afterPropertiesSet();
		final Repository rep = factory.createRepository();
		assertEquals(rep.getDescriptor("jcr.repository.name"), "Jeceira");
	}
}
