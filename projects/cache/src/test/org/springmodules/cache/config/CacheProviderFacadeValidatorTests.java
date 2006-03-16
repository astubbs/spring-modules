/* 
 * Created on Mar 16, 2006
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
package org.springmodules.cache.config;

import java.lang.reflect.Method;

import org.easymock.classextension.MockClassControl;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheProviderFacadeValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheProviderFacadeValidatorTests extends TestCase {

  private AbstractCacheProviderFacadeValidator validator;

  private MockClassControl validatorControl;

  /**
   * @param name
   *          the name of the test case
   */
  public CacheProviderFacadeValidatorTests(String name) {
    super(name);
  }

  public void testValidateWithInvalidClass() {
    RootBeanDefinition definition = new RootBeanDefinition(String.class);

    validator.getCacheProviderFacadeClass();
    validatorControl.setReturnValue(Integer.class);

    validatorControl.replay();

    // method to test.
    try {
      validator.validate(definition);
      fail();
    } catch (IllegalStateException exception) {
      // expecting exception.
    }
  }

  public void testValidateWithValidClass() {
    Class clazz = String.class;
    RootBeanDefinition definition = new RootBeanDefinition(clazz);

    validator.getCacheProviderFacadeClass();
    validatorControl.setReturnValue(clazz);

    validator.doValidate(definition);
    validatorControl.replay();

    // method to test.
    validator.validate(definition);
  }

  protected void setUp() throws Exception {
    Class targetClass = AbstractCacheProviderFacadeValidator.class;

    Method doValidateMethod = targetClass.getDeclaredMethod("doValidate",
        new Class[] { AbstractBeanDefinition.class });

    Method getCacheProviderFacadeClassMethod = targetClass.getDeclaredMethod(
        "getCacheProviderFacadeClass", new Class[0]);

    Method[] methodsToMock = { doValidateMethod,
        getCacheProviderFacadeClassMethod };

    validatorControl = MockClassControl.createControl(targetClass, null, null,
        methodsToMock);
    validator = (AbstractCacheProviderFacadeValidator) validatorControl
        .getMock();
  }

  protected void tearDown() {
    validatorControl.verify();
  }

}
