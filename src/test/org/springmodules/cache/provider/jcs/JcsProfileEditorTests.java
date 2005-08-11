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
 * @version $Revision: 1.4 $ $Date: 2005/08/11 04:34:33 $
 */
public final class JcsProfileEditorTests extends TestCase {

  /**
   * Primary object that is under test.
   */
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
    this.setUpCacheProfileValidatorAsMockObject();

    // expectation: validate the new cache profile.
    this.cacheProfileValidator.validateCacheProfile(expected);

    this.setStatusOfMockControlsToReplay();

    // execute the method to test.
    CacheProfile actual = this.cacheProfileEditor
        .createCacheProfile(this.properties);
    assertEquals("<Cache profile>", expected, actual);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  private void setStatusOfMockControlsToReplay() {
    this.cacheProfileValidatorControl.replay();
  }

  protected void setUp() throws Exception {
    super.setUp();
    this.cacheProfileEditor = new JcsProfileEditor();
    this.properties = new Properties();
  }

  private void setUpCacheProfileValidatorAsMockObject() {
    this.cacheProfileValidatorControl = MockControl
        .createControl(CacheProfileValidator.class);
    this.cacheProfileValidator = (CacheProfileValidator) this.cacheProfileValidatorControl
        .getMock();

    this.cacheProfileEditor
        .setCacheProfileValidator(this.cacheProfileValidator);
  }

  public void testCreateCacheProfileWithEmptyProperties() {
    JcsProfile expected = new JcsProfile();
    this.assertCreateCacheProfileValidatesCreatedCacheProfile(expected);
  }

  public void testCreateCacheProfileWithPropertiesHavingCacheName() {
    String cacheName = "pojos";
    String group = "services";
    this.properties.setProperty("cacheName", cacheName);
    this.properties.setProperty("group", group);

    JcsProfile expected = new JcsProfile(cacheName, group);
    this.assertCreateCacheProfileValidatesCreatedCacheProfile(expected);
  }

  public void testDefaultConstructorCreatesJcsProfileValidator() {
    CacheProfileValidator ehCacheProfileValidator = this.cacheProfileEditor
        .getCacheProfileValidator();

    assertTrue("CacheProfileValidator should be an instance of <"
        + JcsProfileValidator.class.getName() + ">",
        ehCacheProfileValidator instanceof JcsProfileValidator);
  }

  private void verifyExpectationsOfMockControlsWereMet() {
    this.cacheProfileValidatorControl.verify();
  }

}