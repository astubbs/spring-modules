/* 
 * Created on Oct 25, 2005
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
package org.springmodules.cache.provider;

import org.springmodules.AbstractEqualsHashCodeTestCase;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractFlushingModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class FlushingModelTests extends AbstractEqualsHashCodeTestCase {

  private class FlushingModel extends AbstractFlushingModel {

    private static final long serialVersionUID = -7830152178722634535L;

    public FlushingModel() {
      super();
    }

    public FlushingModel(boolean flushBeforeMethodExecution) {
      this();
      setFlushBeforeMethodExecution(flushBeforeMethodExecution);
    }
  }

  private FlushingModel model;

  public FlushingModelTests(String name) {
    super(name);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    boolean flushBeforeMethodExecution = true;
    model.setFlushBeforeMethodExecution(flushBeforeMethodExecution);

    FlushingModel model2 = new FlushingModel(flushBeforeMethodExecution);
    assertEqualsHashCodeRelationshipIsCorrect(model, model2);

    flushBeforeMethodExecution = false;
    model.setFlushBeforeMethodExecution(flushBeforeMethodExecution);
    model2.setFlushBeforeMethodExecution(flushBeforeMethodExecution);
    assertEqualsHashCodeRelationshipIsCorrect(model, model2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    boolean flushBeforeMethodExecution = true;
    model.setFlushBeforeMethodExecution(flushBeforeMethodExecution);

    FlushingModel model2 = new FlushingModel(flushBeforeMethodExecution);
    assertEquals(model, model2);

    model2.setFlushBeforeMethodExecution(false);
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
    boolean flushBeforeMethodExecution = true;
    model.setFlushBeforeMethodExecution(flushBeforeMethodExecution);

    FlushingModel model2 = new FlushingModel(flushBeforeMethodExecution);
    assertEqualsIsSymmetric(model, model2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    boolean flushBeforeMethodExecution = true;
    model.setFlushBeforeMethodExecution(flushBeforeMethodExecution);

    FlushingModel model2 = new FlushingModel(flushBeforeMethodExecution);
    FlushingModel model3 = new FlushingModel(flushBeforeMethodExecution);

    assertEqualsIsTransitive(model, model2, model3);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertEqualsNullComparisonReturnsFalse(model);
  }

  protected void setUp() {
    model = new FlushingModel();
  }

}
