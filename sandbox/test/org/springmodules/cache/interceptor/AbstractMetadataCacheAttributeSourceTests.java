/* 
 * Created on Oct 25, 2004
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

package org.springmodules.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Test for <code>{@link AbstractMetadataCacheAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:19:26 $
 */
public class AbstractMetadataCacheAttributeSourceTests extends TestCase {

  /**
   * Primary object (instance of the class to test).
   */
  private AbstractMetadataCacheAttributeSource attributeSource;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public AbstractMetadataCacheAttributeSourceTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    // create a new instance of the primary object and implement its abstract
    // methods.
    this.attributeSource = new AbstractMetadataCacheAttributeSource() {

      protected Collection findAllAttributes(Method argMethod) {
        return null;
      }
    };
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractMetadataCacheAttributeSource#getAttributeEntryKey(Method, Class)}</code>
   * creates a key by concatenating the name of the given class and the hash
   * code of the given method.
   */
  public void testGetAttributeEntryKey() throws Exception {

    Class targetClass = String.class;
    Method method = targetClass.getMethod("charAt", new Class[] { int.class });

    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(targetClass);
    stringBuffer.append(System.identityHashCode(method));
    String expectedKey = stringBuffer.toString();

    Object actualKey = this.attributeSource.getAttributeEntryKey(method,
        targetClass);

    assertEquals("<Key>", expectedKey, actualKey);
  }

}