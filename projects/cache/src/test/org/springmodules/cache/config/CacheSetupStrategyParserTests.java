/* 
 * Created on Mar 7, 2006
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.AbstractMatcher;
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.w3c.dom.Element;

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import org.springmodules.cache.config.CacheSetupStrategyElementBuilder.CachingListenerElementBuilder;
import org.springmodules.cache.config.CacheSetupStrategyElementBuilder.CachingModelElementBuilder;
import org.springmodules.cache.config.CacheSetupStrategyElementBuilder.FlushingModelElementBuilder;
import org.springmodules.cache.interceptor.caching.CachingListener;
import org.springmodules.cache.mock.MockCachingModel;
import org.springmodules.cache.mock.MockFlushingModel;
import org.springmodules.cache.provider.CacheProviderFacade;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheSetupStrategyParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheSetupStrategyParserTests extends
    AbstractSchemaBasedConfigurationTestCase {

  private class CacheSetupStrategyPropertySourceMatcher extends AbstractMatcher {
    /**
     * @see AbstractMatcher#argumentMatches(Object, Object)
     */
    protected boolean argumentMatches(Object expected, Object actual) {
      if (!(expected instanceof CacheSetupStrategyPropertySource)) {
        return expected.equals(actual);
      }
      if (!(actual instanceof CacheSetupStrategyPropertySource)) {
        return false;
      }
      if (!equals((CacheSetupStrategyPropertySource) expected,
          (CacheSetupStrategyPropertySource) actual)) {
        return false;
      }
      return true;
    }

    private boolean equals(CacheSetupStrategyPropertySource expected,
        CacheSetupStrategyPropertySource actual) {
      if (!equals(expected.cacheProviderFacadeReference, actual.cacheProviderFacadeReference)) {
        return false;
      }
      if (!equals(expected.cachingListeners, actual.cachingListeners)) {
        return false;
      }
      if (!ObjectUtils.nullSafeEquals(expected.cachingModelMap,
          actual.cachingModelMap)) {
        return false;
      }
      if (!ObjectUtils.nullSafeEquals(expected.flushingModelMap,
          actual.flushingModelMap)) {
        return false;
      }
      return true;
    }

    private boolean equals(List expected, List actual) {
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

    private boolean equals(RuntimeBeanReference expected,
        RuntimeBeanReference actual) {
      if (!ObjectUtils.nullSafeEquals(expected.getBeanName(), actual
          .getBeanName())) {
        return false;
      }
      return true;
    }
  }

  private CacheSetupStrategyElementBuilder elementBuilder;

  private CacheModelParser modelParser;

  private MockControl modelParserControl;

  private AbstractCacheSetupStrategyParser strategyParser;

  private MockClassControl strategyParserControl;

  private CacheProviderFacadeDefinitionValidator validator;

  private MockControl validatorControl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public CacheSetupStrategyParserTests(String name) {
    super(name);
  }

  public void testParse() {
    int listenerCount = 5;
    int modelCount = 2;

    List cachingListeners = new ArrayList();
    elementBuilder.setDefaultCachingListenerElementBuilders(listenerCount);
    for (int i = 0; i < listenerCount; i++) {
      String refId = elementBuilder.cachingListenerElementBuilders[i].refId;
      registry.registerBeanDefinition(refId, new RootBeanDefinition(
          CachingListener.class));

      cachingListeners.add(new RuntimeBeanReference(refId));
    }

    elementBuilder.setDefaultCachingModelElementBuilders(modelCount);
    elementBuilder.setDefaultFlushingModelElementBuilders(modelCount);

    expectCacheProviderFacadeReferenceValidation();
    Map cachingModelMap = expectCachingModelParsing();
    Map flushingModelMap = expectFlushingModelParsing();

    Element element = elementBuilder.toXml();
    CacheSetupStrategyPropertySource propertySource = new CacheSetupStrategyPropertySource(
        new RuntimeBeanReference(elementBuilder.cacheProviderId),
        cachingListeners, cachingModelMap, flushingModelMap);

    strategyParser.parseCacheSetupStrategy(element, parserContext,
        propertySource);
    strategyParserControl
        .setMatcher(new CacheSetupStrategyPropertySourceMatcher());

    replay();

    // method to test
    strategyParser.parse(element, parserContext);

    verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheSetupStrategyParser#parse(Element, org.springframework.beans.factory.xml.ParserContext)}</code>
   * throws an <code>{@link IllegalStateException}</code> if any of the bean
   * definitions referenced by the XML element "cachingListener" does not
   * describe an instance of <code>{@link CachingListener}</code>.
   */
  public void testParseWithInvalidCachingListener() {
    expectCacheProviderFacadeReferenceValidation();
    replay();

    // register invalid listener.
    CachingListenerElementBuilder builder = new CachingListenerElementBuilder();
    builder.refId = "listener";
    registry.registerBeanDefinition(builder.refId, new RootBeanDefinition(
        String.class));

    CachingListenerElementBuilder[] builders = { builder };
    elementBuilder.cachingListenerElementBuilders = builders;

    try {
      strategyParser.parse(elementBuilder.toXml(), parserContext);
      fail();
    } catch (IllegalStateException exception) {
      // expecting this exception
    }

    verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheSetupStrategyParser#parse(Element, org.springframework.beans.factory.xml.ParserContext)}</code>
   * does not create a list of caching models if the XML element to parse does
   * not include any "cachingListener" subelement.
   */
  public void testParseWithoutCachingListeners() {
    int modelCount = 2;

    expectCacheProviderFacadeReferenceValidation();

    elementBuilder.setDefaultCachingModelElementBuilders(modelCount);
    elementBuilder.setDefaultFlushingModelElementBuilders(modelCount);

    Map cachingModelMap = expectCachingModelParsing();
    Map flushingModelMap = expectFlushingModelParsing();

    Element element = elementBuilder.toXml();
    CacheSetupStrategyPropertySource propertySource = new CacheSetupStrategyPropertySource(
        new RuntimeBeanReference(elementBuilder.cacheProviderId), null,
        cachingModelMap, flushingModelMap);

    strategyParser.parseCacheSetupStrategy(element, parserContext,
        propertySource);
    strategyParserControl
        .setMatcher(new CacheSetupStrategyPropertySourceMatcher());

    replay();

    // method to test
    strategyParser.parse(element, parserContext);

    verify();
  }

  public void testParseWithoutCachingModels() {
    int modelCount = 2;

    expectCacheProviderFacadeReferenceValidation();

    elementBuilder.setDefaultFlushingModelElementBuilders(modelCount);

    Map flushingModelMap = expectFlushingModelParsing();

    Element element = elementBuilder.toXml();
    CacheSetupStrategyPropertySource propertySource = new CacheSetupStrategyPropertySource(
        new RuntimeBeanReference(elementBuilder.cacheProviderId), null, null,
        flushingModelMap);

    strategyParser.parseCacheSetupStrategy(element, parserContext,
        propertySource);
    strategyParserControl
        .setMatcher(new CacheSetupStrategyPropertySourceMatcher());

    replay();

    // method to test
    strategyParser.parse(element, parserContext);

    verify();
  }

  public void testParseWithoutFlushingModels() {
    int modelCount = 2;

    expectCacheProviderFacadeReferenceValidation();

    elementBuilder.setDefaultCachingModelElementBuilders(modelCount);

    Map cachingModelMap = expectCachingModelParsing();

    Element element = elementBuilder.toXml();
    CacheSetupStrategyPropertySource propertySource = new CacheSetupStrategyPropertySource(
        new RuntimeBeanReference(elementBuilder.cacheProviderId), null,
        cachingModelMap, null);

    strategyParser.parseCacheSetupStrategy(element, parserContext,
        propertySource);
    strategyParserControl
        .setMatcher(new CacheSetupStrategyPropertySourceMatcher());

    replay();

    // method to test
    strategyParser.parse(element, parserContext);

    verify();
  }

  protected void onSetUp() throws Exception {
    setUpModelParser();
    setUpStrategyParser();
    setUpValidator();

    elementBuilder = new CacheSetupStrategyElementBuilder();
    elementBuilder.cacheProviderId = "cacheProvider";
  }

  private void expectCacheProviderFacadeReferenceValidation() {
    strategyParser.getCacheProviderFacadeDefinitionValidator();
    strategyParserControl.setReturnValue(validator);

    RootBeanDefinition cacheProviderFacade = new RootBeanDefinition(
        CacheProviderFacade.class);
    registry.registerBeanDefinition(elementBuilder.cacheProviderId,
        cacheProviderFacade);
    validator.validate(cacheProviderFacade);
  }

  private Map expectCachingModelParsing() {
    Map cachingModelMap = new HashMap();
    int cachingModelCount = elementBuilder.cachingModelElementBuilders.length;

    strategyParser.getCacheModelParser();
    strategyParserControl.setReturnValue(modelParser);

    for (int i = 0; i < cachingModelCount; i++) {
      MockCachingModel model = new MockCachingModel();
      CachingModelElementBuilder builder = elementBuilder.cachingModelElementBuilders[i];

      modelParser.parseCachingModel(builder.toXml());
      modelParserControl.setReturnValue(model);

      cachingModelMap.put(builder.target, model);
    }
    return cachingModelMap;
  }

  private Map expectFlushingModelParsing() {
    Map flushingModelMap = new HashMap();
    int flushingModelCount = elementBuilder.flushingModelElementBuilders.length;

    strategyParser.getCacheModelParser();
    strategyParserControl.setReturnValue(modelParser);

    for (int i = 0; i < flushingModelCount; i++) {
      FlushingModelElementBuilder builder = elementBuilder.flushingModelElementBuilders[i];
      MockFlushingModel model = new MockFlushingModel();

      modelParser.parseFlushingModel(builder.toXml());
      modelParserControl.setReturnValue(model);

      flushingModelMap.put(builder.target, model);
    }
    return flushingModelMap;
  }

  private void replay() {
    modelParserControl.replay();
    strategyParserControl.replay();
    validatorControl.replay();
  }

  private void setUpModelParser() {
    modelParserControl = MockControl.createControl(CacheModelParser.class);
    modelParser = (CacheModelParser) modelParserControl.getMock();
  }

  private void setUpStrategyParser() throws Exception {
    Class targetClass = AbstractCacheSetupStrategyParser.class;

    Method getCacheModelParserMethod = targetClass.getDeclaredMethod(
        "getCacheModelParser", new Class[0]);

    Method getCacheProviderFacadeDefinitionValidatorMethod = targetClass
        .getDeclaredMethod("getCacheProviderFacadeDefinitionValidator",
            new Class[0]);

    Method parseCacheSetupStrategyMethod = targetClass.getDeclaredMethod(
        "parseCacheSetupStrategy", new Class[] { Element.class,
            ParserContext.class, CacheSetupStrategyPropertySource.class });

    Method[] methodsToMock = { getCacheModelParserMethod,
        getCacheProviderFacadeDefinitionValidatorMethod,
        parseCacheSetupStrategyMethod };

    strategyParserControl = MockClassControl.createControl(targetClass, null,
        null, methodsToMock);

    strategyParser = (AbstractCacheSetupStrategyParser) strategyParserControl
        .getMock();
  }

  private void setUpValidator() {
    validatorControl = MockControl
        .createControl(CacheProviderFacadeDefinitionValidator.class);
    validator = (CacheProviderFacadeDefinitionValidator) validatorControl
        .getMock();
  }

  private void verify() {
    modelParserControl.verify();
    strategyParserControl.verify();
    validatorControl.verify();
  }
}
