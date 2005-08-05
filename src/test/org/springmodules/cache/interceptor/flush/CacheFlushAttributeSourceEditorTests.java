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
package org.springmodules.cache.interceptor.flush;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.springmodules.cache.interceptor.SimulatedService;

/**
 * <p>
 * Unit Tests for <code>{@link CacheFlushAttributeSourceEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/08/05 02:18:46 $
 */
public class CacheFlushAttributeSourceEditorTests extends TestCase {

  public static final String LINE_SEPARATOR = System
      .getProperty("line.separator");

  /**
   * Primary object that is under test.
   */
  private CacheFlushAttributeSourceEditor editor;

  /**
   * Reference to class <code>{@link SimulatedService}</code>.
   */
  private Class targetClass;

  public CacheFlushAttributeSourceEditorTests(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.editor = new CacheFlushAttributeSourceEditor();
    this.targetClass = SimulatedService.class;
  }

  /**
   * Verifies that the method
   * <code>{@link CacheFlushAttributeSourceEditor#setAsText(String)}</code>
   * creates a new instance of
   * <code>{@link MethodMapCacheFlushAttributeSource}</code> by parsing the
   * given text.
   */
  public void testSetAsText() throws Exception {
    String myCacheAttrName = "myCache";
    String myOtherCacheAttrName = "myOtherCache";

    Map expectedAdvisedMethods = new HashMap();
    expectedAdvisedMethods.put(this.targetClass.getName() + ".get*",
        "[cacheProfileIds=" + myCacheAttrName + "]");
    expectedAdvisedMethods.put(this.targetClass.getName() + ".getPersonName",
        "[cacheProfileIds=" + myOtherCacheAttrName + "]");

    // build the text to be used to create a MethodMapCacheFlushAttributeSource.
    StringBuffer buffer = new StringBuffer();
    Set entrySet = expectedAdvisedMethods.entrySet();
    Iterator entrySetIterator = entrySet.iterator();
    while (entrySetIterator.hasNext()) {
      Map.Entry entry = (Map.Entry) entrySetIterator.next();
      buffer.append(entry.getKey());
      buffer.append("=");
      buffer.append(entry.getValue());
      buffer.append(LINE_SEPARATOR);
    }
    String text = buffer.toString();

    // execute the method to test.
    this.editor.setAsText(text);

    Object value = this.editor.getValue();
    MethodMapCacheFlushAttributeSource cachingAttributeSource = (MethodMapCacheFlushAttributeSource) value;

    Map actualAttributeMap = cachingAttributeSource.getAttributeMap();

    // verify that the methods 'getPersonName' and 'getPersons' are advised.
    Class clazz = SimulatedService.class;
    Method getPersonNameMethod = clazz.getDeclaredMethod("getPersonName",
        new Class[] { long.class });
    Method getPersonsMethod = clazz.getDeclaredMethod("getPersons", null);

    FlushCache myCacheAttr = (FlushCache) actualAttributeMap
        .get(getPersonsMethod);
    assertEquals("<Cache profile id>", myCacheAttrName, myCacheAttr
        .getCacheProfileIds()[0]);

    FlushCache myOtherCacheAttr = (FlushCache) actualAttributeMap
        .get(getPersonNameMethod);
    assertEquals("<Cache profile id>", myOtherCacheAttrName, myOtherCacheAttr
        .getCacheProfileIds()[0]);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheFlushAttributeSourceEditor#setAsText(String)}</code>
   * does not create any object if the given text is empty.
   */
  public void testSetAsTextWithEmptyText() {
    this.editor.setAsText(null);

    assertNull("The PropertyEditor should not create any object", this.editor
        .getValue());
  }
}
