/* 
 * Created on Sep 1, 2005
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

import org.springframework.util.ObjectUtils;
import org.springmodules.AbstractEqualsHashCodeTestCase;

/**
 * <p>
 * Unit Tests for <code>{@link JbossCacheCachingModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class JbossCacheCachingModelTests extends
    AbstractEqualsHashCodeTestCase {

  private JbossCacheCachingModel model;

  public JbossCacheCachingModelTests(String name) {
    super(name);
  }

  protected final void setUp() {
    model = new JbossCacheCachingModel();
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String nodeFqn = "a/b/c/d/e";

    model.setNode(nodeFqn);
    JbossCacheCachingModel model2 = new JbossCacheCachingModel(nodeFqn);
    assertEqualsHashCodeRelationshipIsCorrect(model, model2);

    nodeFqn = null;
    model.setNode(nodeFqn);
    model2.setNode(nodeFqn);
    assertEqualsHashCodeRelationshipIsCorrect(model, model2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String nodeFqn = "a/b";

    model.setNode(nodeFqn);
    JbossCacheCachingModel model2 = new JbossCacheCachingModel(nodeFqn);
    assertEquals(model, model2);

    model2.setNode("a/b/c/d");
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
    String nodeFqn = "a";
    model.setNode(nodeFqn);
    JbossCacheCachingModel anotherModel = new JbossCacheCachingModel(nodeFqn);
    assertEqualsIsSymmetric(model, anotherModel);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String nodeFqn = "a/b/c";
    model.setNode(nodeFqn);
    JbossCacheCachingModel model2 = new JbossCacheCachingModel(nodeFqn);
    JbossCacheCachingModel model3 = new JbossCacheCachingModel(nodeFqn);

    assertEqualsIsTransitive(model, model2, model3);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertEqualsNullComparisonReturnsFalse(model);
  }

  public void testToStringWithNodeFqnEqualToNull() {
    model.setNode(null);
    String actual = model.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(model) + "[nodeFqn=null]";
    assertEquals(model.toString(), actual);
  }

  public void testToStringWithNodeFqnNotEqualToNull() {
    model.setNode("x/y/z");
    String actual = model.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(model) + "[nodeFqn='x/y/z']";
    assertEquals(model.toString(), actual);
  }
}