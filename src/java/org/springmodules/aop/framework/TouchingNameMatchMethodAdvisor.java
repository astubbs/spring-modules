package org.springmodules.aop.framework;

import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>Convenience class based on NameMatchMethodPointcutAdvisor that
 * has a TouchingAfterReturningAdvice advice.
 * 
 * @author Steven Devijver
 * @since 21-06-2005
 * @see org.springframework.aop.support.NameMatchMethodPointcutAdvisor
 * @see org.springmodules.aop.framework.TouchingAfterReturningAdvice
 */
public class TouchingNameMatchMethodAdvisor extends
		NameMatchMethodPointcutAdvisor {

	private TouchingAfterReturningAdvice advice = null;
	
	public TouchingNameMatchMethodAdvisor() {
		super();
		this.advice = new TouchingAfterReturningAdvice();
		setAdvice(advice);
	}
	
	/**
	 * <p>Property to access the TouchingAfterReturningAdvice
	 * configured with this advisor.
	 * 
	 * <p>To access the [properties] and [ognl] properties of
	 * TouchingAfterReturningAdvice use <code>advice.properties</code>
	 * and <code>advice.ognl</code>.
	 * 
	 * @return the TouchingAfterReturningAdvice instance
	 */
	public TouchingAfterReturningAdvice getAdvice() {
		return this.advice;
	}
}
