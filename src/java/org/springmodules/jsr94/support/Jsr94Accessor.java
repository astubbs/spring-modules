/**
 * 
 */
package org.springmodules.jsr94.support;

import org.springframework.beans.factory.InitializingBean;
import org.springmodules.jsr94.rulesource.RuleSource;

/**
 * @author janm
 *
 */
public abstract class Jsr94Accessor implements InitializingBean {

	/**
	 * The ruleSource instance
	 */
	private RuleSource ruleSource;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		if (ruleSource == null) throw new IllegalArgumentException("Must set ruleSource on " + getClass().getName());
	}

	/**
	 * Gets the value of ruleSource 
	 * @return Value of ruleSource.
	 */
	public final RuleSource getRuleSource() {
		return ruleSource;
	}

	/**
	 * Sets new value for field ruleSource
	 * @param ruleSource The ruleSource to set.
	 */
	public final void setRuleSource(RuleSource ruleSource) {
		this.ruleSource = ruleSource;
	}

}
