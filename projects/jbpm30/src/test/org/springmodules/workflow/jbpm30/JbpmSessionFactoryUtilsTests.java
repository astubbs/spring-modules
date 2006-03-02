package org.springmodules.workflow.jbpm30;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jbpm.db.GraphSession;
import org.jbpm.db.JbpmSession;
import org.jbpm.db.JbpmSessionFactory;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springmodules.workflow.jbpm30.JbpmCallback;
import org.springmodules.workflow.jbpm30.JbpmSessionFactoryUtils;
import org.springmodules.workflow.jbpm30.JbpmTemplate;

/**
 * @author Rob Harrop
 */
public class JbpmSessionFactoryUtilsTests extends AbstractDependencyInjectionSpringContextTests {
	
	private static final Logger log = Logger.getLogger(JbpmSessionFactoryUtilsTests.class);

	private JbpmSessionFactory jbpmSessionFactory;

	private PlatformTransactionManager transactionManager;

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

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public void testGetSessionOutsideTransaction() {
		log.info("getSessionOutsideTransaction");
		
		JbpmSession firstJbpmSession = JbpmSessionFactoryUtils.getSession(this.jbpmSessionFactory);
		assertNotNull(firstJbpmSession);

		JbpmSession secondJbpmSession = JbpmSessionFactoryUtils.getSession(this.jbpmSessionFactory);

		assertNotNull(secondJbpmSession);

		assertNotSame(firstJbpmSession, secondJbpmSession);
		
		// clean-up
		JbpmSessionFactoryUtils.releaseSession(secondJbpmSession, this.jbpmSessionFactory);
		JbpmSessionFactoryUtils.releaseSession(firstJbpmSession, this.jbpmSessionFactory);
	}

	public void testGetSessionInsideTransaction() {
		log.info("getSessionInsideTransaction");
		final Map sessionMap = new HashMap();

		TransactionTemplate transactionTemplate = new TransactionTemplate(this.transactionManager);
		transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus status) {
				JbpmSession firstSession = JbpmSessionFactoryUtils.getSession(jbpmSessionFactory);
				JbpmSession secondSession = JbpmSessionFactoryUtils.getSession(jbpmSessionFactory);
				sessionMap.put("first", firstSession);
				sessionMap.put("second", secondSession);

				// try to close inside a transaction
				JbpmSessionFactoryUtils.releaseSession(firstSession, jbpmSessionFactory);
				assertTrue("Should not close session inside a transaction.",
						firstSession.getSession().isOpen());
				return null;
			}
		});

		JbpmSession first = (JbpmSession) sessionMap.get("first");
		JbpmSession second = (JbpmSession) sessionMap.get("second");

		assertSame("Should've got the same session twice inside the transaction", first, second);
		assertNull("Hibernate session should be closed", first.getSession());
		assertNull("no current session", JbpmSession.getCurrentJbpmSession());

		//assertFalse("Session not closed in synchronization", first.getSession().isOpen());
		
		sessionMap.clear();
	}

	public void testReleaseOutsideTransaction() {
		log.info("ReleaseOutsideTransaction");
		JbpmSession jbpmSession = JbpmSessionFactoryUtils.getSession(this.jbpmSessionFactory);
		JbpmSessionFactoryUtils.releaseSession(jbpmSession, this.jbpmSessionFactory);
		assertNull("Session should be closed", jbpmSession.getSession());
	}

	/**
	 * Test the double session closing as reported here: 
	 * http://opensource2.atlassian.com/projects/spring/browse/MOD-78
	 *
	 */
	public void testJbpmSessionSynchronization(){
		log.info("jbpmSessionSynchronization");
		TransactionTemplate transactionTemplate = new TransactionTemplate(this.transactionManager);
		transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus status) {
				JbpmTemplate jbpmTemplate = new JbpmTemplate(jbpmSessionFactory, processDefinition);

				jbpmTemplate.execute(new JbpmCallback() {
					public Object doInJbpm(JbpmSession session) {
						session.getGraphSession().saveProcessDefinition(processDefinition);
						return null;
					}
				});
				return null;
			}
		});
	}

	/**
	 * Test IllegalArgumentException as reported here:
	 * http://opensource2.atlassian.com/projects/spring/browse/MOD-58
	 *
	 */
	public void testMultipleJBmpInvocations() {

		log.info("multipleJbpmInvocation");
		
		//		 Get Sessions
		JbpmSession jbpmSession = JbpmSessionFactoryUtils.getSession(this.jbpmSessionFactory);
		GraphSession graphSession = jbpmSession.getGraphSession();

		graphSession.saveProcessDefinition(processDefinition);
		//		 Get All Process Definitions
		List l = graphSession.findLatestProcessDefinitions();

		//		 Get the arbitrarily first Process Definition
		ProcessDefinition pd = (ProcessDefinition) l.get(0);

		//		 Load it again, individually
		ProcessDefinition pd2 = graphSession.loadProcessDefinition(pd.getId());

		//		 Create an instance of the Process Def with Task
		ProcessInstance processInstance = new ProcessInstance(pd2);
		TaskInstance jbpmTaskInstance = processInstance.getTaskMgmtInstance().createStartTaskInstance();

		//		 Persist these changes
		graphSession.saveProcessInstance(processInstance);

		//		 Be polite, like the template is.
		JbpmSessionFactoryUtils.releaseSession(jbpmSession, this.jbpmSessionFactory);

	}

	protected String[] getConfigLocations() {
		return new String[] { "org/springmodules/workflow/jbpm30/applicationContext.xml" };
	}

	/**
	 * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#onSetUp()
	 */
	protected void onSetUp() throws Exception {
		super.onSetUp();
		if (JbpmSession.getCurrentJbpmSession() != null)
			log.warn("*** current session not null!");
		else
			log.info("*** current session clean");
	}

}
