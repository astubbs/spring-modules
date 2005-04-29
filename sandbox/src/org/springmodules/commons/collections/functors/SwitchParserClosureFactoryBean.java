package org.springmodules.commons.collections.functors;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.SwitchClosure;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.validation.valang.ValangParser;

/**
 * <p>SwitchParserClosureFactoryBean evaluates rules and executes the associated
 * closure if the evaluation returns true. The syntax is based on Valang expressions.
 * 
 * @author Steven Devijver
 * @since 29-04-2005
 * @see org.apache.commons.collections.functors.SwitchClosure
 */
public class SwitchParserClosureFactoryBean implements FactoryBean, InitializingBean {

	private Map rulesAndClosures = null;
	private Closure closure = null;
	
	public SwitchParserClosureFactoryBean() {
		super();
	}

	/**
	 * <p>Sets the rules and closures (required).
	 * 
	 * @param rulesAndClosures rules and closures
	 */
	public void setRulesAndClosures(Map rulesAndClosures) {
		this.rulesAndClosures = rulesAndClosures;
	}
	
	private Map getRulesAndClosures() {
		if (this.rulesAndClosures == null) {
			throw new IllegalArgumentException("[rulesAndClosures] property must be set!");
		}
		return this.rulesAndClosures;
	}
	
	public Object getObject() throws Exception {
		return this.closure;
	}

	public Class getObjectType() {
		return Closure.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		Map predicatesAndClosures = new HashMap();
		for (Iterator iter = getRulesAndClosures().keySet().iterator(); iter.hasNext();) {
			String rule = (String)iter.next();
			Predicate predicate = new ValangParser(new StringReader(rule)).parseExpression();
			predicatesAndClosures.put(predicate, getRulesAndClosures().get(rule));
		}
		this.closure = SwitchClosure.getInstance(predicatesAndClosures);
	}

}
