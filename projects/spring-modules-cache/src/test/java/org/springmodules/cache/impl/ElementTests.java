
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
import org.springmodules.AssertExt;
import org.springmodules.cache.serializable.SerializationAssert;

/**
 * <p>
 * Unit Tests for <code>{@link Element}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class ElementTests extends AbstractEqualsHashCodeTestCase {

  private static final String ELEMENT_KEY = "myKey";

  private static final String ELEMENT_VALUE = "myValue";

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
    assertEqualsButNotSame(element, clone);
  }

  public void testCloneWithElementHavingValueEqualToNull() {
    element.setValue(null);
    Element clone = (Element) element.clone();
    assertEqualsButNotSame(element, clone);
  }
  
  public void testConstructorCreatesCopyOfKeyAndValueAndInitializesCreationTime() {
    String key = ELEMENT_KEY;
    String value = ELEMENT_VALUE;
    long currentTime = System.currentTimeMillis();

    element = new Element(key, value);
    Serializable copiedKey = element.getKey();
    Serializable copiedValue = element.getValue();

    assertEquals("<key>", key, copiedKey);
    assertEquals("<value>", value, copiedValue);

    assertTrue("the key should be a copy of the original, not the same",
        key != copiedKey);
    assertTrue("the value should be a copy of the original, not the same",
        value != copiedValue);

    assertTrue("the creation time has not been set",
        element.getCreationTime() >= currentTime);
  }

  public void testConstructorWithNegativeTimeToLiveNotEqualToExpiryNever() {
    assertDefaultTimeToLiveIsSet(-3l);
  }

  public void testConstructorWithTimeToLiveEqualToExpiryNever() {
    long expiryNever = -1l;
    element = new Element(ELEMENT_KEY, ELEMENT_VALUE, expiryNever);
    assertEquals("time to live should be equal to 'EXPIRY_NEVER'", expiryNever,
        element.getTimeToLive());
  }

  public void testConstructorWithTimeToLiveEqualToZero() {
    assertDefaultTimeToLiveIsSet(0l);
  }

  public void testConstructorWithTimeToPositiveLive() {
    long timeToLive = 45000l;
    element = new Element(ELEMENT_KEY, ELEMENT_VALUE, timeToLive);
    assertEquals("<time to live>", timeToLive, element.getTimeToLive());
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    Element element2 = new Element(element.getKey(), element.getValue());
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

  public void testSerialization() throws Exception {
    Serializable deserialized = SerializationAssert.copy(element);
    AssertExt.assertInstanceOf(Element.class, deserialized);
    assertEqualsButNotSame(element, (Element) deserialized);
  }

  protected void setUp() throws Exception {
    element = new Element(ELEMENT_KEY, ELEMENT_VALUE);
  }

  private void assertDefaultTimeToLiveIsSet(long specifiedTimeToLive) {
    element = new Element(ELEMENT_KEY, ELEMENT_VALUE, specifiedTimeToLive);
    assertEquals("<timeToLive>", 120000l, element.getTimeToLive());
  }

  private void assertEqualsButNotSame(Element original, Element clone) {
    assertTrue("given elements should not be the same", original != clone);
    
    Serializable originalKey = original.getKey();
    Serializable originalValue = original.getValue();
    Serializable clonedKey = clone.getKey();
    Serializable clonedValue = clone.getValue();

    assertEquals(originalKey, clonedKey);
    assertEquals(originalValue, clonedValue);
    assertEquals(original.getCreationTime(), clone.getCreationTime());
    assertEquals(original.getTimeToLive(), clone.getTimeToLive());

    boolean sameKeys = originalKey == clonedKey && originalKey != null;
    assertFalse("keys should not be same", sameKeys);

    boolean sameValues = originalValue == clonedValue && originalValue != null;
    assertFalse("values should not be same", sameValues);
  }

  private void createAliveElement() {
    element = new Element(ELEMENT_KEY, ELEMENT_VALUE, Long.MAX_VALUE);
    try {
      Thread.sleep(5);
    } catch (InterruptedException exception) {
      exception.printStackTrace();
    }
  }

  private void createExpiredElement() {
    element = new Element(ELEMENT_KEY, ELEMENT_VALUE, 50);
    try {
      Thread.sleep(100);
    } catch (InterruptedException exception) {
      exception.printStackTrace();
    }
  }
}
