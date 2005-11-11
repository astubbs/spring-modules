package org.springmodules.jcr;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.observation.ObservationManager;

import junit.framework.TestCase;

import org.easymock.MockControl;

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
        } catch (IllegalStateException ex) {
            // ignore: test method didn't call replay
        }
    }

    /*
     * Test method for 'org.springmodules.jcr.JcrSessionFactory.getSession()'
     */
    public void testGetSession() {
        try {
            repoCtrl.expectAndReturn(repo.login(null, null), null);
            factory.getSession();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    /*
     * Test method for 'org.springmodules.jcr.JcrSessionFactory.afterPropertiesSet'
     */
    public void testAfterPropertiesSet() {
        try {
            factory.setRepository(null);
            factory.afterPropertiesSet();
            fail("expected exception (session factory badly initialized");
        } catch (IllegalArgumentException e) {
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
        factory.setEventListenerDefinitions(listeners);

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
        assertSame(listeners, factory.getEventListenerDefinitions());
        Session sess = factory.getSession();
        assertSame(session, sess);

        repoCtrl.verify();
        sessionCtrl.verify();
        wsCtrl.verify();
        omCtrl.verify();
    }

}
