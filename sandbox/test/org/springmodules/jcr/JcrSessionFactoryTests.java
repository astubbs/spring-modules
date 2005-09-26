package org.springmodules.jcr;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;

import junit.framework.TestCase;

import org.easymock.MockControl;

public class JcrSessionFactoryTests extends TestCase {

    JcrSessionFactory factory;
    private MockControl repoCtrl;
    private Repository repo;
    
    protected void setUp() throws Exception {
        super.setUp();
        repoCtrl = MockControl.createControl(Repository.class);
        repo = (Repository)repoCtrl.getMock();

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
     * Test method for 'org.springmodules.jcr.JcrSessionFactory.getSession(String)'
     */
    public void testGetSessionString() {
        String otherWs = "other ws";
        try {
            repoCtrl.expectAndReturn(repo.login(null, otherWs), null);
            factory.getSession(otherWs);
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
    
    public void testConstructor()
    {
        factory = new JcrSessionFactory(repo, "ws", null);
        assertEquals(repo, factory.getRepository());
        assertEquals("ws", factory.getWorkspaceName());
        assertNull(factory.getCredentials());
        
        factory.setWorkspaceName("ws");
        assertEquals(factory.getWorkspaceName(), "ws");
    }
    
    public void testEquals()
    {
        assertEquals(factory.hashCode(), repo.hashCode() + 17 *37);
        assertFalse(factory.equals(null));
        assertEquals(factory, factory);
        
       MockControl repoCtrl2;
       Repository repo2;
       
       repoCtrl2 = MockControl.createControl(Repository.class);
       repo2 = (Repository)repoCtrl2.getMock();
       
       JcrSessionFactory fact2 = new JcrSessionFactory();
       fact2.setRepository(repo2);
       assertEquals(factory, fact2);
    }

}
