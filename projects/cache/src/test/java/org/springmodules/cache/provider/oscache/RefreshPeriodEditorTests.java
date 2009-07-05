/* 
 * Created on Sep 21, 2005
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
package org.springmodules.cache.provider.oscache;

import junit.framework.TestCase;

import com.opensymphony.oscache.base.CacheEntry;

/**
 * <p>
 * Unit Tests for <code>{@link RefreshPeriodEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class RefreshPeriodEditorTests extends TestCase {

  private RefreshPeriodEditor editor;

  public RefreshPeriodEditorTests(String name) {
    super(name);
  }

  public void testSetAsTextWithEmptyText() {
    editor.setAsText("");
    assertNull(editor.getValue());
  }

  public void testSetAsTextWithTextEqualToIndefiniteExpire() {
    editor.setAsText("INDEFINITE_EXPIRY");
    assertEquals(CacheEntry.INDEFINITE_EXPIRY, ((Integer) editor.getValue())
        .intValue());
  }

  public void testSetAsTextWithTextEqualToNull() {
    editor.setAsText(null);
    assertNull(editor.getValue());
  }

  public void testSetAsTextWithTextEqualToParsableInteger() {
    Integer expected = new Integer(8);
    editor.setAsText(expected.toString());
    assertEquals(expected, editor.getValue());
  }

  public void testSetAsTextWithTextNotBeingParsableInteger() {
    try {
      editor.setAsText("abc");
      fail();

    } catch (IllegalArgumentException exception) {
      exception.printStackTrace();
    }
  }

  protected void setUp() {
    editor = new RefreshPeriodEditor();
  }
}
