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
 * Unit Test for <code>{@link AbstractCacheProfileEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:19:11 $
 */
public final class AbstractCacheProfileEditorTests extends TestCase {

  /**
   * Primary object (instance of the class to test).
   */
  private AbstractCacheProfileEditor cacheProfileEditor;

  /**
   * Controls the behavior of and mocks the abstract methods of
   * <code>{@link #cacheProfileEditor}</code>.
   */
  private MockClassControl cacheProfileEditorControl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public AbstractCacheProfileEditorTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.setUpCacheProfileEditor();
  }

  /**
   * Sets up:
   * <ul>
   * <li><code>{@link #cacheProfileEditor}</code></li>
   * <li><code>{@link #cacheProfileEditorControl}</code></li>
   * </ul>
   */
  private void setUpCacheProfileEditor() throws Exception {
    // set up the class to mock.
    Class classToMock = AbstractCacheProfileEditor.class;

    // set up the methods to mock.
    Method createCacheProfileMethod = classToMock.getDeclaredMethod(
        "createCacheProfile", new Class[] { Properties.class });

    Method[] methodsToMock = new Method[] { createCacheProfileMethod };

    // create the mock control.
    this.cacheProfileEditorControl = MockClassControl.createControl(
        classToMock, null, null, methodsToMock);

    // create the mock object.
    this.cacheProfileEditor = (AbstractCacheProfileEditor) this.cacheProfileEditorControl
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

    // expectation: create a cache profile from the parsed properties.
    this.cacheProfileEditor.createCacheProfile(parsedProperties);
    this.cacheProfileEditorControl.setReturnValue(cacheProfile);

    // set the state of the mock control to 'replay'.
    this.cacheProfileEditorControl.replay();

    // execute the method to test.
    this.cacheProfileEditor.setAsText(unparsedProperties);

    Object actualCacheProfile = this.cacheProfileEditor.getValue();
    assertSame("<cache profile>", cacheProfile, actualCacheProfile);

    // verify that the expectations of the mock control were met.
    this.cacheProfileEditorControl.verify();
  }

}