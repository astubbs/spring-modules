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
import org.springmodules.util.Strings;

/**
 * <p>
 * Unit Tests for <code>{@link JbossCacheCachingModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class JbossCacheCachingModelTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(JbossCacheCachingModelTests.class);

  private JbossCacheCachingModel cacheModel;

  public JbossCacheCachingModelTests(String name) {
    super(name);
  }

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(cacheModel.getClass().getName());
    buffer.append("@" + System.identityHashCode(cacheModel) + "[");
    buffer.append("nodeFqn=" + Strings.quote(cacheModel.getNode()) + "]");

    String expected = buffer.toString();
    String actual = cacheModel.toString();

    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);

    assertEquals(expected, actual);
  }

  protected final void setUp() throws Exception {
    super.setUp();

    cacheModel = new JbossCacheCachingModel();
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String nodeFqn = "a/b/c/d/e";

    cacheModel.setNode(nodeFqn);

    JbossCacheCachingModel anotherModel = new JbossCacheCachingModel(nodeFqn);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(cacheModel,
        anotherModel);

    nodeFqn = null;
    cacheModel.setNode(nodeFqn);
    anotherModel.setNode(nodeFqn);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(cacheModel,
        anotherModel);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String nodeFqn = "a/b";

    cacheModel.setNode(nodeFqn);

    JbossCacheCachingModel anotherModel = new JbossCacheCachingModel(nodeFqn);

    assertEquals(cacheModel, anotherModel);

    anotherModel.setNode("a/b/c/d");
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
    String nodeFqn = "a";

    cacheModel.setNode(nodeFqn);

    JbossCacheCachingModel anotherModel = new JbossCacheCachingModel(nodeFqn);

    EqualsHashCodeAssert.assertEqualsIsSymmetric(cacheModel, anotherModel);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String nodeFqn = "a/b/c";

    cacheModel.setNode(nodeFqn);

    JbossCacheCachingModel secondModel = new JbossCacheCachingModel(nodeFqn);
    JbossCacheCachingModel thirdModel = new JbossCacheCachingModel(nodeFqn);

    EqualsHashCodeAssert.assertEqualsIsTransitive(cacheModel, secondModel,
        thirdModel);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    EqualsHashCodeAssert.assertEqualsNullComparisonReturnsFalse(cacheModel);
  }

  public void testToStringWithNodeFqnEqualToNull() {
    cacheModel.setNode(null);

    assertToStringIsCorrect();
  }

  public void testToStringWithNodeFqnNotEqualToNull() {
    cacheModel.setNode("x/y/z");

    assertToStringIsCorrect();
  }
}