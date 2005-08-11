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
 * Unit Tests for <code>{@link CacheFlushAttributeEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/08/11 04:32:03 $
 */
public final class CacheFlushAttributeEditorTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private CacheFlushAttributeEditor editor;

  public CacheFlushAttributeEditorTests(String name) {
    super(name);
  }

  private void assertEqualsCacheFlushAttribute(FlushCache expected,
      FlushCache actual) {
    assertEquals("<FlushCache>", expected, actual);
  }

  protected void setUp() throws Exception {
    super.setUp();
    this.editor = new CacheFlushAttributeEditor();
  }

  public void testSetAsTextWithFlushBeforeExecutionEqualToTrue() {
    FlushCache expectedCacheFlushAttribute = new FlushCache("main,test", true);
    String properties = "[cacheProfileIds=main,test][flushBeforeExecution=true]";

    // execute the method to test.
    this.editor.setAsText(properties);

    FlushCache actualCacheFlushAttribute = (FlushCache) this.editor.getValue();
    this.assertEqualsCacheFlushAttribute(expectedCacheFlushAttribute,
        actualCacheFlushAttribute);
  }

  public void testSetAsTextWithFlushBeforeExecutionEqualToYes() {
    FlushCache expectedCacheFlushAttribute = new FlushCache("main,test", true);
    String properties = "[cacheProfileIds=main,test][flushBeforeExecution=yes]";

    // execute the method to test.
    this.editor.setAsText(properties);

    FlushCache actualCacheFlushAttribute = (FlushCache) this.editor.getValue();
    this.assertEqualsCacheFlushAttribute(expectedCacheFlushAttribute,
        actualCacheFlushAttribute);
  }

  public void testSetAsTextWithoutFlushBeforeExecution() {
    FlushCache expectedCacheFlushAttribute = new FlushCache("main,test");
    String properties = "[cacheProfileIds=main,test]";

    // execute the method to test.
    this.editor.setAsText(properties);

    FlushCache actualCacheFlushAttribute = (FlushCache) this.editor.getValue();
    this.assertEqualsCacheFlushAttribute(expectedCacheFlushAttribute,
        actualCacheFlushAttribute);
  }
}