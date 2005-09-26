package org.springmodules.jcr.jackrabbit;

import javax.jcr.Session;
import javax.transaction.xa.XAResource;

import junit.framework.TestCase;

import org.apache.jackrabbit.core.XASession;
import org.easymock.MockControl;
import org.springmodules.jcr.SessionHolder;

public class JcrInterceptorTests extends TestCase {

    /*
     * Test method for 'org.springmodules.jcr.jackrabbit.JcrInterceptor.createSessionHolder(Session)'
     */
    public void testCreateSessionHolder() {
        MockControl sessionControl = MockControl.createControl(Session.class);
        Session session = (Session)sessionControl.getMock();

        MockControl xaSessionControl = MockControl.createControl(XASession.class);
        XASession xaSession = (XASession)xaSessionControl.getMock();

        MockControl xaResCtrl = MockControl.createControl(XAResource.class);
        XAResource xaRes = (XAResource)xaResCtrl.getMock();
        
        xaSessionControl.expectAndReturn(xaSession.getXAResource(), xaRes);
        xaSessionControl.replay();
        sessionControl.replay();
        xaResCtrl.replay();
        
        JcrInterceptor interceptor = new JcrInterceptor();
        SessionHolder holder = null;
        try {
            holder = interceptor.createSessionHolder(session);
            fail("should throw exception - session is not of type XASession");
        } catch (IllegalArgumentException e) {
            // it's okay
        }
        holder = interceptor.createSessionHolder(xaSession);
        assertTrue(holder instanceof UserTxSessionHolder);
        UserTxSessionHolder hld = (UserTxSessionHolder) holder;
        assertSame(xaSession, hld.getSession());
        assertNotNull(hld.getTransaction());
        

        xaSessionControl.verify();
        sessionControl.verify();
        xaResCtrl.verify();

    }
}
