package org.springmodules.jcr;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.jcr.NamespaceRegistry;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.observation.ObservationManager;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.springmodules.jcr.support.ListSessionHolderProviderManager;

public class JcrSessionFactoryTests extends TestCase {

	JcrSessionFactory factory;

	private MockControl repoCtrl;

	private Repository repo;

	protected void setUp() throws Exception {
		super.setUp();
		repoCtrl = MockControl.createControl(Repository.class);
		repo = (Repository) repoCtrl.getMock();

		factory = new JcrSessionFactory();
		factory.setRepository(repo);
	}

	protected void tearDown() throws Exception {
		super.tearDown();

		try {
			repoCtrl.verify();
		}
		catch (IllegalStateException ex) {
			// ignore: test method didn't call replay
		}
		
		repoCtrl = null;
		repo = null;
		factory = null;
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrSessionFactory.getSession()'
	 */
	public void testGetSession() {
		try {
			repoCtrl.expectAndReturn(repo.login(null, null), null);
			factory.getSession();
		}
		catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrSessionFactory.afterPropertiesSet'
	 */
	public void testAfterPropertiesSet() throws Exception {
		try {
			factory.setRepository(null);
			factory.afterPropertiesSet();
			fail("expected exception (session factory badly initialized");
		}
		catch (IllegalArgumentException e) {
		}
	}

	public void testConstructor() {
		factory = new JcrSessionFactory(repo, "ws", null);
		assertEquals(repo, factory.getRepository());
		assertEquals("ws", factory.getWorkspaceName());
		assertNull(factory.getCredentials());

		factory.setWorkspaceName("ws");
		assertEquals(factory.getWorkspaceName(), "ws");
	}

	public void testEquals() {
		assertEquals(factory.hashCode(), repo.hashCode() + 17 * 37);
		assertFalse(factory.equals(null));
		assertEquals(factory, factory);

		MockControl repoCtrl2;
		Repository repo2;

		repoCtrl2 = MockControl.createControl(Repository.class);
		repo2 = (Repository) repoCtrl2.getMock();

		JcrSessionFactory fact2 = new JcrSessionFactory();
		fact2.setRepository(repo2);
		assertEquals(factory, fact2);
	}

	public void testAddListeners() throws RepositoryException {
		EventListenerDefinition def1 = new EventListenerDefinition();
		EventListenerDefinition def2 = new EventListenerDefinition();

		EventListenerDefinition listeners[] = new EventListenerDefinition[] { def1, def2 };
		factory.setEventListeners(listeners);

		MockControl sessionCtrl = MockControl.createControl(Session.class);
		Session session = (Session) sessionCtrl.getMock();

		MockControl wsCtrl = MockControl.createControl(Workspace.class);
		Workspace ws = (Workspace) wsCtrl.getMock();

		MockControl omCtrl = MockControl.createControl(ObservationManager.class);
		ObservationManager oManager = (ObservationManager) omCtrl.getMock();

		repoCtrl.expectAndReturn(repo.login(null, null), session);
		sessionCtrl.expectAndReturn(session.getWorkspace(), ws);

		wsCtrl.expectAndReturn(ws.getObservationManager(), oManager);

		oManager.addEventListener(def1.getListener(), def1.getEventTypes(), def1.getAbsPath(), def1.isDeep(),
				def1.getUuid(), def1.getNodeTypeName(), def1.isNoLocal());
		oManager.addEventListener(def2.getListener(), def2.getEventTypes(), def2.getAbsPath(), def2.isDeep(),
				def2.getUuid(), def2.getNodeTypeName(), def2.isNoLocal());

		repoCtrl.replay();
		sessionCtrl.replay();
		wsCtrl.replay();
		omCtrl.replay();

		// coverage madness
		assertSame(listeners, factory.getEventListeners());
		Session sess = factory.getSession();
		assertSame(session, sess);

		repoCtrl.verify();
		sessionCtrl.verify();
		wsCtrl.verify();
		omCtrl.verify();
	}

	public void testRegisterNamespaces() throws RepositoryException {
		Properties namespaces = new Properties();
		namespaces.put("foo", "bar");
		namespaces.put("hocus", "pocus");

		factory.setNamespaces(namespaces);

		MockControl sessionCtrl = MockControl.createControl(Session.class);
		Session session = (Session) sessionCtrl.getMock();

		MockControl wsCtrl = MockControl.createControl(Workspace.class);
		Workspace ws = (Workspace) wsCtrl.getMock();

		MockControl nrCtrl = MockControl.createControl(NamespaceRegistry.class);
		NamespaceRegistry registry = (NamespaceRegistry) nrCtrl.getMock();

		repoCtrl.expectAndReturn(repo.login(null, null), session);
		sessionCtrl.expectAndReturn(session.getWorkspace(), ws);
		wsCtrl.expectAndReturn(ws.getNamespaceRegistry(), registry);
		registry.registerNamespace("foo", "bar");
		registry.registerNamespace("hocus", "pocus");

		nrCtrl.replay();
		wsCtrl.replay();
		sessionCtrl.replay();
		repoCtrl.replay();

		factory.afterPropertiesSet();

		nrCtrl.verify();
		wsCtrl.verify();
		sessionCtrl.verify();
	}

	public void testDefaultSesionHolder() throws Exception {
		factory.afterPropertiesSet();
		Session session = factory.getSession();
		SessionHolder holder = factory.getSessionHolder(session);
		assertSame(SessionHolder.class, holder.getClass());
		// default session holder provider
		assertSame(SessionHolder.class, factory.getSessionHolder(null).getClass());
	}

	public void testSessionHolder() throws Exception {
		final String REPO_NAME = "hocus_pocus";

		repoCtrl.expectAndReturn(repo.getDescriptor(Repository.REP_NAME_DESC), REPO_NAME);

		MockControl sessionCtrl = MockControl.createControl(Session.class);
		Session session = (Session) sessionCtrl.getMock();

		repoCtrl.expectAndReturn(repo.login(null, null), session);

		repoCtrl.replay();
		sessionCtrl.replay();

		List providers = new ArrayList();

		providers.add(new SessionHolderProvider() {

			/**
			 * @see org.springmodules.jcr.SessionHolderProvider#acceptsRepository(java.lang.String)
			 */
			public boolean acceptsRepository(String repositoryName) {
				return REPO_NAME.equals(repositoryName);
			}

			/**
			 * @see org.springmodules.jcr.SessionHolderProvider#createSessionHolder(javax.jcr.Session)
			 */
			public SessionHolder createSessionHolder(Session session) {
				return new CustomSessionHolder(session);
			}

		});
		
		ListSessionHolderProviderManager providerManager = new ListSessionHolderProviderManager();
		providerManager.setProviders(providers);

		factory.setSessionHolderProviderManager(providerManager);
		factory.afterPropertiesSet();

		Session sess = factory.getSession();
		assertSame(session, sess);
		assertSame(CustomSessionHolder.class, factory.getSessionHolder(sess).getClass());
		
		repoCtrl.verify();
		sessionCtrl.verify();
	}

	/**
	 * Used for testing.
	 * 
	 * @author Costin Leau
	 *
	 */
	private class CustomSessionHolder extends SessionHolder {

		/**
		 * @param session
		 */
		public CustomSessionHolder(Session session) {
			super(session);
			
		}

	}

}
