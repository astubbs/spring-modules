package org.springmodules.commons.collections.functors;

import java.io.StringReader;

import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.commons.collections.functors.parser.Parser;

/**
 * <p>ParserPredicateFactoryBean creates a predicate based on a simple syntax.
 * 
 * <p>The syntax evaluates properties of a target bean. Examples are:
 * 
 * <pre>
 * 	total &gt;= 500 or (total &gt;= 250 and customer.since &lt; [T-2y]) 
 * </pre>
 * 
 * <p>This scenario decides if a reduction is granted on an order if the
 * total amount is 500 or more or in case the customer has been created more
 * than 2 years ago if the amount is 250 or more.
 * 
 * <pre>
 * 	creationDate between [T&lt;d] and [T&gt;d-1S] 
 * </pre>
 * 
 * <p>This scenario decides if an order has been created today.
 * 
 * <p>For a complete overview of the syntax please take a look at the Valang validator.
 * 
 * <p>If you are planning to create a rule engine this might be the predicate you
 * were looking for. Also, if you want to externalize your decision logic this tool
 * might be useful.
 * 
 * @author Steven Devijver
 * @since 28-04-2005
 * @see org.apache.commons.collections.Predicate
 */
public class ParserPredicateFactoryBean
		implements
			FactoryBean,
			InitializingBean {

	private String syntax = null;
	private Predicate predicate = null;
	
	public ParserPredicateFactoryBean() {
		super();
	}

	/**
	 * <p>The syntax property (required).
	 * 
	 * @param syntax the syntax
	 */
	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}
	
	private String getSyntax() {
		if (this.syntax == null || this.syntax.length() == 0) {
			throw new IllegalArgumentException("[syntax] property must be set!");
		}
		return this.syntax;
	}
	
	public Object getObject() throws Exception {
		return this.predicate;
	}

	public Class getObjectType() {
		return Predicate.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		this.predicate = new Parser(new StringReader(getSyntax())).parse();
	}

}
