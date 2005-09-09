/* 
 * Created on Jan 11, 2005
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

package org.springmodules.cache.provider.jcs;

import java.util.Properties;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.springmodules.cache.provider.CacheProfile;
import org.springmodules.cache.provider.CacheProfileValidator;

/**
 * <p>
 * Unit Tests for <code>{@link JcsProfileEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.6 $ $Date: 2005/09/09 02:18:55 $
 */
public final class JcsProfileEditorTests extends TestCase {

  private JcsProfileEditor cacheProfileEditor;

  private CacheProfileValidator cacheProfileValidator;

  private MockControl cacheProfileValidatorControl;

  private Properties properties;

  public JcsProfileEditorTests(String name) {
    super(name);
  }

  /**
   * Asserts that <code>{@link JcsProfileEditor}</code> validates the created
   * cache profile.
   * 
   * @param expected
   *          the expected cache profile to be created.
   */
  private void assertCreateCacheProfileValidatesCreatedCacheProfile(
      JcsProfile expected) {
    setUpCacheProfileValidatorAsMockObject();

    // validate the new cache profile.
    cacheProfileValidator.validateCacheProfile(expected);

    setStatusOfMockControlsToReplay();

    // execute the method to test.
    CacheProfile actual = cacheProfileEditor.createCacheProfile(properties);
    assertEquals("<Cache profile>", expected, actual);

    verifyExpectationsOfMockControlsWereMet();
  }

  private void setStatusOfMockControlsToReplay() {
    cacheProfileValidatorControl.replay();
  }

  protected void setUp() throws Exception {
    super.setUp();
    cacheProfileEditor = new JcsProfileEditor();
    properties = new Properties();
  }

  private void setUpCacheProfileValidatorAsMockObject() {
    cacheProfileValidatorControl = MockControl
        .createControl(CacheProfileValidator.class);
    cacheProfileValidator = (CacheProfileValidator) cacheProfileValidatorControl
        .getMock();

    cacheProfileEditor.setCacheProfileValidator(cacheProfileValidator);
  }

  public void testCreateCacheProfileWithEmptyProperties() {
    JcsProfile expected = new JcsProfile();
    assertCreateCacheProfileValidatesCreatedCacheProfile(expected);
  }

  public void testCreateCacheProfileWithPropertiesHavingCacheName() {
    String cacheName = "pojos";
    String group = "services";
    properties.setProperty("cacheName", cacheName);
    properties.setProperty("group", group);

    JcsProfile expected = new JcsProfile(cacheName, group);
    assertCreateCacheProfileValidatesCreatedCacheProfile(expected);
  }

  public void testDefaultConstructorCreatesJcsProfileValidator() {
    CacheProfileValidator validator = cacheProfileEditor
        .getCacheProfileValidator();

    assertEquals(JcsProfileValidator.class, validator.getClass());
  }

  private void verifyExpectationsOfMockControlsWereMet() {
    cacheProfileValidatorControl.verify();
  }

}