/**
 * 
 */
package org.springmodules.jsr94.core;

import java.rmi.RemoteException;
import java.util.List;

import javax.rules.Handle;
import javax.rules.InvalidHandleException;
import javax.rules.InvalidRuleSessionException;
import javax.rules.StatefulRuleSession;

import org.springmodules.jsr94.core.Jsr94Template;
import org.springmodules.jsr94.support.StatefulRuleSessionCallback;

/**
 * @author janm
 *
 */
public class BusinessBean {

	/**
	 * Jsr94Template
	 */
	private Jsr94Template template;
	
	/**
	 * Broser fact handle
	 */
	private Handle browserHandle;

	/**
	 * Tests stateful ruleset execution in Tx
	 */
	public void statefulInTransaction() {
		List result = (List)template.executeStateful("test", null, new StatefulRuleSessionCallback() {

			public Object execute(StatefulRuleSession session) throws InvalidRuleSessionException, InvalidHandleException, RemoteException {
				browserHandle = session.addObject("Gecko");
				session.executeRules();
				return session.getObjects();
			}
			
		});
		
		result = (List)template.executeStateful("test", null, new StatefulRuleSessionCallback() {

			public Object execute(StatefulRuleSession session) throws InvalidRuleSessionException, InvalidHandleException, RemoteException {
				session.removeObject(browserHandle);
				session.addObject("MSIE");
				session.executeRules();
				return session.getObjects();
			}
			
		});
	}
	
	/**
	 * Tests stateful ruleset execition outside Tx
	 */
	public void statefulOutsideTransaction() {
		List result = (List)template.executeStateful("test", null, new StatefulRuleSessionCallback() {

			public Object execute(StatefulRuleSession session) throws InvalidRuleSessionException, InvalidHandleException, RemoteException {
				browserHandle = session.addObject("Gecko");
				session.executeRules();
				return session.getObjects();
			}
			
		});
		
		result = (List)template.executeStateful("test", null, new StatefulRuleSessionCallback() {

			public Object execute(StatefulRuleSession session) throws InvalidRuleSessionException, InvalidHandleException, RemoteException {
				try {
					session.removeObject(browserHandle);
					throw new InvalidRuleSessionException("This must be invalid!");
				} catch (InvalidRuleSessionException ex) {
					// expected
				}
				session.addObject("MSIE");
				session.executeRules();
				return session.getObjects();
			}
			
		});
	}

	/**
	 * Sets new value for field template
	 * @param template The template to set.
	 */
	public final void setTemplate(Jsr94Template template) {
		this.template = template;
	}

}
