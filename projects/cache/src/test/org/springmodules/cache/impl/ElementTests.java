/* 
 * Created on Apr 5, 2006
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2006 the original author or authors.
 */
package org.springmodules.cache.impl;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.AbstractEqualsHashCodeTestCase;

/**
 * <p>
 * Unit Tests for <code>{@link Element}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class ElementTests extends AbstractEqualsHashCodeTestCase {

	private static Log logger = LogFactory.getLog(ElementTests.class);

	private Element element;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *          the name of the test case
	 */
	public ElementTests(String name) {
		super(name);
	}

	public void testClone() {
		Element clone = (Element) element.clone();
		assertCloneIsCorrect(element, clone);
	}

	public void testCloneWithElementHavingValueEqualToNull() {
		element.setValue(null);
		Element clone = (Element) element.clone();
		assertCloneIsCorrect(element, clone);
	}

	public void testConstructorCreatesCopyOfKeyAndValueAndInitializesCreationTime() {
		String key = "myKey";
		String value = "myValue";
		long currentTime = System.currentTimeMillis();

		element = new Element(key, value);
		assertEquals("<key>", key, element.getKey());
		assertEquals("<value>", value, element.getValue());

		assertTrue("the key should be a copy of the original key", key != element
				.getKey());
		assertTrue("the value should be a copy of the original value",
				value != element.getValue());

		assertTrue("the creation time has not been set",
				element.getCreationTime() >= currentTime);
	}

	/**
	 * @see org.springmodules.EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
	 */
	public void testEqualsHashCodeRelationship() {
		Element element2 = new Element(element.getKey(), element.getValue());
		assertEqualsHashCodeRelationshipIsCorrect(element, element2);

		String newValue = "newValue";
		element.setValue(newValue);
		element2.setValue(newValue);
		assertEqualsHashCodeRelationshipIsCorrect(element, element2);
	}

	/**
	 * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsConsistent()
	 */
	public void testEqualsIsConsistent() {
		logger.info("It is not necessary to test consistency of method 'equals.' "
				+ "The state of a <" + Element.class.getName()
				+ "> only depends on an inmutable value: the field 'key.'");
	}

	/**
	 * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsReflexive()
	 */
	public void testEqualsIsReflexive() {
		assertEqualsIsReflexive(element);
	}

	/**
	 * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsSymmetric()
	 */
	public void testEqualsIsSymmetric() {
		Element element2 = new Element(element.getKey(), element.getValue());
		assertEqualsIsSymmetric(element, element2);
	}

	/**
	 * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsTransitive()
	 */
	public void testEqualsIsTransitive() {
		Element element2 = new Element(element.getKey(), element.getValue());
		Element element3 = new Element(element.getKey(), element.getValue());
		assertEqualsIsTransitive(element, element2, element3);
	}

	/**
	 * @see org.springmodules.EqualsHashCodeTestCase#testEqualsNullComparison()
	 */
	public void testEqualsNullComparison() {
		assertEqualsNullComparisonReturnsFalse(element);
	}

	public void testIsAliveWithAliveElement() {
		createAliveElement();
		assertTrue(element.isAlive());
	}

	public void testIsAliveWithExpiredElement() {
		createExpiredElement();
		assertFalse(element.isAlive());
	}

	public void testIsAliveWithNeverExpiringElement() {
		assertTrue(element.isAlive());
	}

	public void testIsExpiredWithAliveElement() {
		createAliveElement();
		assertFalse(element.isExpired());
	}

	public void testIsExpiredWithExpiredElement() {
		createExpiredElement();
		assertTrue(element.isExpired());
	}

	public void testIsExpiredWithNeverExpiringElement() {
		assertFalse(element.isExpired());
	}

	protected void setUp() throws Exception {
		element = new Element("key", "value");
	}

	private void assertCloneIsCorrect(Element original, Element clone) {
		Serializable originalKey = original.getKey();
		Serializable originalValue = original.getValue();
		Serializable clonedKey = clone.getKey();
		Serializable clonedValue = clone.getValue();

		assertEquals(originalKey, clonedKey);
		assertEquals(originalValue, clonedValue);
		assertEquals(original.getCreationTime(), clone.getCreationTime());

		boolean sameKeys = originalKey == clonedKey && originalKey != null;
		assertFalse("Keys should not be same", sameKeys);

		boolean sameValues = originalValue == clonedValue && originalValue != null;
		assertFalse("Values should not be same", sameValues);
	}

	private void createAliveElement() {
		element = new Element("myKey", "myElement", Long.MAX_VALUE);
		try {
			Thread.sleep(5);
		} catch (InterruptedException exception) {
			exception.printStackTrace();
		}
	}

	private void createExpiredElement() {
		element = new Element("myKey", "myElement", 50);
		try {
			Thread.sleep(100);
		} catch (InterruptedException exception) {
			exception.printStackTrace();
		}
	}
}
