/**
 * Created on Sep 12, 2005
 *
 * $Id: JcrDaoSupportTests.java,v 1.1 2005/09/26 10:21:53 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr.support;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.springmodules.jcr.JcrTemplate;
import org.springmodules.jcr.SessionFactory;

/**
 * @author Costin Leau
 * 
 */
public class JcrDaoSupportTests extends TestCase {

    private MockControl sfCtrl, sessCtrl;
    private SessionFactory sf;
    private Session sess;
    
    protected void setUp() throws Exception {
        super.setUp();
        sfCtrl = MockControl.createControl(SessionFactory.class);
        sf = (SessionFactory)sfCtrl.getMock();

        sessCtrl = MockControl.createControl(Session.class);
        sess = (Session)sessCtrl.getMock();

    }

    protected void tearDown() throws Exception {
        super.tearDown();
        
        try {
            sessCtrl.verify();
            sfCtrl.verify();
        }
        catch (IllegalStateException ex) {
            // ignore: test method didn't call replay
        }
    }
    
    public void testJcrDaoSupportWithSessionFactory() throws Exception {
        sfCtrl.replay();
        final List test = new ArrayList();
        JcrDaoSupport dao = new JcrDaoSupport() {
            protected void initDao() {
                test.add("test");
            }
        };
        dao.setSessionFactory(sf);
        dao.afterPropertiesSet();
        assertEquals("Correct SessionFactory", sf, dao.getSessionFactory());
        assertEquals("Correct JcrTemplate", sf, dao.getJcrTemplate().getSessionFactory());
        assertEquals("initDao called", test.size(), 1);
        sfCtrl.verify();
    }

    public void testJcrDaoSupportWithJcrTemplate() throws Exception {
        JcrTemplate template = new JcrTemplate();
        final List test = new ArrayList();
        JcrDaoSupport dao = new JcrDaoSupport() {
            protected void initDao() {
                test.add("test");
            }
        };
        dao.setJcrTemplate(template);
        dao.afterPropertiesSet();
        assertEquals("Correct JcrTemplate", template, dao.getJcrTemplate());
        assertEquals("initDao called", test.size(), 1);
    }
    
    public void testAfterPropertiesSet()
    {
        JcrDaoSupport dao = new JcrDaoSupport()
        {
        };
        
        try {
            dao.afterPropertiesSet();
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            //
        }
    }
    
    public void testSetSessionFactory()
    {
        JcrDaoSupport dao = new JcrDaoSupport() {};
        
        sfCtrl.replay();
        dao.setSessionFactory(sf);
        assertEquals(dao.getSessionFactory(), sf);
    }
    
    public void testGetSession()
    {
        JcrDaoSupport dao = new JcrDaoSupport() {};
        sfCtrl.expectAndReturn(sf.getSession(), sess);
        
        sfCtrl.replay();
        dao.setSessionFactory(sf);
        try {
            dao.getSession();
            fail("expected exception");
        } catch (IllegalStateException e) {
            // it's okay 
        }
        assertEquals(dao.getSession(true), sess);
    }

    public void testReleaseSession()
    {
        JcrDaoSupport dao = new JcrDaoSupport() {};
        
        dao.releaseSession(null);
        
        sfCtrl.expectAndReturn(sf.getSession(), sess);
        
        sfCtrl.replay();
        dao.setSessionFactory(sf);
        dao.releaseSession(sess);
    }

    public void testConvertException()
    {
        JcrDaoSupport dao = new JcrDaoSupport() {};
        MockControl tCtrl = MockClassControl.createControl(JcrTemplate.class);
        JcrTemplate t = (JcrTemplate)tCtrl.getMock();
        
        RepositoryException ex = new RepositoryException();
        
        tCtrl.expectAndReturn(t.convertJcrAccessException(ex), null);
        dao.setJcrTemplate(t);
        dao.convertJCRAccessException(ex);
        
    }

}
