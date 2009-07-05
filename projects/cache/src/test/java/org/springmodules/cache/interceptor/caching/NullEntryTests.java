/* 
 * Created on Mar 29, 2006
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
package org.springmodules.cache.interceptor.caching;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springmodules.AbstractEqualsHashCodeTestCase;

/**
 * <p>
 * Unit Tests for <code>{@link NullObject}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class NullEntryTests extends AbstractEqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(NullEntryTests.class);

  private NullObject nullObject;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public NullEntryTests(String name) {
    super(name);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    NullObject nullObject2 = new NullObject();
    assertEqualsHashCodeRelationshipIsCorrect(nullObject, nullObject2);
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
    assertEqualsIsReflexive(nullObject);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    NullObject nullObject2 = new NullObject();
    assertEqualsIsSymmetric(nullObject, nullObject2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    NullObject nullObject2 = new NullObject();
    NullObject nullObject3 = new NullObject();
    assertEqualsIsTransitive(nullObject, nullObject2, nullObject3);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertEqualsNullComparisonReturnsFalse(nullObject);
  }

  protected void setUp() {
    nullObject = new NullObject();
  }

}
