package org.springmodules.workflow.jbpm30;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jbpm.db.JbpmSession;
import org.jbpm.db.JbpmSessionFactory;
import org.jbpm.graph.def.Action;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * @author Rob Harrop
 */
public class LocalJbpmSessionFactoryBeanTests extends AbstractTransactionalDataSourceSpringContextTests {

	private static final Logger log = Logger.getLogger(LocalJbpmSessionFactoryBeanTests.class);

	private JbpmSessionFactory jbpmSessionFactory;

	private SessionFactory hibernateSessionFactory;

	private ProcessDefinition processDefinition;

	/**
	 * @param processDefinition The processDefinition to set.
	 */
	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	public void setJbpmSessionFactory(JbpmSessionFactory jbpmSessionFactory) {
		this.jbpmSessionFactory = jbpmSessionFactory;
	}

	public void setHibernateSessionFactory(SessionFactory hibernateSessionFactory) {
		this.hibernateSessionFactory = hibernateSessionFactory;
	}

	public void testGetSession() {
		JbpmSession session = this.jbpmSessionFactory.openJbpmSession();
		assertNotNull("JbpmSession should not be null", session);

		Session hibernateSession = session.getSession();
		Session springSuppliedHibernateSession = SessionFactoryUtils.getSession(this.hibernateSessionFactory,
				false);

		assertSame("JbpmSession not using Spring-supplied Hibernate session", hibernateSession,
				springSuppliedHibernateSession);

		hibernateSession = null;
		session.close();

	}

	public void testJbpmCloseSession() throws Exception {
		log.debug("start testJbpmCloseSession");
		// save reference to current Session (in case there is one existing already)
		JbpmSession currentSession = JbpmSession.getCurrentJbpmSession();

		JbpmSession jbpmSession = this.jbpmSessionFactory.openJbpmSession();

		Session springSession = SessionFactoryUtils.getSession(hibernateSessionFactory, false);
		// session is opened
		assertTrue(jbpmSession.getSession().isOpen());
		// same session returned by current jbpm session
		assertSame(jbpmSession.getSession(), JbpmSession.getCurrentJbpmSession().getSession());
		// same session as the one thread-bounded by Spring
		assertSame(springSession, jbpmSession.getSession());

		// close jbpm session
		jbpmSession.close();

		// jbpm session nullified
		assertNull(jbpmSession.getSession());
		assertSame(currentSession, JbpmSession.getCurrentJbpmSession());

		// thread-bound session not null and opened
		assertTrue(springSession.isOpen());
		log.debug("end testJbpmCloseSession");
	}

	public void testJbpmHandler() throws Exception {
		Action action = processDefinition.getAction("myAction");
		ActionHandler delegate = (ActionHandler) action.getActionDelegation().getInstance();

		// create the context and pass it on to the action
		ProcessInstance instance = processDefinition.createProcessInstance();
		// we have to use transient variables or otherwise HB will get in the way
		instance.getContextInstance().setTransientVariable(DummyActionHandler.TEST_LABEL,
				applicationContext.getBean("jbpmAction"));
		Token token = instance.getRootToken();

		delegate.execute(new ExecutionContext(token));
	}

	protected String[] getConfigLocations() {
		return new String[] { "org/springmodules/workflow/jbpm30/applicationContext.xml" };
	}

	/**
	 * @see org.springframework.test.AbstractTransactionalSpringContextTests#onSetUpInTransaction()
	 */
	protected void onSetUpBeforeTransaction() throws Exception {
		super.onSetUpInTransaction();
		
		if (JbpmSession.getCurrentJbpmSession() != null)
			log.warn("*** current session not null!");
		else
			log.info("*** current session null");
	}
}
