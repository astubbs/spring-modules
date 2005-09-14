/*
 * Copyright 2004-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package org.springmodules.validation.valang;

import org.apache.commons.collections.Predicate;
import org.springframework.core.JdkVersion;
import org.springmodules.util.dateparser.DefaultDateParser;
import org.springmodules.validation.functions.Function;
import org.springmodules.validation.functions.LengthOfFunction;
import org.springmodules.validation.functions.LowerCaseFunction;
import org.springmodules.validation.functions.NotFunction;
import org.springmodules.validation.functions.ResolveFunction;
import org.springmodules.validation.functions.UpperCaseFunction;
import org.springmodules.validation.predicates.GenericTestPredicate;
import org.springmodules.validation.predicates.Operator;

/**
 * <p>Allows registration of custom functions. Custom functions can overwrite default functions.
 * 
 * <p>Default functions are:
 * 
 * <ul>
 * 	<li>len, length, size
 * 	<li>upper
 * 	<li>lower
 * 	<li>!
 * </ul> 
 * 
 * @author Steven Devijver
 * @since Apr 23, 2005
 */
public class DefaultVisitor implements ValangVisitor {

	private ValangVisitor visitor = null;
	private DefaultDateParser dateParser = null;
	
	public DefaultVisitor() {
		super();
		if (JdkVersion.getMajorJavaVersion() != JdkVersion.JAVA_13) {
			this.dateParser = new DefaultDateParser();
		}
	}

	public Function getFunction(String name, Function[] arguments, int line, int column) {
		if (getVisitor() != null) {
			Function tmpFunction = getVisitor().getFunction(name, arguments, line, column);
			if (tmpFunction != null) {
				return tmpFunction;
			}
		}
		
		if ("len".equals(name)) {
			return new LengthOfFunction(arguments, line, column);
		} else if ("length".equals(name)) {
			return new LengthOfFunction(arguments, line, column);
		} else if ("size".equals(name)) {
			return new LengthOfFunction(arguments, line, column);
		} else if ("upper".equals(name)) {
			return new UpperCaseFunction(arguments, line, column);
		} else if ("lower".equals(name)) {
			return new LowerCaseFunction(arguments, line, column);
		} else if ("!".equals(name)) {
			return new NotFunction(arguments, line, column);
		} else if ("resolve".equals(name)) {
			return new ResolveFunction(arguments, line, column);
		}

		throw new IllegalArgumentException("Could not find function [" + name + "]!");
	}

	/**
	 * <p>Register a custom visitor to look up custom functions. Lookup of functions
	 * will first be delegated to this visitor. If no function has been returned (null)
	 * lookup will be handled by DefaultVisitor.
	 * 
	 * @param visitor the custom visitor
	 */
	public void setVisitor(ValangVisitor visitor) {
		this.visitor = visitor;
	}
	
	public ValangVisitor getVisitor() {
		return this.visitor;
	}
	
	public Predicate getPredicate(Function leftFunction, Operator operator, Function rightFunction, int line, int column) {
		return new GenericTestPredicate(leftFunction, operator, rightFunction, line, column);
	}
	
	public DefaultDateParser getDateParser() {
		if (this.dateParser == null) {
			throw new IllegalStateException("Date parser not supported in Java 1.3 or older.");
		}
		return this.dateParser;
	}
}
