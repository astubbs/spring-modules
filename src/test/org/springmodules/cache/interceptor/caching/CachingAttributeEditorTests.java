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

package org.springmodules.cache.interceptor.caching;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Test for <code>{@link CachingAttributeEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:41:13 $
 */
public final class CachingAttributeEditorTests extends TestCase {

  /**
   * Primary object (instance of the class to test).
   */
  private CachingAttributeEditor editor;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public CachingAttributeEditorTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.editor = new CachingAttributeEditor();
  }

  /**
   * Verifies that the method
   * <code>{@link CachingAttributeEditor#setAsText(String)}</code> creates a
   * new instance of <code>{@link Cached}</code> using the properties
   * specified in the given String.
   */
  public void testSetAsTextWithNotEmptyString() {
    String cacheProfileId = "[cacheProfileId=main]";

    Cached expectedCached = new Cached();
    expectedCached.setCacheProfileId("main");

    this.editor.setAsText(cacheProfileId);
    Object actualCached = this.editor.getValue();

    assertEquals("<Cached>", expectedCached, actualCached);
  }
}