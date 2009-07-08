/* 
 * Created on Mar 31, 2006
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

import junit.framework.TestCase;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;

import org.springmodules.cache.interceptor.caching.CachingListener;

/**
 * <p>
 * Unit Tests for <code>{@link CachingListenerValidatorImpl}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CachingListenerValidatorImplTests extends TestCase {

  private static final String CACHING_LISTENER_BEAN_NAME = "cachingListener";

  private ParserContext parserContext;

  private BeanDefinitionRegistry registry;

  private CachingListenerValidatorImpl validator;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public CachingListenerValidatorImplTests(String name) {
    super(name);
  }

  public void testValidateWithBeanDefinitionHolder() {
    BeanDefinition beanDefinition = createCachingListenerBeanDefinition();
    BeanDefinitionHolder holder = createBeanDefinitionHolder(beanDefinition);

    validator.validate(holder, 0, parserContext);
  }

  public void testValidateWithBeanDefinitionNotDescribingCachingListener() {
    BeanDefinition beanDefinition = new RootBeanDefinition(String.class);
    BeanDefinitionHolder holder = createBeanDefinitionHolder(beanDefinition);

    assertValidationThrowsException(holder);
  }

  public void testValidateWithObjectNotBeingReferenceOrHolder() {
    assertValidationThrowsException("cachingListener");
  }

  public void testValidateWithRuntimeBeanReference() {
    BeanDefinition beanDefinition = createCachingListenerBeanDefinition();
    String beanName = CACHING_LISTENER_BEAN_NAME;
    registry.registerBeanDefinition(beanName, beanDefinition);
    RuntimeBeanReference reference = new RuntimeBeanReference(beanName);

    validator.validate(reference, 0, parserContext);
  }

  protected void setUp() {
    parserContext = ParserContextFactory.create();
    registry = parserContext.getRegistry();
    validator = new CachingListenerValidatorImpl();
  }

  private void assertValidationThrowsException(Object objectToValidate) {
    try {
      validator.validate(objectToValidate, 0, parserContext);
      fail();
    } catch (IllegalStateException exception) {
      // expecting this exception
    }
  }

  private BeanDefinitionHolder createBeanDefinitionHolder(
      BeanDefinition beanDefinition) {
    String beanName = CACHING_LISTENER_BEAN_NAME;
    BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition,
        beanName);
    return holder;
  }

  private BeanDefinition createCachingListenerBeanDefinition() {
    return new RootBeanDefinition(CachingListener.class);
  }

}
