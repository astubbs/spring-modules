/* 
 * Created on Jan 19, 2005
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

package org.springmodules.cache.interceptor.flush;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Test for <code>{@link CacheFlushAttributeEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:19:20 $
 */
public final class CacheFlushAttributeEditorTests extends TestCase {

  /**
   * Instance of the class to test.
   */
  private CacheFlushAttributeEditor editor;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public CacheFlushAttributeEditorTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.editor = new CacheFlushAttributeEditor();
  }

  /**
   * Verifies that the method
   * <code>{@link CacheFlushAttributeEditor#setAsText(String)}</code> creates
   * a new instance of <code>{@link FlushCache}</code> using the properties
   * specified in the given String.
   */
  public void testSetAsTextWithFlushBeforeExecutionEqualToTrue() {

    FlushCache expectedCacheFlushAttribute = new FlushCache("main,test", true);
    String properties = "[cacheProfileIds=main,test][flushBeforeExecution=true]";

    // execute the method to test.
    this.editor.setAsText(properties);

    Object actualCacheFlushAttribute = this.editor.getValue();
    assertEquals("<Cache-flush-attribute>", expectedCacheFlushAttribute,
        actualCacheFlushAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheFlushAttributeEditor#setAsText(String)}</code> creates
   * a new instance of <code>{@link FlushCache}</code> using the properties
   * specified in the given String.
   */
  public void testSetAsTextWithFlushBeforeExecutionEqualToYes() {

    FlushCache expectedCacheFlushAttribute = new FlushCache("main,test", true);
    String properties = "[cacheProfileIds=main,test][flushBeforeExecution=yes]";

    // execute the method to test.
    this.editor.setAsText(properties);

    Object actualCacheFlushAttribute = this.editor.getValue();
    assertEquals("<Cache-flush-attribute>", expectedCacheFlushAttribute,
        actualCacheFlushAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheFlushAttributeEditor#setAsText(String)}</code> creates
   * a new instance of <code>{@link FlushCache}</code> using the properties
   * specified in the given String.
   */
  public void testSetAsTextWithoutFlushBeforeExecution() {

    FlushCache expectedCacheFlushAttribute = new FlushCache("main,test");
    String properties = "[cacheProfileIds=main,test]";

    // execute the method to test.
    this.editor.setAsText(properties);

    Object actualCacheFlushAttribute = this.editor.getValue();
    assertEquals("<Cache-flush-attribute>", expectedCacheFlushAttribute,
        actualCacheFlushAttribute);
  }
}