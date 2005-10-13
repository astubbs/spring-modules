/* 
 * Created on Jan 17, 2005
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

package org.springmodules.cache.interceptor.flush;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.EqualsHashCodeAssert;
import org.springmodules.EqualsHashCodeTestCase;
import org.springmodules.util.Strings;

/**
 * <p>
 * Unit Tests for <code>{@link FlushCache}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class FlushCacheTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(FlushCacheTests.class);

  private FlushCache attribute;

  public FlushCacheTests(String name) {
    super(name);
  }

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(attribute.getClass().getName());
    buffer.append("@" + System.identityHashCode(attribute) + "[");
    buffer.append("modelId=" + Strings.quote(attribute.getModelId()) + "]");

    String expected = buffer.toString();
    String actual = attribute.toString();

    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);

    assertEquals(expected, actual);
  }

  protected void setUp() {
    attribute = new FlushCache();
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String modelId = "main";
    attribute.setModelId(modelId);

    FlushCache attribute2 = new FlushCache(modelId);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(attribute,
        attribute2);

    attribute.setModelId(null);
    attribute2.setModelId(null);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(attribute,
        attribute2);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String modelId = "test";
    attribute.setModelId(modelId);

    FlushCache attribute2 = new FlushCache(modelId);
    assertEquals(attribute, attribute2);

    attribute2.setModelId("main");
    assertFalse(attribute.equals(attribute2));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    EqualsHashCodeAssert.assertEqualsIsReflexive(attribute);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String modelId = "service";
    attribute.setModelId(modelId);

    FlushCache attribute2 = new FlushCache(modelId);

    EqualsHashCodeAssert.assertEqualsIsSymmetric(attribute, attribute2);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String modelId = "pojo";
    attribute.setModelId(modelId);

    FlushCache attribute2 = new FlushCache(modelId);
    FlushCache attribute3 = new FlushCache(modelId);

    EqualsHashCodeAssert.assertEqualsIsTransitive(attribute, attribute2,
        attribute3);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    EqualsHashCodeAssert.assertEqualsNullComparisonReturnsFalse(attribute);
  }

  public void testToString() {
    attribute.setModelId("main");
    assertToStringIsCorrect();
  }

  public void testToStringWithModelIdEqualToNull() {
    attribute.setModelId(null);
    assertToStringIsCorrect();
  }
}