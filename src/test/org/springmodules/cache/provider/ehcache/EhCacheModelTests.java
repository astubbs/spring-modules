/* 
 * Created on Oct 29, 2004
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

package org.springmodules.cache.provider.ehcache;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.EqualsHashCodeAssert;
import org.springmodules.EqualsHashCodeTestCase;
import org.springmodules.util.Strings;

/**
 * <p>
 * Unit Tests for <code>{@link EhCacheModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class EhCacheModelTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(EhCacheModelTests.class);

  private EhCacheModel cacheModel;

  public EhCacheModelTests(String name) {
    super(name);
  }

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(cacheModel.getClass().getName());
    buffer.append("@" + System.identityHashCode(cacheModel) + "[");
    buffer
        .append("cacheName=" + Strings.quote(cacheModel.getCacheName()) + "]");

    String expected = buffer.toString();
    String actual = cacheModel.toString();

    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);

    assertEquals(expected, actual);
  }

  protected final void setUp() throws Exception {
    super.setUp();

    cacheModel = new EhCacheModel();
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String cacheName = "main";

    cacheModel.setCacheName(cacheName);

    EhCacheModel anotherModel = new EhCacheModel(cacheName);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(cacheModel,
        anotherModel);

    cacheName = null;
    cacheModel.setCacheName(cacheName);
    anotherModel.setCacheName(cacheName);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(cacheModel,
        anotherModel);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String cacheName = "test";

    cacheModel.setCacheName(cacheName);

    EhCacheModel anotherModel = new EhCacheModel(cacheName);

    assertEquals(cacheModel, anotherModel);

    anotherModel.setCacheName("main");
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
    String cacheName = "test";

    cacheModel.setCacheName(cacheName);

    EhCacheModel anotherModel = new EhCacheModel(cacheName);

    EqualsHashCodeAssert.assertEqualsIsSymmetric(cacheModel, anotherModel);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String cacheName = "test";

    cacheModel.setCacheName(cacheName);

    EhCacheModel secondModel = new EhCacheModel(cacheName);
    EhCacheModel thirdModel = new EhCacheModel(cacheName);

    EqualsHashCodeAssert.assertEqualsIsTransitive(cacheModel, secondModel,
        thirdModel);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    EqualsHashCodeAssert.assertEqualsNullComparisonReturnsFalse(cacheModel);
  }

  public void testToStringWithCacheNameEqualToNull() {
    cacheModel.setCacheName(null);

    assertToStringIsCorrect();
  }

  public void testToStringWithCacheNameNotEqualToNull() {
    cacheModel.setCacheName("main");

    assertToStringIsCorrect();
  }
}