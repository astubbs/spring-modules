package org.springmodules.jcr.jackrabbit;

import javax.jcr.Session;
import javax.transaction.xa.XAResource;

import junit.framework.TestCase;

import org.apache.jackrabbit.core.XASession;
import org.easymock.MockControl;
import org.springmodules.jcr.JcrInterceptor;
import org.springmodules.jcr.SessionHolder;
import org.springmodules.jcr.SessionHolderProvider;
import org.springmodules.jcr.jackrabbit.support.JackRabbitSessionHolderProvider;

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
        SessionHolderProvider provider = new JackRabbitSessionHolderProvider();
        interceptor.setSessionHolderProvider(provider);
        SessionHolder holder = null;
        
        assertSame(provider, interceptor.getSessionHolderProvider());
        holder = provider.createSessionHolder(xaSession);
        
        assertSame(xaSession, holder.getSession());
        
        xaSessionControl.verify();
        sessionControl.verify();
        xaResCtrl.verify();

    }
}
