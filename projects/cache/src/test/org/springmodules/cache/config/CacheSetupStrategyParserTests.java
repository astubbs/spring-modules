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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.classextension.MockClassControl;
import org.w3c.dom.Element;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.ObjectUtils;

import org.springmodules.cache.interceptor.caching.CachingListener;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheSetupStrategyParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheSetupStrategyParserTests extends TestCase {

  private AbstractCacheSetupStrategyParser parser;

  private MockClassControl parserControl;

  private class StrategyConfigStruct {
    String providerId = "";

    CachingListenerConfigStruct[] cachingListenerConfigStructs = null;

    Element toXml() {
      Element root = new DomElementStub("abstract");
      root.setAttribute("providerId", providerId);

      if (ObjectUtils.isEmpty(cachingListenerConfigStructs)) {
        Element listeners = new DomElementStub("cachingListeners");

        int size = cachingListenerConfigStructs.length;
        for (int i = 0; i < size; i++) {
          Element listener = cachingListenerConfigStructs[i].toXml();
          listeners.appendChild(listener);
        }

        root.appendChild(listeners);
      }

      return root;
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

  private StrategyConfigStruct strategyConfig;

  private BeanDefinitionRegistry registry;

  /**
   * Constructor.
   * 
   * @param name
   */
  public CacheSetupStrategyParserTests(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    Class target = AbstractCacheSetupStrategyParser.class;

    Method getCacheModelParserMethod = target.getDeclaredMethod(
        "getCacheModelParser", new Class[0]);
    Method getCacheProviderFacadeValidatorMethod = target.getDeclaredMethod(
        "getCacheProviderFacadeValidator", new Class[0]);
    Method parseCacheSetupStrategyMethod = target.getDeclaredMethod(
        "parseCacheSetupStrategy", new Class[] { Element.class,
            BeanDefinitionRegistry.class,
            CacheSetupStrategyPropertySource.class });

    Method[] methodsToMock = { getCacheModelParserMethod,
        getCacheProviderFacadeValidatorMethod, parseCacheSetupStrategyMethod };

    parserControl = MockClassControl.createControl(target, null, null,
        methodsToMock);
    parser = (AbstractCacheSetupStrategyParser) parserControl.getMock();

    registry = new DefaultListableBeanFactory();

    strategyConfig = new StrategyConfigStruct();
    strategyConfig.cachingListenerConfigStructs = cachingListenerConfigStructs();
    strategyConfig.providerId = "cacheProvider";
  }

  public void testParseWithNonExistingCacheProvideFacade() {
    try {
      parser.parse(strategyConfig.toXml(), registry);
      fail();
    } catch (NoSuchBeanDefinitionException exception) {
      // expecting this exception
    }
  }

  public void testParse() {

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
}
