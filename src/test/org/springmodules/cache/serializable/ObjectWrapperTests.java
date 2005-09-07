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
import org.springmodules.cache.util.Strings;

/**
 * <p>
 * Unit Tests for <code>{@link XStreamSerializableFactory.ObjectWrapper}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class ObjectWrapperTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(ObjectWrapperTests.class);

  private ObjectWrapper wrapper;

  public ObjectWrapperTests(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.wrapper = new ObjectWrapper();
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String obj = "Leia";

    this.wrapper.setValue(obj);
    ObjectWrapper anotherWrapper = new ObjectWrapper(obj);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        this.wrapper, anotherWrapper);

    obj = null;

    this.wrapper.setValue(obj);
    anotherWrapper.setValue(obj);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        this.wrapper, anotherWrapper);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    ObjectWrapper anotherWrapper = new ObjectWrapper();
    assertEquals(this.wrapper, anotherWrapper);

    anotherWrapper.setValue("Luke");
    assertFalse(this.wrapper.equals(anotherWrapper));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    EqualsHashCodeAssert.assertEqualsIsReflexive(this.wrapper);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    ObjectWrapper anotherWrapper = new ObjectWrapper();
    EqualsHashCodeAssert.assertEqualsIsSymmetric(this.wrapper, anotherWrapper);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String obj = "Han";

    this.wrapper.setValue(obj);
    ObjectWrapper secondWrapper = new ObjectWrapper(obj);
    ObjectWrapper thirdWrapper = new ObjectWrapper(obj);

    EqualsHashCodeAssert.assertEqualsIsTransitive(this.wrapper, secondWrapper,
        thirdWrapper);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    EqualsHashCodeAssert.assertEqualsNullComparisonReturnsFalse(this.wrapper);
  }

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(this.wrapper.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.wrapper) + "[");
    buffer.append("value="
        + Strings.quoteIfString(this.wrapper.getValue()) + "]");

    String expected = buffer.toString();
    String actual = this.wrapper.toString();
    
    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);
    
    assertEquals(expected, actual);
  }
  
  public void testToStringWithValueBeingString() {
    this.wrapper.setValue("C-3PO");

    assertToStringIsCorrect();
  }

  public void testToStringWithValueNotBeingString() {
    this.wrapper.setValue(new Integer(10));
    
    assertToStringIsCorrect();
  }
}
