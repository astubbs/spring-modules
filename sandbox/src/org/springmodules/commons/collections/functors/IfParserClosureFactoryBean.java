package org.springmodules.commons.collections.functors;

import java.io.StringReader;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.functors.IfClosure;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.commons.collections.functors.parser.Parser;

/**
 * <p>Closure that takes a decision syntax and a true and false closure.
 * 
 * <p>If the syntax evaluates to true the true closure is executed, otherwise
 * the false closure is executed.
 * 
 * <p>This is the single building block of a very simple rule engine.
 * 
 * @author Steven Devijver
 * @since 28-04-2005
 * @see org.apache.commons.collections.functors.IfClosure
 */
public class IfParserClosureFactoryBean
		implements
			FactoryBean,
			InitializingBean {

	private String syntax = null;
	private Closure trueClosure = null;
	private Closure falseClosure = new Closure() { public void execute(Object target) {} };
	private Closure closure = null;
	
	public IfParserClosureFactoryBean() {
		super();
	}

	/**
	 * <p>The decision syntax property (required).
	 * 
	 * @param syntax the decision syntax
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
	
	/**
	 * <p>The closure that is executed when the syntax evaluates to true (required).
	 * 
	 * @param trueClosure the true closure
	 */
	public void setTrueClosure(Closure trueClosure) {
		this.trueClosure = trueClosure;
	}
	
	private Closure getTrueClosure() {
		if (this.trueClosure == null) {
			throw new IllegalArgumentException("[trueClosure] property must be set!");
		}
		return this.trueClosure;
	}
	
	/**
	 * <p>The closure that is executed when the syntax evaluates to false (required).
	 * 
	 * <p>A default empty closure is provided for the false closure.
	 * 
	 * @param falseClosure the false closure
	 */
	public void setFalseClosure(Closure falseClosure) {
		this.falseClosure = falseClosure;
	}
	
	private Closure getFalseClosure() {
		if (this.falseClosure == null) {
			throw new IllegalArgumentException("[falseClosure] property must be set!");
		}
		return this.falseClosure;
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
		this.closure = IfClosure.getInstance(new Parser(new StringReader(getSyntax())).parse(), getTrueClosure(), getFalseClosure());
	}

}
