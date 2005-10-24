/* 
 * Created on Oct 14, 2005
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
package org.springmodules.cache.provider.jboss;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.AbstractEqualsHashCodeTestCase;
import org.springmodules.util.ArrayUtils;

/**
 * <p>
 * Unit Tests for <code>{@link JbossCacheFlushingModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class JbossCacheFlushingModelTests extends
    AbstractEqualsHashCodeTestCase {

  private JbossCacheFlushingModel model;

  private static Log logger = LogFactory
      .getLog(JbossCacheFlushingModelTests.class);

  public JbossCacheFlushingModelTests(String newName) {
    super(newName);
  }

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(model.getClass().getName());
    buffer.append("@" + System.identityHashCode(model) + "[");
    buffer.append("nodes=" + ArrayUtils.toString(model.getNodes()) + ", ");
    buffer.append("flushBeforeMethodExecution="
        + model.isFlushBeforeMethodExecution() + "]");

    String expected = buffer.toString();
    String actual = model.toString();

    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);

    assertEquals(expected, actual);
  }

  protected void setUp() {
    model = new JbossCacheFlushingModel();
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String[] nodes = { "a/b/c", "x/y/z" };

    model.setNodes(nodes);
    JbossCacheFlushingModel model2 = new JbossCacheFlushingModel(nodes);
    assertEqualsHashCodeRelationshipIsCorrect(model, model2);

    nodes = null;
    model.setNodes(nodes);
    model2.setNodes(nodes);
    assertEqualsHashCodeRelationshipIsCorrect(model, model2);

    boolean flushBeforeMethodExecution = true;
    model.setFlushBeforeMethodExecution(flushBeforeMethodExecution);
    model2.setFlushBeforeMethodExecution(flushBeforeMethodExecution);
    assertEqualsHashCodeRelationshipIsCorrect(model, model2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String[] nodes = { "o/p/q" };

    model.setNodes(nodes);
    JbossCacheFlushingModel model2 = new JbossCacheFlushingModel(nodes);
    assertEquals(model, model2);

    model2.setNodes(new String[0]);
    assertFalse(model.equals(model2));
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    assertEqualsIsReflexive(model);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    JbossCacheFlushingModel model2 = new JbossCacheFlushingModel();
    assertEqualsIsSymmetric(model, model2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String[] nodes = { "a/b" };

    model.setNodes(nodes);
    JbossCacheFlushingModel model2 = new JbossCacheFlushingModel(nodes);
    JbossCacheFlushingModel model3 = new JbossCacheFlushingModel(nodes);

    assertEqualsIsTransitive(model, model2, model3);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertEqualsNullComparisonReturnsFalse(model);
  }

  public void testToStringWithNodesEqualToNull() {
    model.setNodes((String[]) null);
    assertToStringIsCorrect();
  }

  public void testToStringWithEmptyNodes() {
    model.setNodes(new String[0]);
    assertToStringIsCorrect();
  }

  public void testToStringWithNotEmptyNodes() {
    model.setNodes(new String[] { "a/b/c" });
    assertToStringIsCorrect();
  }

}
