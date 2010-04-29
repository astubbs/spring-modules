package org.springmodules.jcr.support;

import java.util.List;

import junit.framework.TestCase;

import org.springmodules.jcr.jackrabbit.support.JackRabbitSessionHolderProvider;

/**
 * 
 * @author Costin Leau
 *
 */
public class ServiceSessionHolderProviderManagerTests extends TestCase {

	ServiceSessionHolderProviderManager providerManager;
	
	protected void setUp() throws Exception {
		super.setUp();
		providerManager = new ServiceSessionHolderProviderManager();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'org.springmodules.jcr.support.ServiceSessionHolderProviderManager.getProviders()'
	 */
	public void testGetProviders() {
		List providers = providerManager.getProviders();
		assertEquals(1, providers.size());
		assertTrue(providers.get(0) instanceof JackRabbitSessionHolderProvider);
	}

}
