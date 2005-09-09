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

package org.springmodules.cache.provider.jboss;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.EqualsHashCodeAssert;
import org.springmodules.EqualsHashCodeTestCase;
import org.springmodules.cache.util.Strings;

/**
 * <p>
 * Unit Tests for <code>{@link JbossCacheProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class JbossCacheProfileTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(JbossCacheProfileTests.class);

  private JbossCacheProfile cacheProfile;

  public JbossCacheProfileTests(String name) {
    super(name);
  }

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(cacheProfile.getClass().getName());
    buffer.append("@" + System.identityHashCode(cacheProfile) + "[");
    buffer.append("nodeFqn=" + Strings.quote(cacheProfile.getNodeFqn()) + "]");

    String expected = buffer.toString();
    String actual = cacheProfile.toString();

    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);

    assertEquals(expected, actual);
  }

  protected final void setUp() throws Exception {
    super.setUp();

    cacheProfile = new JbossCacheProfile();
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String nodeFqn = "a/b/c/d/e";

    cacheProfile.setNodeFqn(nodeFqn);

    JbossCacheProfile anotherProfile = new JbossCacheProfile(nodeFqn);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        cacheProfile, anotherProfile);

    nodeFqn = null;
    cacheProfile.setNodeFqn(nodeFqn);
    anotherProfile.setNodeFqn(nodeFqn);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        cacheProfile, anotherProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String nodeFqn = "a/b";

    cacheProfile.setNodeFqn(nodeFqn);

    JbossCacheProfile anotherProfile = new JbossCacheProfile(nodeFqn);

    assertEquals(cacheProfile, anotherProfile);

    anotherProfile.setNodeFqn("a/b/c/d");
    assertFalse(cacheProfile.equals(anotherProfile));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    EqualsHashCodeAssert.assertEqualsIsReflexive(cacheProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String nodeFqn = "a";

    cacheProfile.setNodeFqn(nodeFqn);

    JbossCacheProfile anotherProfile = new JbossCacheProfile(nodeFqn);

    EqualsHashCodeAssert.assertEqualsIsSymmetric(cacheProfile, anotherProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String nodeFqn = "a/b/c";

    cacheProfile.setNodeFqn(nodeFqn);

    JbossCacheProfile secondProfile = new JbossCacheProfile(nodeFqn);
    JbossCacheProfile thirdProfile = new JbossCacheProfile(nodeFqn);

    EqualsHashCodeAssert.assertEqualsIsTransitive(cacheProfile, secondProfile,
        thirdProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    EqualsHashCodeAssert.assertEqualsNullComparisonReturnsFalse(cacheProfile);
  }

  public void testToStringWithNodeFqnEqualToNull() {
    cacheProfile.setNodeFqn(null);

    assertToStringIsCorrect();
  }

  public void testToStringWithNodeFqnNotEqualToNull() {
    cacheProfile.setNodeFqn("x/y/z");

    assertToStringIsCorrect();
  }
}