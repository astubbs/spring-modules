/**
 * 
 */
package org.springmodules.jsr94.core;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import javax.rules.InvalidRuleSessionException;
import javax.rules.ObjectFilter;
import javax.rules.StatelessRuleSession;

import org.springframework.beans.factory.InitializingBean;
import org.springmodules.jsr94.rulesource.RuleSource;
import org.springmodules.jsr94.support.StatelessRuleSessionCallback;

/**
 * Convenience [super]class that simplifies usage of rules in the business layer of a
 * Spring application. You can either directly subclass this class 
 * <code>
 * public class BusinessBean extends Jsr94RuleSupport {
 *    // ...
 * }
 * </code>
 * or use it as Spring-configured bean injected into your business logic bean.
 * The class provides overloaded executeStateless method to execute the rules and return the results with no
 * additional coding required.
 * 
 * @see #executeStateless(String, List)
 * @see #executeStateless(String, List, ObjectFilter)
 * @see #executeStateless(String, Map, List, ObjectFilter)
 * @author janm
 */
public class Jsr94RuleSupport implements InitializingBean {
	/**
	 * The ruleSource to be used
	 */
	private RuleSource ruleSource;

	/**
	 * The Jsr94Template to be used
	 */
	private Jsr94Template template;

	/**
	 * Subclasses can override this method to perform additional initialization. This method
	 * will be called as last in the afterPropertiesSet.
	 * @throws Exception If the initialization fails
	 * @see #afterPropertiesSet() 
	 */
	protected void initRuleSupport() throws Exception {
		// noop
	}

	/**
	 * Gets the ruleSource
	 * @return Valid RuleSource implementation
	 * @see RuleSource
	 */
	protected final RuleSource getRuleSource() {
		return ruleSource;
	}

	/**
	 * Gets the template
	 * @return Valid Jsr94Template instance
	 * @see Jsr94Template
	 */
	protected Jsr94Template getTemplate() {
		return template;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		// we can have the template or ruleSource set, but not both or none
		if (ruleSource == null && template == null) throw new IllegalArgumentException("Must set either ruleSource or template on " + getClass().getName());
		if (ruleSource != null && template != null) throw new IllegalArgumentException("Must set either ruleSource or template on " + getClass().getName() + ", not both");
		if (ruleSource != null /* && template == null */) {
			template = new Jsr94Template(ruleSource);
		}
		if (template != null /* && ruleSource == null */) {
			ruleSource = template.getRuleSource();
		}
		initRuleSupport();
	}
	
	/**
	 * Executes rules given the uri, properties, list of input objects and optional filter
	 * @param uri The ruleset uri
	 * @param properties The ruleset execution properties
	 * @param input The input facts
	 * @param filter The object filter (can be null)
	 * @return List of inferred facts
	 * @see #executeStateless(String, List)
	 * @see #executeStateless(String, List, ObjectFilter)
	 */
	protected List executeStateless(final String uri, final Map properties, final List input, final ObjectFilter filter) {
		return (List)template.executeStateless(uri, properties, new StatelessRuleSessionCallback() {

			public Object execute(StatelessRuleSession session) throws InvalidRuleSessionException, RemoteException {
				List result;
				if (filter != null) {
					result = session.executeRules(input, filter);
				} else {
					result = session.executeRules(input);
				}
				
				return result;
			}
			
		});
	}

	/**
	 * Executes rules given the uri, list of input facts and optional filter
	 * @param uri The ruleset uri
	 * @param input The input facts
	 * @param filter The object filter (can be null)
	 * @return List of inferred facts
	 * @see #executeStateless(String, Map, List, ObjectFilter)
	 * @see #executeStateless(String, List)
	 */
	protected List executeStateless(String uri, List input, ObjectFilter filter) {
		return executeStateless(uri, null, input, filter);
	}
	
	/**
	 * Executes rules given the uri and list of input facts
	 * @param uri The ruleset uri
	 * @param input The input facts
	 * @return List of inferred objects
	 * @see #executeStateless(String, List, ObjectFilter)
	 * @see #executeStateless(String, Map, List, ObjectFilter)
	 */
	protected List executeStateless(String uri, List input) {
		return executeStateless(uri, null, input, null);
	}
	
	/**
	 * Sets new value for field ruleSource
	 * @param ruleSource The ruleSource to set.
	 */
	public final void setRuleSource(RuleSource ruleSource) {
		this.ruleSource = ruleSource;
	}

	/**
	 * Sets new value for field template
	 * @param template The template to set.
	 */
	public final void setTemplate(Jsr94Template template) {
		this.template = template;
	}

}
