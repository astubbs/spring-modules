/**
 * Created on Nov 10, 2005
 *
 * $Id: AbstractSessionHolderProviderManagerTests.java,v 1.2 2006/03/07 13:09:31 costin Exp $
 * $Revision: 1.2 $
 */
package org.springmodules.jcr.support;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Repository;
import javax.jcr.Session;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.springmodules.jcr.SessionFactory;
import org.springmodules.jcr.SessionHolder;
import org.springmodules.jcr.SessionHolderProvider;

/**
 * 
 * @author Costin Leau
 *
 */
public class AbstractSessionHolderProviderManagerTests extends TestCase {

	AbstractSessionHolderProviderManager providerManager;
	List providers;
	String repositoryName;
	MockControl sfCtrl, sessCtrl, repoCtrl;
	Repository repo;
	Session sess;
	SessionFactory sf;
	SessionHolderProvider customProvider;
	

	protected void setUp() throws Exception {
		super.setUp();

		providers = new ArrayList();
		repositoryName = "dummyRepository";

		providerManager = new AbstractSessionHolderProviderManager() {
			/**
			 * @see org.springmodules.jcr.support.AbstractSessionHolderProviderManager#getProviders()
			 */
			public List getProviders() {
				return providers;
			}
		};
		// build crazy mock hierarchy
		sfCtrl = MockControl.createControl(SessionFactory.class);
		sf = (SessionFactory) sfCtrl.getMock();
		sessCtrl = MockControl.createControl(Session.class);
		sess = (Session) sessCtrl.getMock();
		repoCtrl = MockControl.createControl(Repository.class);
		repo = (Repository) repoCtrl.getMock();

		//sfCtrl.expectAndReturn(sf.getSession(), sess);
		//sessCtrl.expectAndReturn(sess.getRepository(), repo);
		repoCtrl.expectAndReturn(repo.getDescriptor(Repository.REP_NAME_DESC), repositoryName);
		
		customProvider = new SessionHolderProvider() {

			/**
			 * @see org.springmodules.jcr.SessionHolderProvider#acceptsRepository(java.lang.String)
			 */
			public boolean acceptsRepository(String repo) {
				return repositoryName.equals(repo);
			}

			/**
			 * @see org.springmodules.jcr.SessionHolderProvider#createSessionHolder(javax.jcr.Session)
			 */
			public SessionHolder createSessionHolder(Session session) {
				return null;
			}

		};
	}

	protected void tearDown() throws Exception {
		sfCtrl.verify();
		sessCtrl.verify();
		repoCtrl.verify();
		
		super.tearDown();
	}

	/*
	 * Default provider is used even on empty list.
	 * 
	 * Test method for 'org.springmodules.jcr.support.AbstractSessionHolderProviderManager.getSessionProvider(SessionFactory)'
	 */
	public void testDefaultSessionProvider() {
		// sanity check
		assertSame(providers, providerManager.getProviders());

		sfCtrl.replay();
		sessCtrl.replay();
		repoCtrl.replay();

		SessionHolderProvider provider = providerManager.getSessionProvider(repo);
		assertSame(GenericSessionHolderProvider.class, provider.getClass());
	}

	/*
	 * Make sure that the approapriate provider is selected
	 * Test method for 'org.springmodules.jcr.support.AbstractSessionHolderProviderManager.getSessionProvider(SessionFactory)'
	 */
	public void testCustomSessionProvider() {
		// sanity check

		providers = new ArrayList();
		providers.add(customProvider);

		sfCtrl.replay();
		sessCtrl.replay();
		repoCtrl.replay();

		assertSame(customProvider, providerManager.getSessionProvider(repo));
	}
	
	/*
	 * Make sure that we fallback to default provider
	 * 
	 * Test method for 'org.springmodules.jcr.support.AbstractSessionHolderProviderManager.getSessionProvider(SessionFactory)'
	 */
	public void testDifferentSessionProvider() {
		// sanity check

		customProvider = new SessionHolderProvider() {

			/**
			 * @see org.springmodules.jcr.SessionHolderProvider#acceptsRepository(java.lang.String)
			 */
			public boolean acceptsRepository(String repo) {
				return false;
			}

			/**
			 * @see org.springmodules.jcr.SessionHolderProvider#createSessionHolder(javax.jcr.Session)
			 */
			public SessionHolder createSessionHolder(Session session) {
				return null;
			}

		};
		providers = new ArrayList();
		providers.add(customProvider);

		sfCtrl.replay();
		sessCtrl.replay();
		repoCtrl.replay();

		assertSame(GenericSessionHolderProvider.class, providerManager.getSessionProvider(repo).getClass());
	}	
}
