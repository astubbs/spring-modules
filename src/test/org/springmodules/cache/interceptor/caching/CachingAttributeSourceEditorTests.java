/* 
 * Created on Apr 6, 2005
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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import org.springmodules.cache.interceptor.SimulatedService;

/**
 * <p>
 * Unit Tests for <code>{@link CachingAttributeSourceEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/09/09 02:19:00 $
 */
public class CachingAttributeSourceEditorTests extends TestCase {

  public static final String LINE_SEPARATOR = System
      .getProperty("line.separator");

  private CachingAttributeSourceEditor editor;

  /**
   * Reference to the class <code>{@link SimulatedService}</code>.
   */
  private Class targetClass;

  public CachingAttributeSourceEditorTests(String name) {
    super(name);
  }

  private void assertCachingAttributeSourceEditorDidNotCreateAnyObject() {
    assertNull(editor.getValue());
  }

  private String createProfileIdProperty(String profileId) {
    return "[cacheProfileId=" + profileId + "]";
  }

  protected void setUp() throws Exception {
    super.setUp();

    editor = new CachingAttributeSourceEditor();
    targetClass = SimulatedService.class;
  }

  /**
   * Verifies that the method
   * <code>{@link CachingAttributeSourceEditor#setAsText(String)}</code>
   * creates a new instance of
   * <code>{@link MethodMapCachingAttributeSource}</code> by parsing the given
   * text.
   */
  public void testSetAsText() throws Exception {
    String myCacheAttrName = "myCache";
    String myOtherCacheAttrName = "myOtherCache";

    String targetClassName = targetClass.getName();
    Map expectedAdvisedMethods = new HashMap();
    expectedAdvisedMethods.put(targetClassName + ".get*",
        createProfileIdProperty(myCacheAttrName));
    expectedAdvisedMethods.put(targetClassName + ".getPersonName",
        createProfileIdProperty(myOtherCacheAttrName));

    // build the text to be used to create a MethodMapCachingAttributeSource.
    StringBuffer buffer = new StringBuffer();
    for (Iterator i = expectedAdvisedMethods.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();
      buffer.append(entry.getKey());
      buffer.append("=");
      buffer.append(entry.getValue());
      buffer.append(LINE_SEPARATOR);
    }
    String text = buffer.toString();

    // execute the method to test.
    editor.setAsText(text);

    Object value = editor.getValue();
    MethodMapCachingAttributeSource cachingAttributeSource = (MethodMapCachingAttributeSource) value;

    Map actualAttributeMap = cachingAttributeSource.getAttributeMap();

    // verify that the methods 'getPersonName' and 'getPersons' are advised.
    Class clazz = SimulatedService.class;
    Method getPersonNameMethod = clazz.getDeclaredMethod("getPersonName",
        new Class[] { long.class });
    Method getPersonsMethod = clazz.getDeclaredMethod("getPersons", null);

    Cached myCacheAttr = (Cached) actualAttributeMap.get(getPersonsMethod);
    assertEquals(myCacheAttrName, myCacheAttr.getCacheProfileId());

    Cached myOtherCacheAttr = (Cached) actualAttributeMap
        .get(getPersonNameMethod);
    assertEquals(myOtherCacheAttrName, myOtherCacheAttr.getCacheProfileId());
  }

  /**
   * Verifies that the method
   * <code>{@link CachingAttributeSourceEditor#setAsText(String)}</code> does
   * not create any <code>{@link MethodMapCachingAttributeSource}</code> if
   * the given text is <code>null</code>.
   */
  public void testSetAsTextWithEmptyText() {
    editor.setAsText("");
    assertCachingAttributeSourceEditorDidNotCreateAnyObject();
  }

  /**
   * Verifies that the method
   * <code>{@link CachingAttributeSourceEditor#setAsText(String)}</code> does
   * not create any <code>{@link MethodMapCachingAttributeSource}</code> if
   * the given text is <code>null</code>.
   */
  public void testSetAsTextWithTextEqualToNull() {
    editor.setAsText(null);
    assertCachingAttributeSourceEditorDidNotCreateAnyObject();
  }
}
