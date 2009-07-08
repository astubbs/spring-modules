/* 
 * Created on Mar 8, 2006
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
import java.util.List;
import java.util.Map;

import org.easymock.AbstractMatcher;
import org.easymock.classextension.MockClassControl;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import org.springmodules.cache.interceptor.caching.CachingAttributeSourceAdvisor;
import org.springmodules.cache.interceptor.caching.MetadataCachingInterceptor;
import org.springmodules.cache.interceptor.flush.FlushingAttributeSourceAdvisor;
import org.springmodules.cache.interceptor.flush.MetadataFlushingInterceptor;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractMetadataAttributesParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class MetadataAttributesParserTests extends
    AbstractCacheSetupStrategyParserImplTestCase {

  protected abstract class AbstractInterceptorPropertyValuesMatcher extends
      AbstractMatcher {
    /**
     * @see AbstractMatcher#argumentMatches(Object, Object)
     */
    protected boolean argumentMatches(Object expected, Object actual) {
      if (!(expected instanceof MutablePropertyValues)) {
        return expected.equals(actual);
      }
      if (!(actual instanceof MutablePropertyValues)) {
        return false;
      }
      return equals((MutablePropertyValues) expected,
          (MutablePropertyValues) actual);
    }

    protected boolean equals(List expected, List actual) {
      if (expected == actual) {
        return true;
      }
      if (!CollectionUtils.isEmpty(expected)) {
        int count = expected.size();

        if (actual.size() != count) {
          return false;
        }

        for (int i = 0; i < count; i++) {
          if (!equals((RuntimeBeanReference) expected.get(i),
              (RuntimeBeanReference) actual.get(i))) {
            return false;
          }
        }

      } else if (!CollectionUtils.isEmpty(actual)) {
        return false;
      }
      return true;
    }

    protected abstract boolean equals(MutablePropertyValues expected,
        MutablePropertyValues actual);

    protected boolean equals(RuntimeBeanReference expected,
        RuntimeBeanReference actual) {
      if (expected == actual) {
        return true;
      }
      return ObjectUtils.nullSafeEquals(expected.getBeanName(), actual
          .getBeanName());
    }

    protected RuntimeBeanReference getCacheProviderFacadeReference(
        MutablePropertyValues propertyValues) {
      return (RuntimeBeanReference) propertyValues.getPropertyValue(
          "cacheProviderFacade").getValue();
    }
  }

  protected class CachingInterceptorPropertyValuesMatcher extends
      AbstractInterceptorPropertyValuesMatcher {

    protected boolean equals(MutablePropertyValues expected,
        MutablePropertyValues actual) {
      if (expected == actual) {
        return true;
      }
      if (!equals(getCacheProviderFacadeReference(expected),
          getCacheProviderFacadeReference(actual))) {
        return false;
      }
      if (!equals(getCachingListeners(expected), getCachingListeners(actual))) {
        return false;
      }
      if (!ObjectUtils.nullSafeEquals(getCachingModels(expected),
          getCachingModels(actual))) {
        return false;
      }
      return true;
    }

    private List getCachingListeners(MutablePropertyValues propertyValues) {
      return (List) propertyValues.getPropertyValue("cachingListeners")
          .getValue();
    }

    private Map getCachingModels(MutablePropertyValues propertyValues) {
      return (Map) propertyValues.getPropertyValue("cachingModels").getValue();
    }
  }

  protected class FlushingInterceptorPropertyValuesMatcher extends
      AbstractInterceptorPropertyValuesMatcher {

    protected boolean equals(MutablePropertyValues expected,
        MutablePropertyValues actual) {
      if (expected == actual) {
        return true;
      }
      if (!equals(getCacheProviderFacadeReference(expected),
          getCacheProviderFacadeReference(actual))) {
        return false;
      }
      if (!ObjectUtils.nullSafeEquals(getFlushingModels(expected),
          getFlushingModels(actual))) {
        return false;
      }
      return true;
    }

    private Map getFlushingModels(MutablePropertyValues propertyValues) {
      return (Map) propertyValues.getPropertyValue("flushingModels").getValue();
    }
  }

  private AbstractMetadataAttributesParser parser;

  private MockClassControl parserControl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public MetadataAttributesParserTests(String name) {
    super(name);
  }

  public void testParseCacheSetupStrategy() {
    expectations();
    parserControl.replay();

    parser.parseCacheSetupStrategy(new DomElementStub(""), parserContext,
        propertySource);

    assertAutoProxyIsRegistered();
    assertCachingInterceptorIsRegistered();
    assertCachingAdvisorIsRegistered();
    assertFlushingInterceptorIsRegistered();
    assertFlushingAdvisorIsRegistered();

    parserControl.verify();
  }

  protected void afterSetUp() throws Exception {
    Class targetClass = AbstractMetadataAttributesParser.class;

    Method registerCustomBeansMethod = targetClass.getDeclaredMethod(
        "registerCustomBeans", new Class[] { BeanDefinitionRegistry.class });

    Method configureCachingInterceptorMethod = targetClass.getDeclaredMethod(
        "configureCachingInterceptor", new Class[] {
            MutablePropertyValues.class, BeanDefinitionRegistry.class });

    Method configureFlushingInterceptorMethod = targetClass.getDeclaredMethod(
        "configureFlushingInterceptor", new Class[] {
            MutablePropertyValues.class, BeanDefinitionRegistry.class });

    Method[] methodsToMock = new Method[] { configureCachingInterceptorMethod,
        configureFlushingInterceptorMethod, registerCustomBeansMethod };

    parserControl = MockClassControl.createControl(targetClass, null, null,
        methodsToMock);
    parser = (AbstractMetadataAttributesParser) parserControl.getMock();
  }

  private void assertAutoProxyIsRegistered() {
    AbstractBeanDefinition definition = (AbstractBeanDefinition) registry
        .getBeanDefinition("autoproxy");

    ConfigAssert.assertBeanDefinitionWrapsClass(definition,
        DefaultAdvisorAutoProxyCreator.class);
  }

  private void assertCachingAdvisorIsRegistered() {
    Class advisorClass = CachingAttributeSourceAdvisor.class;
    AbstractBeanDefinition definition = (AbstractBeanDefinition) registry
        .getBeanDefinition(advisorClass.getName());

    ConfigAssert.assertBeanDefinitionWrapsClass(definition, advisorClass);

    RuntimeBeanReference expectedReference = new RuntimeBeanReference(
        MetadataCachingInterceptor.class.getName());

    ConfigAssert.assertBeanDefinitionHasConstructorArgument(expectedReference,
        definition.getConstructorArgumentValues(), 0,
        RuntimeBeanReference.class);
  }

  private void assertCachingInterceptorIsRegistered() {
    BeanDefinition definition = registry
        .getBeanDefinition(MetadataCachingInterceptor.class.getName());

    ConfigAssert.assertBeanDefinitionHasProperty(definition, propertySource
        .getCacheKeyGeneratorProperty());
    ConfigAssert.assertBeanDefinitionHasProperty(definition, propertySource
        .getCacheProviderFacadeProperty());
    ConfigAssert.assertBeanDefinitionHasProperty(definition, propertySource
        .getCachingListenersProperty());
    ConfigAssert.assertBeanDefinitionHasProperty(definition, propertySource
        .getCachingModelsProperty());
  }

  private void assertFlushingAdvisorIsRegistered() {
    Class advisorClass = FlushingAttributeSourceAdvisor.class;
    AbstractBeanDefinition definition = (AbstractBeanDefinition) registry
        .getBeanDefinition(advisorClass.getName());

    ConfigAssert.assertBeanDefinitionWrapsClass(definition, advisorClass);

    RuntimeBeanReference expectedReference = new RuntimeBeanReference(
        MetadataFlushingInterceptor.class.getName());

    ConfigAssert.assertBeanDefinitionHasConstructorArgument(expectedReference,
        definition.getConstructorArgumentValues(), 0,
        RuntimeBeanReference.class);
  }

  private void assertFlushingInterceptorIsRegistered() {
    BeanDefinition definition = registry
        .getBeanDefinition(MetadataFlushingInterceptor.class.getName());

    ConfigAssert.assertBeanDefinitionHasProperty(definition, propertySource
        .getCacheProviderFacadeProperty());
    ConfigAssert.assertBeanDefinitionHasProperty(definition, propertySource
        .getFlushingModelsProperty());
  }

  private MutablePropertyValues cachingInterceptorPropertyValues() {
    MutablePropertyValues pv = new MutablePropertyValues();

    pv.addPropertyValue(propertySource.getCacheProviderFacadeProperty());
    pv.addPropertyValue(propertySource.getCachingListenersProperty());
    pv.addPropertyValue(propertySource.getCachingModelsProperty());

    return pv;
  }

  private void expectations() {
    parser.configureCachingInterceptor(cachingInterceptorPropertyValues(),
        registry);
    parserControl.setMatcher(new CachingInterceptorPropertyValuesMatcher());

    parser.configureFlushingInterceptor(flushingInterceptorPropertyValues(),
        registry);
    parserControl.setMatcher(new FlushingInterceptorPropertyValuesMatcher());

    parser.registerCustomBeans(registry);
  }

  private MutablePropertyValues flushingInterceptorPropertyValues() {
    MutablePropertyValues pv = new MutablePropertyValues();

    pv.addPropertyValue(propertySource.getCacheProviderFacadeProperty());
    pv.addPropertyValue(propertySource.getFlushingModelsProperty());

    return pv;
  }

}
