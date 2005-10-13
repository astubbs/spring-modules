/* 
 * Created on Aug 19, 2005
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
 * Copyright @2005 the original author or authors.
 */
package org.springmodules.cache.serializable;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.EqualsHashCodeAssert;
import org.springmodules.EqualsHashCodeTestCase;
import org.springmodules.cache.serializable.XStreamSerializableFactory;
import org.springmodules.cache.serializable.XStreamSerializableFactory.ObjectWrapper;
import org.springmodules.util.Strings;

/**
 * <p>
 * Unit Tests for <code>{@link XStreamSerializableFactory.ObjectWrapper}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class ObjectWrapperTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(ObjectWrapperTests.class);

  private ObjectWrapper wrapper;

  public ObjectWrapperTests(String name) {
    super(name);
  }

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(wrapper.getClass().getName());
    buffer.append("@" + System.identityHashCode(wrapper) + "[");
    buffer.append("value="
        + Strings.quoteIfString(wrapper.getValue()) + "]");

    String expected = buffer.toString();
    String actual = wrapper.toString();
    
    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);
    
    assertEquals(expected, actual);
  }

  protected void setUp() {
    wrapper = new ObjectWrapper();
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String obj = "Leia";

    wrapper.setValue(obj);
    ObjectWrapper anotherWrapper = new ObjectWrapper(obj);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        wrapper, anotherWrapper);

    obj = null;

    wrapper.setValue(obj);
    anotherWrapper.setValue(obj);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        wrapper, anotherWrapper);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    ObjectWrapper anotherWrapper = new ObjectWrapper();
    assertEquals(wrapper, anotherWrapper);

    anotherWrapper.setValue("Luke");
    assertFalse(wrapper.equals(anotherWrapper));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    EqualsHashCodeAssert.assertEqualsIsReflexive(wrapper);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    ObjectWrapper anotherWrapper = new ObjectWrapper();
    EqualsHashCodeAssert.assertEqualsIsSymmetric(wrapper, anotherWrapper);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String obj = "Han";

    wrapper.setValue(obj);
    ObjectWrapper secondWrapper = new ObjectWrapper(obj);
    ObjectWrapper thirdWrapper = new ObjectWrapper(obj);

    EqualsHashCodeAssert.assertEqualsIsTransitive(wrapper, secondWrapper,
        thirdWrapper);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    EqualsHashCodeAssert.assertEqualsNullComparisonReturnsFalse(wrapper);
  }
  
  public void testToStringWithValueBeingString() {
    wrapper.setValue("C-3PO");

    assertToStringIsCorrect();
  }

  public void testToStringWithValueNotBeingString() {
    wrapper.setValue(new Integer(10));
    
    assertToStringIsCorrect();
  }
}
