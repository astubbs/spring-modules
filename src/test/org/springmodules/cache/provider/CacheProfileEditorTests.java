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

import java.lang.reflect.Method;
import java.util.Properties;

import junit.framework.TestCase;

import org.easymock.classextension.MockClassControl;
import org.springmodules.cache.mock.MockCacheProfile;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheProfileEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class CacheProfileEditorTests extends TestCase {

  private AbstractCacheProfileEditor cacheProfileEditor;

  private MockClassControl cacheProfileEditorControl;

  public CacheProfileEditorTests(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();

    Class classToMock = AbstractCacheProfileEditor.class;

    // set up the methods to mock.
    Method createCacheProfileMethod = classToMock.getDeclaredMethod(
        "createCacheProfile", new Class[] { Properties.class });

    Method[] methodsToMock = new Method[] { createCacheProfileMethod };

    // create the mock control.
    cacheProfileEditorControl = MockClassControl.createControl(classToMock,
        null, null, methodsToMock);

    // create the mock object.
    cacheProfileEditor = (AbstractCacheProfileEditor) cacheProfileEditorControl
        .getMock();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProfileEditor#setAsText(String)}</code>.
   * creates a new instance of <code>{@link CacheProfile}</code> by parsing
   * the specified set of properties.
   */
  public void testSetAsText() {
    // String containing the properties of the cache profile to create.
    String unparsedProperties = "[name=value]";

    // properties to be constructed by parsing the String above.
    Properties parsedProperties = new Properties();
    parsedProperties.setProperty("name", "value");

    CacheProfile cacheProfile = new MockCacheProfile();

    // create a cache profile from the parsed properties.
    cacheProfileEditor.createCacheProfile(parsedProperties);
    cacheProfileEditorControl.setReturnValue(cacheProfile);

    cacheProfileEditorControl.replay();

    // execute the method to test.
    cacheProfileEditor.setAsText(unparsedProperties);

    Object actualCacheProfile = cacheProfileEditor.getValue();
    assertSame("<cache profile>", cacheProfile, actualCacheProfile);

    cacheProfileEditorControl.verify();
  }

}