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
 * Copyright @2007 the original author or authors.
 */
package org.springmodules.cache.serializable;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springmodules.AbstractEqualsHashCodeTestCase;
import org.springmodules.cache.serializable.XStreamSerializableFactory.ObjectWrapper;

/**
 * Unit Tests for <code>{@link XStreamSerializableFactory}</code>.
 *
 * @author Omar Irbouh
 * @author Alex Ruiz
 */
public class XStreamSerializableFactoryTests extends
    AbstractEqualsHashCodeTestCase {

  private static Log logger = LogFactory
      .getLog(XStreamSerializableFactoryTests.class);

  /**
   * Non-serializable object.
   */
  private Puppy puppy;

  private XStreamSerializableFactory serializableFactory;

  public XStreamSerializableFactoryTests(String name) {
    super(name);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    XStreamSerializableFactory factory2 = new XStreamSerializableFactory();
    assertEqualsHashCodeRelationshipIsCorrect(serializableFactory, factory2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    logger.info("Test 'testEqualsIsConsistent' is not necessary "
        + "since equality is not based on state");
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    assertEqualsIsReflexive(serializableFactory);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    XStreamSerializableFactory factory2 = new XStreamSerializableFactory();
    assertEqualsIsSymmetric(serializableFactory, factory2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    XStreamSerializableFactory factory2 = new XStreamSerializableFactory();
    XStreamSerializableFactory factory3 = new XStreamSerializableFactory();
    assertEqualsIsTransitive(serializableFactory, factory2, factory3);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertEqualsNullComparisonReturnsFalse(serializableFactory);
  }

  public void testGetOriginalValueWithArgumentBeingObjectWrapper() {
	XStream xstream = new XStream();
	ObjectWrapper wrapper = new ObjectWrapper(xstream.toXML(puppy));
    assertEquals(puppy, serializableFactory.getOriginalValue(wrapper));
  }

  public void testGetOriginalValueWithArgumentEqualToNull() {
    assertNull(serializableFactory.getOriginalValue(null));
  }

  public void testGetOriginalValueWithArgumentNotBeingObjectWrapper() {
    Object obj = "R2-D2";
    assertSame(obj, serializableFactory.getOriginalValue(obj));
  }

  public void testMakeSerializableIfNecessaryWithArgumentEqualToNull() {
    assertNull(serializableFactory.makeSerializableIfNecessary(null));
  }

  public void testMakeSerializableIfNecessaryWithNotSerializableArgument()
      throws Exception {
    XStream xstream = new XStream();
    ObjectWrapper expected = new ObjectWrapper(xstream.toXML(puppy));

    Object actual = serializableFactory.makeSerializableIfNecessary(puppy);

    assertEquals(expected, actual);
    SerializationAssert.assertIsSerializable(actual);
  }

  public void testMakeSerializableIfNecessaryWithSerializableArgument() {
    Object obj = "Luke Skywalker";
    Object actual = serializableFactory.makeSerializableIfNecessary(obj);
    assertSame(obj, actual);
  }

  protected void setUp() {
    serializableFactory = new XStreamSerializableFactory();
    puppy = new Puppy("Scooby");
  }

}