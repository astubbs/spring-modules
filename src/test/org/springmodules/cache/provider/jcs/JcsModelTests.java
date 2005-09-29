/* 
 * JcsCachingAttributeTest.java
 * 
 * Created on Sep 24, 2004
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
 * Copyright @2004 the original author or authors.
 */

package org.springmodules.cache.provider.jcs;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.EqualsHashCodeAssert;
import org.springmodules.EqualsHashCodeTestCase;
import org.springmodules.util.Strings;

/**
 * <p>
 * Unit Tests for <code>{@link JcsModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class JcsModelTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(JcsModelTests.class);

  private JcsModel cacheModel;

  public JcsModelTests(String name) {
    super(name);
  }

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(cacheModel.getClass().getName());
    buffer.append("@" + System.identityHashCode(cacheModel) + "[");
    buffer.append("cacheName=" + Strings.quote(cacheModel.getCacheName())
        + ", ");
    buffer.append("group=" + Strings.quote(cacheModel.getGroup()) + "]");

    String expected = buffer.toString();
    String actual = cacheModel.toString();

    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);

    assertEquals(expected, actual);
  }

  protected void setUp() throws Exception {
    super.setUp();
    cacheModel = new JcsModel();
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String cacheName = "main";
    String group = "test";

    cacheModel.setCacheName(cacheName);
    cacheModel.setGroup(group);

    JcsModel anotherModel = new JcsModel(cacheName, group);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(cacheModel,
        anotherModel);

    cacheName = null;
    cacheModel.setCacheName(cacheName);
    anotherModel.setCacheName(cacheName);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(cacheModel,
        anotherModel);

    group = null;
    cacheModel.setGroup(group);
    anotherModel.setGroup(group);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(cacheModel,
        anotherModel);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String cacheName = "ch01";
    String group = "grp87";

    cacheModel.setCacheName(cacheName);
    cacheModel.setGroup(group);

    JcsModel anotherModel = new JcsModel(cacheName, group);

    assertEquals(cacheModel, anotherModel);

    anotherModel.setCacheName("main");
    assertFalse(cacheModel.equals(anotherModel));

    anotherModel.setCacheName(cacheName);
    anotherModel.setGroup("test");
    assertFalse(cacheModel.equals(anotherModel));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    EqualsHashCodeAssert.assertEqualsIsReflexive(cacheModel);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String cacheName = "mainCache";
    String group = "testGroup";

    cacheModel.setCacheName(cacheName);
    cacheModel.setGroup(group);

    JcsModel anotherModel = new JcsModel(cacheName, group);

    EqualsHashCodeAssert.assertEqualsIsSymmetric(cacheModel, anotherModel);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String cacheName = "pojos";
    String group = "model";

    cacheModel.setCacheName(cacheName);
    cacheModel.setGroup(group);

    JcsModel secondModel = new JcsModel(cacheName, group);
    JcsModel thirdModel = new JcsModel(cacheName, group);

    EqualsHashCodeAssert.assertEqualsIsTransitive(cacheModel, secondModel,
        thirdModel);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    EqualsHashCodeAssert.assertEqualsNullComparisonReturnsFalse(cacheModel);
  }

  public void testToStringWithCacheNameAndGroupEqualToNull() {
    cacheModel.setCacheName(null);
    cacheModel.setGroup(null);

    assertToStringIsCorrect();
  }

  public void testToStringWithCacheNameAndGroupNotEqualToNull() {
    cacheModel.setCacheName("main");
    cacheModel.setGroup("services");

    assertToStringIsCorrect();
  }
}