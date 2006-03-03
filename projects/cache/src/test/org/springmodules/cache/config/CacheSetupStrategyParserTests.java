/* 
 * Created on Mar 2, 2006
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.w3c.dom.Element;

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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
public class CacheSetupStrategyParserTests extends TestCase {

  private class CacheSetupStrategyParser extends
      AbstractCacheSetupStrategyParser {

    boolean parseCacheSetupStrategyCalled;

    CacheSetupStrategyPropertySource propertySource;

    protected CacheModelParser getCacheModelParser() {
      return getModelParser();
    }

    protected CacheProviderFacadeDefinitionValidator getCacheProviderFacadeDefinitionValidator() {
      return getValidator();
    }

    protected void parseCacheSetupStrategy(Element element,
        BeanDefinitionRegistry newRegistry,
        CacheSetupStrategyPropertySource newPropertySource) {
      parseCacheSetupStrategyCalled = true;
      propertySource = newPropertySource;
    }
  }

  private class CachingListenerConfigStruct {
    String refId = "";

    Element toXml() {
      Element element = new DomElementStub("cachingListener");
      element.setAttribute("refId", refId);
      return element;
    }
  }

  private class CachingModelConfigStruct {
    String target = "";

    Element toXml() {
      Element element = new DomElementStub("caching");
      element.setAttribute("target", target);
      return element;
    }
  }

  private class FlushingModelConfigStruct {
    String target = "";

    Element toXml() {
      Element element = new DomElementStub("flushing");
      element.setAttribute("target", target);
      return element;
    }
  }

  private class StrategyConfigStruct {
    CachingListenerConfigStruct[] cachingListenerConfigStructs;

    CachingModelConfigStruct[] cachingModelConfigStructs;

    FlushingModelConfigStruct[] flushingModelConfigStructs;

    String providerId = "";

    Element toXml() {
      Element root = new DomElementStub("abstract");
      root.setAttribute("providerId", providerId);

      if (!ObjectUtils.isEmpty(cachingListenerConfigStructs)) {
        Element listeners = new DomElementStub("cachingListeners");

        int size = cachingListenerConfigStructs.length;
        for (int i = 0; i < size; i++) {
          Element listener = cachingListenerConfigStructs[i].toXml();
          listeners.appendChild(listener);
        }

        root.appendChild(listeners);
      }

      if (!ObjectUtils.isEmpty(cachingModelConfigStructs)) {
        int size = cachingModelConfigStructs.length;
        for (int i = 0; i < size; i++) {
          root.appendChild(cachingModelConfigStructs[i].toXml());
        }
      }

      if (!ObjectUtils.isEmpty(flushingModelConfigStructs)) {
        int size = flushingModelConfigStructs.length;
        for (int i = 0; i < size; i++) {
          root.appendChild(flushingModelConfigStructs[i].toXml());
        }
      }

      return root;
    }
  }

  private CacheModelParser modelParser;

  private MockControl modelParserControl;

  private CacheSetupStrategyParser parser;

  private BeanDefinitionRegistry registry;

  private StrategyConfigStruct strategyConfig;

  private CacheProviderFacadeDefinitionValidator validator;

  private MockControl validatorControl;

  /**
   * Constructor.
   * 
   * @param name
   */
  public CacheSetupStrategyParserTests(String name) {
    super(name);
  }

  public void testParse() {
    CachingListenerConfigStruct[] cachingListenerConfigStructs = cachingListenerConfigStructs();
    int cachingListenerConfigStructCount = cachingListenerConfigStructs.length;
    for (int i = 0; i < cachingListenerConfigStructCount; i++) {
      registry.registerBeanDefinition(cachingListenerConfigStructs[i].refId,
          new RootBeanDefinition(CachingListener.class));
    }
    strategyConfig.cachingListenerConfigStructs = cachingListenerConfigStructs;
    strategyConfig.cachingModelConfigStructs = cachingModelConfigStructs();
    strategyConfig.flushingModelConfigStructs = flushingModelConfigStructs();

    expectCacheProviderFacadeReferenceValidation();
    Map cachingModels = expectCachingModelParsing();
    Map flushingModels = expectFlushingModelParsing();

    replay();

    // method to test
    parser.parse(strategyConfig.toXml(), registry);

    assertTrue(parser.parseCacheSetupStrategyCalled);

    assertPropertySourceIsCorrect(cachingModels, flushingModels,
        parser.propertySource);

    verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheSetupStrategyParser#parse(Element, BeanDefinitionRegistry)}</code>
   * throws an <code>{@link IllegalStateException}</code> if any of the bean
   * definitions referenced by the XML element "cachingListener" does not
   * describe an instance of <code>{@link CachingListener}</code>.
   */
  public void testParseWithInvalidCachingListener() {
    expectCacheProviderFacadeReferenceValidation();
    replay();

    CachingListenerConfigStruct config = new CachingListenerConfigStruct();
    config.refId = "listener";

    registry.registerBeanDefinition(config.refId, new RootBeanDefinition(
        String.class));

    CachingListenerConfigStruct[] cachingListenerConfigStructs = { config };
    strategyConfig.cachingListenerConfigStructs = cachingListenerConfigStructs;

    try {
      parser.parse(strategyConfig.toXml(), registry);
      fail();

    } catch (IllegalStateException exception) {
      // expecting this exception
    }

    assertFalse(parser.parseCacheSetupStrategyCalled);
    verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheSetupStrategyParser#parse(Element, BeanDefinitionRegistry)}</code>
   * does not create a list of caching models if the XML element to parse does
   * not include any "cachingListener" subelement.
   */
  public void testParseWithoutCachingListeners() {
    expectCacheProviderFacadeReferenceValidation();

    strategyConfig.cachingModelConfigStructs = cachingModelConfigStructs();
    strategyConfig.flushingModelConfigStructs = flushingModelConfigStructs();

    Map cachingModels = expectCachingModelParsing();
    Map flushingModels = expectFlushingModelParsing();

    replay();

    // method to test
    parser.parse(strategyConfig.toXml(), registry);

    assertTrue(parser.parseCacheSetupStrategyCalled);

    assertPropertySourceIsCorrect(cachingModels, flushingModels,
        parser.propertySource);

    verify();
  }

  public void testParseWithoutCachingModels() {
    expectCacheProviderFacadeReferenceValidation();

    strategyConfig.cachingModelConfigStructs = cachingModelConfigStructs();

    Map cachingModels = expectCachingModelParsing();

    replay();

    // method to test
    parser.parse(strategyConfig.toXml(), registry);

    assertTrue(parser.parseCacheSetupStrategyCalled);

    assertPropertySourceIsCorrect(cachingModels, null, parser.propertySource);

    verify();
  }

  public void testParseWithoutFlushingModels() {
    expectCacheProviderFacadeReferenceValidation();

    strategyConfig.flushingModelConfigStructs = flushingModelConfigStructs();

    Map flushingModels = expectFlushingModelParsing();

    replay();

    // method to test
    parser.parse(strategyConfig.toXml(), registry);

    assertTrue(parser.parseCacheSetupStrategyCalled);

    assertPropertySourceIsCorrect(null, flushingModels, parser.propertySource);

    verify();
  }

  protected final CacheModelParser getModelParser() {
    return modelParser;
  }

  protected final CacheProviderFacadeDefinitionValidator getValidator() {
    return validator;
  }

  protected void setUp() throws Exception {
    parser = new CacheSetupStrategyParser();

    modelParserControl = MockControl.createControl(CacheModelParser.class);
    modelParser = (CacheModelParser) modelParserControl.getMock();

    validatorControl = MockControl
        .createControl(CacheProviderFacadeDefinitionValidator.class);
    validator = (CacheProviderFacadeDefinitionValidator) validatorControl
        .getMock();

    registry = new DefaultListableBeanFactory();

    strategyConfig = new StrategyConfigStruct();
    strategyConfig.providerId = "cacheProvider";
  }

  private void assertPropertySourceIsCorrect(Map expectedCachingModels,
      Map expectedFlushingModels,
      CacheSetupStrategyPropertySource actualPropertySource) {

    // verify cache provider facade reference
    RuntimeBeanReference cacheProviderFacadeReference = (RuntimeBeanReference) actualPropertySource
        .getCacheProviderFacade().getValue();
    assertEquals(strategyConfig.providerId, cacheProviderFacadeReference
        .getBeanName());

    // verify caching listeners
    List actualCachingListeners = (List) actualPropertySource
        .getCachingListeners().getValue();

    if (strategyConfig.cachingListenerConfigStructs != null) {
      int expectedListenerCount = strategyConfig.cachingListenerConfigStructs.length;
      assertEquals(expectedListenerCount, actualCachingListeners.size());

      for (int i = 0; i < expectedListenerCount; i++) {
        RuntimeBeanReference cachingListenerReference = (RuntimeBeanReference) actualCachingListeners
            .get(i);
        assertEquals(strategyConfig.cachingListenerConfigStructs[i].refId,
            cachingListenerReference.getBeanName());
      }
    } else {
      assertTrue(CollectionUtils.isEmpty(actualCachingListeners));
    }

    // verify caching models
    assertEquals(expectedCachingModels, actualPropertySource.getCachingModels()
        .getValue());

    // verify flushing models
    assertEquals(expectedFlushingModels, actualPropertySource
        .getFlushingModels().getValue());
  }

  private CachingListenerConfigStruct[] cachingListenerConfigStructs() {
    int size = 5;
    CachingListenerConfigStruct[] structs = new CachingListenerConfigStruct[size];

    String prefix = "listener_";
    for (int i = 0; i < size; i++) {
      CachingListenerConfigStruct config = new CachingListenerConfigStruct();
      config.refId = prefix + i;
      structs[i] = config;
    }

    return structs;
  }

  private CachingModelConfigStruct[] cachingModelConfigStructs() {
    int size = 6;
    CachingModelConfigStruct[] structs = new CachingModelConfigStruct[size];

    String prefix = "target_";
    for (int i = 0; i < size; i++) {
      CachingModelConfigStruct config = new CachingModelConfigStruct();
      config.target = prefix + i;
      structs[i] = config;
    }

    return structs;
  }

  private void expectCacheProviderFacadeReferenceValidation() {
    RootBeanDefinition cacheProviderFacade1 = new RootBeanDefinition(
        CacheProviderFacade.class);
    registry.registerBeanDefinition(strategyConfig.providerId,
        cacheProviderFacade1);
    RootBeanDefinition cacheProviderFacade = cacheProviderFacade1;
    validator.validate(cacheProviderFacade);
  }

  private Map expectCachingModelParsing() {
    Map cachingModels = new HashMap();
    int cachingModelCount = strategyConfig.cachingModelConfigStructs.length;

    for (int i = 0; i < cachingModelCount; i++) {
      CachingModelConfigStruct config = strategyConfig.cachingModelConfigStructs[i];
      Element element = config.toXml();
      modelParser.parseCachingModel(element);

      MockCachingModel model = new MockCachingModel();
      modelParserControl.setReturnValue(model);

      cachingModels.put(config.target, model);
    }
    return cachingModels;
  }

  private Map expectFlushingModelParsing() {
    Map flushingModels = new HashMap();
    int flushingModelCount = strategyConfig.flushingModelConfigStructs.length;

    for (int i = 0; i < flushingModelCount; i++) {
      FlushingModelConfigStruct config = strategyConfig.flushingModelConfigStructs[i];
      Element element = config.toXml();
      modelParser.parseFlushingModel(element);

      MockFlushingModel model = new MockFlushingModel();
      modelParserControl.setReturnValue(model);

      flushingModels.put(config.target, model);
    }
    return flushingModels;
  }

  private FlushingModelConfigStruct[] flushingModelConfigStructs() {
    int size = 3;
    FlushingModelConfigStruct[] structs = new FlushingModelConfigStruct[size];

    String prefix = "target_";
    for (int i = 0; i < size; i++) {
      FlushingModelConfigStruct config = new FlushingModelConfigStruct();
      config.target = prefix + i;
      structs[i] = config;
    }

    return structs;
  }

  private void replay() {
    modelParserControl.replay();
    validatorControl.replay();
  }

  private void verify() {
    modelParserControl.verify();
    validatorControl.verify();
  }
}
