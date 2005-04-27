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

package org.springmodules.cache.interceptor;

import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.springmodules.cache.CacheAttribute;
import org.springmodules.cache.mock.MockCacheAttribute;

/**
 * <p>
 * Unit Test for <code>{@link AbstractNameMatchCacheAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:41:20 $
 */
public final class NameMatchCacheAttributeSourceTests extends TestCase {

  /**
   * Primary object (instance of the class to test).
   */
  private AbstractNameMatchCacheAttributeSource cacheAttributeSource;

  /**
   * Controls the behavior and implements the abstract methods of
   * <code>{@link #cacheAttributeSource}</code>.
   */
  private MockClassControl cacheAttributeSourceControl;

  /**
   * Mock editor of instances of <code>{@link CacheAttribute}</code> to be
   * returned by the method
   * <code>{@link AbstractNameMatchCacheAttributeSource#getCacheAttributeEditor()}</code>.
   */
  private PropertyEditor mockCacheAttributeEditor;

  /**
   * Controls the behavior of <code>{@link #mockCacheAttributeEditor}</code>.
   */
  private MockControl mockCacheAttributeEditorControl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public NameMatchCacheAttributeSourceTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.setUpCacheAttributeSource();
  }

  /**
   * Sets up:
   * <ul>
   * <li>{@link #cacheAttributeSource}</li>
   * <li>{@link #cacheAttributeSourceControl}</li>
   * </ul>
   */
  private void setUpCacheAttributeSource() throws Exception {

    // set up the class to mock.
    Class classToMock = AbstractNameMatchCacheAttributeSource.class;

    // set up the abstract methods to mock.
    Method getCacheAttributeEditorMethod = classToMock.getDeclaredMethod(
        "getCacheAttributeEditor", null);

    Method[] methodsToMock = new Method[] { getCacheAttributeEditorMethod };

    // create the mock control.
    this.cacheAttributeSourceControl = MockClassControl.createControl(
        classToMock, null, null, methodsToMock);

    // create the mock object.
    this.cacheAttributeSource = (AbstractNameMatchCacheAttributeSource) this.cacheAttributeSourceControl
        .getMock();
  }

  /**
   * Sets up:
   * <ul>
   * <li>{@link #mockCacheAttributeEditor}</li>
   * <li>{@link #mockCacheAttributeEditorControl}</li>
   * </ul>
   */
  private void setUpMockCacheAttributeEditor() {
    // create the mock control.
    this.mockCacheAttributeEditorControl = MockControl
        .createControl(PropertyEditor.class);

    // create the mock object.
    this.mockCacheAttributeEditor = (PropertyEditor) this.mockCacheAttributeEditorControl
        .getMock();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractNameMatchCacheAttributeSource#isMatch(String, String)}</code>
   * returns <code>true</code> when the given method name matches the given
   * mapped name. For this test, the mapped name ends with a '*'.
   */
  public void testIsMatchWithMatchingMethodNameAndMappedNameEndingWithWildcards() {
    String methodName = "getNewCustomer";
    String mappedName = "getNew*";

    // execute the method to test.
    boolean matches = this.cacheAttributeSource.isMatch(methodName, mappedName);

    assertTrue("'" + methodName + "' should match '" + mappedName + "'",
        matches);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractNameMatchCacheAttributeSource#isMatch(String, String)}</code>
   * returns <code>true</code> when the given method name matches the given
   * mapped name. For this test, the mapped name starts with a '*'.
   */
  public void testIsMatchWithMatchingMethodNameAndMappedNameStartingWithWildcards() {
    String methodName = "getNewCustomer";
    String mappedName = "*Customer";

    // execute the method to test.
    boolean matches = this.cacheAttributeSource.isMatch(methodName, mappedName);

    assertTrue("'" + methodName + "' should match '" + mappedName + "'",
        matches);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractNameMatchCacheAttributeSource#isMatch(String, String)}</code>
   * returns <code>false</code> when the given method name does not match the
   * given mapped name.
   */
  public void testIsMatchWithNotMatchingMethodName() {
    String methodName = "getNewCustomer";
    String mappedName = "getCurrentCustomer";

    // execute the method to test.
    boolean matches = this.cacheAttributeSource.isMatch(methodName, mappedName);

    assertFalse("'" + methodName + "' should not match '" + mappedName + "'",
        matches);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractNameMatchCacheAttributeSource#setProperties(Properties)}</code>
   * does not add any entry to the map of <code>{@link CacheAttribute}</code>
   * if the <code>{@link CacheAttribute}</code> returned by the editor (
   * <code>{@link #mockCacheAttributeEditor}</code>) is <code>null</code>.
   */
  public void testSetPropertiesWhenPropertyEditorReturnsCacheAttributeEqualToNull() {
    this.setUpMockCacheAttributeEditor();

    // set the properties to create cache profiles from.
    String mappedName = "getNew*";
    String cacheAttributeProperties = "[cacheProfileId=test]";
    Properties cacheAttributes = new Properties();
    cacheAttributes.setProperty(mappedName, cacheAttributeProperties);

    // expectation: get the the editor of cache attributes.
    this.cacheAttributeSource.getCacheAttributeEditor();
    this.cacheAttributeSourceControl
        .setReturnValue(this.mockCacheAttributeEditor);

    // expectation: set the properties of the cache attributes to the editor.
    this.mockCacheAttributeEditor.setAsText(cacheAttributeProperties);

    // expectation: the cache attribute created by the editor is null.
    this.mockCacheAttributeEditor.getValue();
    this.mockCacheAttributeEditorControl.setReturnValue(null);

    // set the state of the mock controls to "replay".
    this.cacheAttributeSourceControl.replay();
    this.mockCacheAttributeEditorControl.replay();

    // execute the method to test.
    this.cacheAttributeSource.setProperties(cacheAttributes);

    // verify that the expectations of the mock controls were met.
    this.cacheAttributeSourceControl.verify();
    this.mockCacheAttributeEditorControl.verify();

    // verify that the map of cache attributes is empty.
    Map actualCacheAttributes = this.cacheAttributeSource.getAttributeMap();
    assertTrue("The map of attributes should be empty", actualCacheAttributes
        .isEmpty());
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractNameMatchCacheAttributeSource#setProperties(Properties)}</code>
   * adds the <code>{@link CacheAttribute}</code> returned by the editor (
   * <code>{@link #mockCacheAttributeEditor}</code>) to the map of cache
   * attributes when such <code>{@link CacheAttribute}</code> is not
   * <code>null</code>.
   */
  public void testSetPropertiesWhenPropertyEditorReturnsObjectNotEqualToNull() {
    this.setUpMockCacheAttributeEditor();

    // set the properties to create cache profiles from.
    String mappedName = "getNew*";
    String cacheAttributeProperties = "[cacheProfileId=test]";
    Properties cacheAttributes = new Properties();
    cacheAttributes.setProperty(mappedName, cacheAttributeProperties);

    // expectation: get the the editor of cache attributes.
    this.cacheAttributeSource.getCacheAttributeEditor();
    this.cacheAttributeSourceControl
        .setReturnValue(this.mockCacheAttributeEditor);

    // expectation: set the properties of the cache attributes to the editor.
    this.mockCacheAttributeEditor.setAsText(cacheAttributeProperties);

    // expectation: the cache attribute created by the editor is not null.
    this.mockCacheAttributeEditor.getValue();
    CacheAttribute cacheAttribute = new MockCacheAttribute();
    this.mockCacheAttributeEditorControl.setReturnValue(cacheAttribute);

    // set the state of the mock controls to "replay".
    this.cacheAttributeSourceControl.replay();
    this.mockCacheAttributeEditorControl.replay();

    // execute the method to test.
    this.cacheAttributeSource.setProperties(cacheAttributes);

    // verify that the expectations of the mock controls were met.
    this.cacheAttributeSourceControl.verify();
    this.mockCacheAttributeEditorControl.verify();

    // verify the cache attribute was added to the map.
    Map actualCacheAttributes = this.cacheAttributeSource.getAttributeMap();
    Object actualCacheAttribute = actualCacheAttributes.get(mappedName);

    assertSame("<Cache attribute>", cacheAttribute, actualCacheAttribute);
  }
}