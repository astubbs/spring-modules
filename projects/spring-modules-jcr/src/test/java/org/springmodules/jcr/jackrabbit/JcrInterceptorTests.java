package org.springmodules.jcr.jackrabbit;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Session;
import javax.transaction.xa.XAResource;

import junit.framework.TestCase;

import org.apache.jackrabbit.api.XASession;
import org.easymock.MockControl;
import org.springmodules.jcr.JcrInterceptor;
import org.springmodules.jcr.SessionFactory;
import org.springmodules.jcr.SessionHolder;
import org.springmodules.jcr.SessionHolderProvider;
import org.springmodules.jcr.jackrabbit.support.JackRabbitSessionHolderProvider;
import org.springmodules.jcr.support.ListSessionHolderProviderManager;

public class JcrInterceptorTests extends TestCase {

	/*
	 * Test method for 'org.springmodules.jcr.jackrabbit.JcrInterceptor.createSessionHolder(Session)'
	 */
	public void testCreateSessionHolder() throws Exception {
		final MockControl sfCtrl = MockControl.createControl(SessionFactory.class);
		final SessionFactory sf = (SessionFactory) sfCtrl.getMock();
		final MockControl sessionControl = MockControl.createControl(Session.class);

		final MockControl xaSessionControl = MockControl.createControl(XASession.class);
		final XASession xaSession = (XASession) xaSessionControl.getMock();

		final MockControl xaResCtrl = MockControl.createControl(XAResource.class);
		final XAResource xaRes = (XAResource) xaResCtrl.getMock();

		xaSessionControl.expectAndReturn(xaSession.getXAResource(), xaRes);
		xaSessionControl.replay();

		sfCtrl.replay();
		sessionControl.replay();
		xaResCtrl.replay();

		final JcrInterceptor interceptor = new JcrInterceptor();
		final ListSessionHolderProviderManager manager = new ListSessionHolderProviderManager();
		final List providers = new ArrayList();
		final SessionHolderProvider provider = new JackRabbitSessionHolderProvider();
		providers.add(provider);
		manager.setProviders(providers);
		interceptor.setSessionFactory(sf);
		interceptor.afterPropertiesSet();

		SessionHolder holder = null;

		holder = provider.createSessionHolder(xaSession);

		assertSame(xaSession, holder.getSession());

		xaSessionControl.verify();
		sessionControl.verify();
		xaResCtrl.verify();
		sfCtrl.verify();
	}
}
