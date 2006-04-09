/* 
 * Created on Apr 8, 2006
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
 * Copyright @2006 the original author or authors.
 */
package org.springmodules.cache.impl;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.easymock.classextension.MockClassControl;

import junit.framework.TestCase;

/**
 * Unit Tests for <code>{@link AbstractCache}</code>
 * 
 * @author Alex Ruiz
 * 
 */
public class CacheTests extends TestCase {

  private static final String KEY = "myKey";

  private static final String VALUE = "myValue";

  private AbstractCache cache;

  private MockClassControl cacheControl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public CacheTests(String name) {
    super(name);
  }

  public void testContainsKeyWithKeyEqualToNull() {
    cacheControl.replay();

    try {
      cache.containsKey(null);
      fail();
    } catch (IllegalArgumentException exception) {
      // expecting this exception.
    }

    cacheControl.verify();
  }

  public void testContains() {
    boolean expected = true;
    cache.doContainsKey(KEY);
    cacheControl.setReturnValue(expected);
    cacheControl.replay();

    boolean actual = cache.containsKey(KEY);
    assertEquals(expected, actual);

    cacheControl.verify();
  }

  public void testGetWithReturnedAliveElementNotEqualToNull() {
    Serializable expected = VALUE;
    Element element = new Element(KEY, expected);
    cache.doGet(KEY);
    cacheControl.setReturnValue(element);
    cacheControl.replay();

    Serializable actual = cache.get(KEY);
    assertEquals(expected, actual);

    cacheControl.verify();
  }

  public void testGetWithReturnedElementEqualToNull() {
    cache.doGet(KEY);
    cacheControl.setReturnValue(null);
    cacheControl.replay();

    Serializable actual = cache.get(KEY);
    assertNull(actual);

    cacheControl.verify();
  }

  public void testGetWithReturnedExpiredElement() {
    Element element = new Element(KEY, VALUE, 1l);

    // expire the element.
    try {
      Thread.sleep(10);
    } catch (InterruptedException exception) {
      exception.printStackTrace();
    }

    cache.doGet(KEY);
    cacheControl.setReturnValue(element);
    cacheControl.replay();

    Serializable actual = cache.get(KEY);
    assertNull(actual);

    cacheControl.verify();
  }

  public void testGetWithKeyEqualToNull() {
    cacheControl.replay();

    try {
      cache.get(null);
      fail();
    } catch (IllegalArgumentException exception) {
      // expecting this exception.
    }

    cacheControl.verify();
  }

  /*
   * Test method for
   * 'org.springmodules.cache.impl.AbstractCache.put(Serializable,
   * Serializable)'
   */
  public void testPutSerializableSerializable() {

  }

  /*
   * Test method for
   * 'org.springmodules.cache.impl.AbstractCache.put(Serializable, Serializable,
   * long)'
   */
  public void testPutSerializableSerializableLong() {

  }

  /*
   * Test method for
   * 'org.springmodules.cache.impl.AbstractCache.remove(Serializable)'
   */
  public void testRemove() {

  }

  protected void setUp() throws Exception {
    Class target = AbstractCache.class;

    Method doContainsKeyMethod = target.getDeclaredMethod("doContainsKey",
        new Class[] { Serializable.class });
    Method doGetMethod = target.getDeclaredMethod("doGet",
        new Class[] { Serializable.class });
    Method doPutMethod = target.getDeclaredMethod("doPut",
        new Class[] { Element.class });
    Method doRemoveMethod = target.getDeclaredMethod("doRemove",
        new Class[] { Serializable.class });

    Method[] methodsToMock = { doContainsKeyMethod, doGetMethod, doPutMethod,
        doRemoveMethod };

    cacheControl = MockClassControl.createControl(target, null, null,
        methodsToMock);
    cache = (AbstractCache) cacheControl.getMock();
  }

}
