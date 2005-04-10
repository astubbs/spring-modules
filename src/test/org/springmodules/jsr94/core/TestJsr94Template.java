/**
 * 
 */

package org.springmodules.jsr94.core;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.rules.Handle;
import javax.rules.InvalidHandleException;
import javax.rules.InvalidRuleSessionException;
import javax.rules.StatefulRuleSession;
import javax.rules.StatelessRuleSession;

import junit.framework.TestCase;
import org.springmodules.jsr94.Jsr94TransactionManager;
import org.springmodules.jsr94.rulesource.RuleSource;
import org.springmodules.jsr94.support.StatefulRuleSessionCallback;
import org.springmodules.jsr94.support.StatelessRuleSessionCallback;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Tests the Jsr94Template class
 * 
 * @author janm
 */
public class TestJsr94Template extends TestCase {

	private Handle browserHandle;

	private static String URI = "test";

	private ApplicationContext context = new ClassPathXmlApplicationContext("org/springmodules/jsr94/rulesource.xml");

	/**
		 * Gets the Jsr94Template for the given source
		 *
		 * @param ruleSourceBeanName
		 * @return A Jsr94Template instance
		 */
	private Jsr94Template getTemplate(String ruleSourceBeanName) {
		RuleSource source = (RuleSource) context.getBean(ruleSourceBeanName);

		return new Jsr94Template(source);
	}

	/**
	 * Tests the executeStateless method
	 */
	public void testStateless() {
		List result = (List) getTemplate("ruleSource").executeStateless(URI, null, new StatelessRuleSessionCallback() {
					public Object execute(StatelessRuleSession session) throws InvalidRuleSessionException, RemoteException {
						List inputList = new ArrayList();
						inputList.add("Gecko");
						return session.executeRules(inputList);
					}

				});
		assertTrue("result must contain Gecko", result.contains("Gecko"));
		assertTrue("result must contain a:Gecko", result.contains("a:Gecko"));
	}

	/**
	 * Tests the executeStateful method within a transaction
	 */
	public void testStatefulInTransaction() {
		final Jsr94Template template = getTemplate("ruleSource");
		final Jsr94TransactionManager transactionManager = new Jsr94TransactionManager();
		transactionManager.setRuleSource(template.getRuleSource());
		final TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);
		List result = (List) txTemplate.execute(new TransactionCallback() {

					/* (non-Javadoc)
					 * @see org.springframework.transaction.support.TransactionCallback#doInTransaction(org.springframework.transaction.TransactionStatus)
					 */
					public Object doInTransaction(TransactionStatus status) {
						List result = (List) template.executeStateful(URI, null, new StatefulRuleSessionCallback() {
									public Object execute(StatefulRuleSession session) throws InvalidRuleSessionException, InvalidHandleException, RemoteException {
										browserHandle = session.addObject("Gecko");
										session.executeRules();
										return session.getObjects();
									}

								});

						result = (List) template.executeStateful(URI, null, new StatefulRuleSessionCallback() {
									public Object execute(StatefulRuleSession session) throws InvalidRuleSessionException, InvalidHandleException, RemoteException {
										session.removeObject(browserHandle);
										session.addObject("MSIE");
										session.executeRules();
										return session.getObjects();
									}

								});

						return result;
					}

				});

		assertTrue("result must contain MSIE", result.contains("MSIE"));
		assertTrue("result must contain a:MSIE", result.contains("a:MSIE"));
		assertTrue("result must contain a:Gecko", result.contains("a:Gecko"));
	}

	/**
	 * Tests the executeStateful method outside a transaction
	 */
	public void testStatefulOutsideTransaction() {
		final Jsr94Template template = getTemplate("ruleSource");

		List result = (List) template.executeStateful(URI, null, new StatefulRuleSessionCallback() {
					public Object execute(StatefulRuleSession session) throws InvalidRuleSessionException, InvalidHandleException, RemoteException {
						browserHandle = session.addObject("Gecko");
						session.executeRules();
						return session.getObjects();
					}

				});

		result = (List) template.executeStateful(URI, null, new StatefulRuleSessionCallback() {
					public Object execute(StatefulRuleSession session) throws InvalidRuleSessionException, InvalidHandleException, RemoteException {
						try {
							session.removeObject(browserHandle);
							fail("Cannot remove browserHandle from a new session");
						}
						catch (InvalidRuleSessionException ex) {
							// expected
						}
						return null;
					}

				});
	}

	/**
	 * Tests stateful execution in objects created in bean factory
	 */
	public void testStateful() {
		BusinessBean bean = (BusinessBean) context.getBean("businessBean");
		bean.statefulInTransaction();
		bean.statefulOutsideTransaction();
	}
}
