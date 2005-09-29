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
 * Unit Tests for <code>{@link JbossCacheModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class JbossCacheModelTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(JbossCacheModelTests.class);

  private JbossCacheModel cacheModel;

  public JbossCacheModelTests(String name) {
    super(name);
  }

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(cacheModel.getClass().getName());
    buffer.append("@" + System.identityHashCode(cacheModel) + "[");
    buffer.append("nodeFqn=" + Strings.quote(cacheModel.getNodeFqn()) + "]");

    String expected = buffer.toString();
    String actual = cacheModel.toString();

    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);

    assertEquals(expected, actual);
  }

  protected final void setUp() throws Exception {
    super.setUp();

    cacheModel = new JbossCacheModel();
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String nodeFqn = "a/b/c/d/e";

    cacheModel.setNodeFqn(nodeFqn);

    JbossCacheModel anotherModel = new JbossCacheModel(nodeFqn);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(cacheModel,
        anotherModel);

    nodeFqn = null;
    cacheModel.setNodeFqn(nodeFqn);
    anotherModel.setNodeFqn(nodeFqn);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(cacheModel,
        anotherModel);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String nodeFqn = "a/b";

    cacheModel.setNodeFqn(nodeFqn);

    JbossCacheModel anotherModel = new JbossCacheModel(nodeFqn);

    assertEquals(cacheModel, anotherModel);

    anotherModel.setNodeFqn("a/b/c/d");
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

    cacheModel.setNodeFqn(nodeFqn);

    JbossCacheModel anotherModel = new JbossCacheModel(nodeFqn);

    EqualsHashCodeAssert.assertEqualsIsSymmetric(cacheModel, anotherModel);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String nodeFqn = "a/b/c";

    cacheModel.setNodeFqn(nodeFqn);

    JbossCacheModel secondModel = new JbossCacheModel(nodeFqn);
    JbossCacheModel thirdModel = new JbossCacheModel(nodeFqn);

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
    cacheModel.setNodeFqn(null);

    assertToStringIsCorrect();
  }

  public void testToStringWithNodeFqnNotEqualToNull() {
    cacheModel.setNodeFqn("x/y/z");

    assertToStringIsCorrect();
  }
}