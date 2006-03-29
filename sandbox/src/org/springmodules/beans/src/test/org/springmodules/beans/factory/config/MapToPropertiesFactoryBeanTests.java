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
package org.springmodules.beans.factory.config;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * 
 * 
 * @author Steven Devijver
 * @since Aug 6, 2005
 */
public class MapToPropertiesFactoryBeanTests extends TestCase {

	public MapToPropertiesFactoryBeanTests() {
		super();
	}

	public MapToPropertiesFactoryBeanTests(String arg0) {
		super(arg0);
	}

	public void testSuccessAllStrings() throws Exception {
		Map map = new HashMap();
		map.put("test 1", "test 2");
		map.put("test 3", "test 4");
		MapToPropertiesFactoryBean m2pfb = new MapToPropertiesFactoryBean();
		m2pfb.setMap(map);
		m2pfb.afterPropertiesSet();
	}
	
	public void testFailKeyOtherObject() throws Exception {
		Map map = new HashMap();
		map.put(new Object(), "test1");
		MapToPropertiesFactoryBean m2pfb = new MapToPropertiesFactoryBean();
		m2pfb.setMap(map);
		try {
			m2pfb.afterPropertiesSet();
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	public void testFailValueOtherObject() throws Exception {
		Map map = new HashMap();
		map.put("test 1", new Object());
		MapToPropertiesFactoryBean m2pfb = new MapToPropertiesFactoryBean();
		m2pfb.setMap(map);
		try {
			m2pfb.afterPropertiesSet();
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

}
