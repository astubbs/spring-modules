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
 * Unit Tests for <code>{@link AbstractMetadataCacheAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/09/06 01:41:38 $
 */
public class MetadataCacheAttributeSourceTests extends TestCase {

  private AbstractMetadataCacheAttributeSource attributeSource;

  public MetadataCacheAttributeSourceTests(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();

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

    StringBuffer buffer = new StringBuffer();
    buffer.append(targetClass);
    buffer.append(System.identityHashCode(method));
    String expectedKey = buffer.toString();

    Object actualKey = this.attributeSource.getAttributeEntryKey(method,
        targetClass);

    assertEquals("<Key>", expectedKey, actualKey);
  }

}