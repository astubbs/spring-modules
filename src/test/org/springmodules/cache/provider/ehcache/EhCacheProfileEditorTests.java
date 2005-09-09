/* 
 * Created on Jan 14, 2005
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

package org.springmodules.cache.provider.ehcache;

import java.util.Properties;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.springmodules.cache.provider.CacheProfileValidator;

/**
 * <p>
 * Unit Tests for <code>{@link EhCacheProfileEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.6 $ $Date: 2005/09/09 02:19:11 $
 */
public final class EhCacheProfileEditorTests extends TestCase {

  private EhCacheProfileEditor cacheProfileEditor;

  private CacheProfileValidator cacheProfileValidator;

  private MockControl cacheProfileValidatorControl;

  private Properties properties;

  public EhCacheProfileEditorTests(String name) {
    super(name);
  }

  /**
   * Asserts that <code>{@link EhCacheProfileEditor}</code> validates the
   * created cache profile.
   * 
   * @param expected
   *          the expected cache profile to be created.
   */
  private void assertCreateCacheProfileValidatesCreatedCacheProfile(
      EhCacheProfile expected) {
    setUpCacheProfileValidatorAsMockObject();

    cacheProfileValidator.validateCacheProfile(expected);

    setStatusOfMockControlsToReplay();

    assertEquals(expected, cacheProfileEditor.createCacheProfile(properties));

    verifyExpectationsOfMockControlsWereMet();
  }

  private void setStatusOfMockControlsToReplay() {
    cacheProfileValidatorControl.replay();
  }

  protected void setUp() throws Exception {
    super.setUp();
    cacheProfileEditor = new EhCacheProfileEditor();
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
    EhCacheProfile expected = new EhCacheProfile();
    assertCreateCacheProfileValidatesCreatedCacheProfile(expected);
  }

  public void testCreateCacheProfileWithPropertiesHavingCacheName() {
    String cacheName = "pojos";
    properties.setProperty("cacheName", cacheName);

    EhCacheProfile expected = new EhCacheProfile(cacheName);
    assertCreateCacheProfileValidatesCreatedCacheProfile(expected);
  }

  public void testDefaultConstructorCreatesEhCacheProfileValidator() {
    CacheProfileValidator validator = cacheProfileEditor
        .getCacheProfileValidator();

    assertEquals(EhCacheProfileValidator.class, validator.getClass());
  }

  private void verifyExpectationsOfMockControlsWereMet() {
    cacheProfileValidatorControl.verify();
  }
}