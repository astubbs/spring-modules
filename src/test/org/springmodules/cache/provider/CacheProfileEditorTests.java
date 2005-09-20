/* 
 * Created on Jan 10, 2005
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

import java.beans.PropertyEditorSupport;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link CacheProfileEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class CacheProfileEditorTests extends TestCase {

  private class MockPropertyEditor extends PropertyEditorSupport {
    String value;

    public Object getValue() {
      return value;
    }

    public void setAsText(String text) {
      value = "_" + text;
    }
  }

  private static final String CACHE_NAME_PROPERTY = "cacheName";

  private static final String GROUP_PROPERTY = "group";

  private CacheProfileEditor cacheProfileEditor;

  public CacheProfileEditorTests(String name) {
    super(name);
  }

  private String getPropertiesAsText(String cacheName, String group) {
    return getPropertyAsText(CACHE_NAME_PROPERTY, cacheName)
        + getPropertyAsText(GROUP_PROPERTY, group);
  }

  private String getPropertyAsText(String propertyName, String propertyValue) {
    return "[" + propertyName + "=" + propertyValue + "]";
  }

  protected void setUp() throws Exception {
    super.setUp();

    cacheProfileEditor = new CacheProfileEditor();
    cacheProfileEditor.setCacheProfileClass(MockCacheProfile.class);
  }

  public void testSetAsText() {
    String cacheName = "main";
    String group = "pojo";
    MockCacheProfile expected = new MockCacheProfile(cacheName, group);

    String properties = getPropertiesAsText(cacheName, group);
    cacheProfileEditor.setAsText(properties);

    assertEquals(expected, cacheProfileEditor.getValue());
  }

  public void testSetAsTextWithCacheProfileClassEqualToNull() {
    cacheProfileEditor.setCacheProfileClass(null);

    try {
      cacheProfileEditor.setAsText("");
      fail();
    } catch (IllegalStateException exception) {
      // we are expecting this exception.
    }
  }

  public void testSetAsTextWithEmptyText() {
    cacheProfileEditor.setAsText("");
    assertEquals(new MockCacheProfile(), cacheProfileEditor.getValue());
  }

  public void testSetAsTextWithPropertyEditors() {
    MockPropertyEditor cacheNameEditor = new MockPropertyEditor();
    MockPropertyEditor groupEditor = new MockPropertyEditor();

    Map editors = new HashMap();
    editors.put(CACHE_NAME_PROPERTY, cacheNameEditor);
    editors.put(GROUP_PROPERTY, groupEditor);

    cacheProfileEditor.setCacheProfilePropertyEditors(editors);

    String cacheName = "rebels";
    String group = "jedi";

    String properties = getPropertiesAsText(cacheName, group);
    cacheProfileEditor.setAsText(properties);

    MockCacheProfile expected = new MockCacheProfile(cacheNameEditor.value,
        groupEditor.value);

    assertFalse(expected.getCacheName().equals(cacheName));
    assertFalse(expected.getGroup().equals(group));
    assertEquals(expected, cacheProfileEditor.getValue());
  }

  public void testSetAsTextWithTextEqualToNull() {
    cacheProfileEditor.setAsText(null);
    assertEquals(new MockCacheProfile(), cacheProfileEditor.getValue());
  }
}