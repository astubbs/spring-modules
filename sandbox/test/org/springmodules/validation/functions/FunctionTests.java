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
package org.springmodules.validation.functions;

import org.springframework.util.Assert;
import org.springmodules.beans.factory.drivers.LiteralValue;
import org.springmodules.validation.ValangException;

import junit.framework.TestCase;

public class FunctionTests extends TestCase {

	public FunctionTests() {
		super();
	}

	public FunctionTests(String arg0) {
		super(arg0);
	}

	private Function getLengthOfFunction(Object value) {
		return new LengthOfFunction(new Function[] { new LiteralFunction(value) }, 1, 1);
	}
	
	private Function getUpperCaseFunction(Object value) {
		return new UpperCaseFunction(new Function[] { new LiteralFunction(value) }, 1, 1);
	}
	
	private Function getLowerCaseFunction(Object value) {
		return new LowerCaseFunction(new Function[] { new LiteralFunction(value) }, 1, 1);
	}
	
	private Function getNotFunction(Object value) {
		return new NotFunction(new Function[] { new LiteralFunction(value) }, 1, 1);
	}
	
	public void testLengthOfFunctionSuccess() {
		Integer result = (Integer)getLengthOfFunction("test").getResult(null);
		assertEquals(result.intValue(), 4);
	}
	
	public void testLengthOfFunctionFail() {
		try {
			Integer result = (Integer)getLengthOfFunction(null).getResult(null);
			fail("LengthOfFunction should throw ValangException!");
		} catch (ValangException e) {
			Assert.isInstanceOf(NullPointerException.class, e.getCause(), "Cause is not NullPointerException!");
		}
	}
	
	public void testUpperCaseFunctionSuccess() {
		String result = (String)getUpperCaseFunction("test").getResult(null);
		assertEquals("TEST", result);
	}
	
	public void testUpperCaseFunctionFail() {
		try {
			getUpperCaseFunction(null).getResult(null);
			fail("UpperCaseFunction should throw ValangException!");
		} catch (ValangException e) {
			Assert.isInstanceOf(NullPointerException.class, e.getCause(), "Cause is not NullPointerException!");
		}
	}
	
	public void testLowerCaseFunctionSuccess() {
		String result = (String)getLowerCaseFunction("tEst").getResult(null);
		assertEquals("test", result);
	}
	
	public void testLowerCaseFunctionFail() {
		try {
			getLowerCaseFunction(null).getResult(null);
			fail("LowerCaseFunction should throw ValangException!");
		} catch (ValangException e) {
			Assert.isInstanceOf(NullPointerException.class, e.getCause(), "Cause is not NullPointerException!");
		}
	}

	public void testNotFunctionSuccess() {
		Boolean result = (Boolean)getNotFunction(Boolean.FALSE).getResult(null);
		assertEquals(Boolean.TRUE, result);
	}
}
