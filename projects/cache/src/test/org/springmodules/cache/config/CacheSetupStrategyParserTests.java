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

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
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

    protected CacheProviderFacadeValidator getCacheProviderFacadeValidator() {
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

  private CacheProviderFacadeValidator validator;

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
    RootBeanDefinition cacheProviderFacade = new RootBeanDefinition(
        CacheProviderFacade.class);
    registry.registerBeanDefinition(strategyConfig.providerId,
        cacheProviderFacade);

    validator.validate(cacheProviderFacade);

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

    replay();

    // method to test
    parser.parse(strategyConfig.toXml(), registry);

    assertTrue(parser.parseCacheSetupStrategyCalled);

    // verify that the reference to the cache provider facade exists
    CacheSetupStrategyPropertySource actualPropertySource = parser.propertySource;
    RuntimeBeanReference cacheProviderFacadeReference = (RuntimeBeanReference) actualPropertySource
        .getCacheProviderFacade().getValue();
    assertEquals(strategyConfig.providerId, cacheProviderFacadeReference
        .getBeanName());

    // verify that the caching listeners were created
    int expectedListenerCount = strategyConfig.cachingListenerConfigStructs.length;
    List actualCachingListeners = (List) actualPropertySource
        .getCachingListeners().getValue();
    assertEquals(expectedListenerCount, actualCachingListeners.size());
    for (int i = 0; i < expectedListenerCount; i++) {
      RuntimeBeanReference cachingListenerReference = (RuntimeBeanReference) actualCachingListeners
          .get(i);
      assertEquals(strategyConfig.cachingListenerConfigStructs[i].refId,
          cachingListenerReference.getBeanName());
    }

    // verify that the caching models were parsed
    assertEquals(cachingModels, actualPropertySource.getCachingModels()
        .getValue());

    // verify that the flushing models were parsed
    assertEquals(flushingModels, actualPropertySource.getFlushingModels()
        .getValue());

    verify();
  }

  public void testParseWithNonExistingCacheProvideFacade() {
    replay();

    try {
      parser.parse(strategyConfig.toXml(), registry);
      fail();
    } catch (NoSuchBeanDefinitionException exception) {
      // expecting this exception
    }

    verify();
  }

  protected final CacheModelParser getModelParser() {
    return modelParser;
  }

  protected final CacheProviderFacadeValidator getValidator() {
    return validator;
  }

  protected void setUp() throws Exception {
    parser = new CacheSetupStrategyParser();

    modelParserControl = MockControl.createControl(CacheModelParser.class);
    modelParser = (CacheModelParser) modelParserControl.getMock();

    validatorControl = MockControl
        .createControl(CacheProviderFacadeValidator.class);
    validator = (CacheProviderFacadeValidator) validatorControl.getMock();

    registry = new DefaultListableBeanFactory();

    strategyConfig = new StrategyConfigStruct();
    strategyConfig.cachingListenerConfigStructs = cachingListenerConfigStructs();
    strategyConfig.cachingModelConfigStructs = cachingModelConfigStructs();
    strategyConfig.flushingModelConfigStructs = flushingModelConfigStructs();
    strategyConfig.providerId = "cacheProvider";
  }

  private CachingListenerConfigStruct[] cachingListenerConfigStructs() {
    int size = 5;
    CachingListenerConfigStruct[] structs = new CachingListenerConfigStruct[size];

    String prefix = "listener_";
    for (int i = 0; i < size; i++) {
      CachingListenerConfigStruct config = new CachingListenerConfigStruct();
      config.refId = prefix + i;
      structs[i] = config;
      registry.registerBeanDefinition(config.refId, new RootBeanDefinition(
          CachingListener.class));
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
