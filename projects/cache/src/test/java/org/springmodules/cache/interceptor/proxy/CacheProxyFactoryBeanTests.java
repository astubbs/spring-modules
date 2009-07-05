/* 
 * Created on Feb 16, 2005
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

package org.springmodules.cache.interceptor.proxy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.MockControl;

import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AopConfigException;
import org.springframework.aop.target.EmptyTargetSource;
import org.springframework.util.ClassUtils;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.integration.CacheableService;
import org.springmodules.cache.integration.CacheableServiceImpl;
import org.springmodules.cache.interceptor.caching.CachingModelSourceAdvisor;
import org.springmodules.cache.interceptor.caching.NameMatchCachingInterceptor;
import org.springmodules.cache.interceptor.flush.FlushingModelSourceAdvisor;
import org.springmodules.cache.interceptor.flush.NameMatchFlushingInterceptor;
import org.springmodules.cache.key.CacheKeyGenerator;
import org.springmodules.cache.key.HashCodeCacheKeyGenerator;
import org.springmodules.cache.mock.MockCachingModel;
import org.springmodules.cache.mock.MockFlushingModel;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.CacheProviderFacade;

/**
 * <p>
 * Unit Tests for <code>{@link CacheProxyFactoryBean}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class CacheProxyFactoryBeanTests extends TestCase {

  private CacheProviderFacade cacheProviderFacade;

  private MockControl cacheProviderFacadeControl;

  private Map cachingModels;

  private CacheProxyFactoryBean factoryBean;

  private Map flushingModels;

  private CacheableServiceImpl target;

  private CacheModelValidator validator;

  private MockControl validatorControl;

  public CacheProxyFactoryBeanTests(String name) {
    super(name);
  }

  public void testAfterPropertiesSetWithCacheFlushAttributesNotSet()
      throws Exception {
    factoryBean = new CacheProxyFactoryBean();
    setUpCachingModels();

    expectAfterPropertiesSetOnCachingInterceptorOnly();
    replay();

    Person targetObject = new PersonImpl("Anakin", "Skywalker");
    factoryBean.setTarget(targetObject);

    factoryBean.afterPropertiesSet();

    // verify that the target is only advised for caching.
    Advised advised = (Advised) factoryBean.getProxy();
    Advisor[] advisors = advised.getAdvisors();
    assertEquals(1, advisors.length);
    assertEquals(CachingModelSourceAdvisor.class, advisors[0].getClass());

    verify();
  }

  public void testAfterPropertiesSetWithNullTarget() {
    expectAfterPropertiesSetOnInterceptors();
    replay();

    try {
      factoryBean.afterPropertiesSet();
      fail();
    } catch (IllegalStateException exception) {
      // we are expecting this exception.
    }

    verify();
  }

  public void testAfterPropertiesSetWithProxyInterfacesEqualToNullAndProxyTargetClassEqualToTrue() {
    expectAfterPropertiesSetOnInterceptors();
    replay();

    Person targetObject = new PersonImpl("Darth", "Vader");
    factoryBean.setTarget(targetObject);
    factoryBean.setProxyTargetClass(true);

    factoryBean.afterPropertiesSet();

    Advised advised = (Advised) factoryBean.getProxy();
    Advisor[] advisors = advised.getAdvisors();
    assertEquals(2, advisors.length);
    Advisor advisor1 = advisors[0];
    Advisor advisor2 = advisors[1];

    if (advisor1 instanceof CachingModelSourceAdvisor) {
      assertEquals(FlushingModelSourceAdvisor.class, advisor2.getClass());

    } else if (advisor1 instanceof FlushingModelSourceAdvisor) {
      assertEquals(CachingModelSourceAdvisor.class, advisor2.getClass());

    } else {
      fail("Expected: <" + CachingModelSourceAdvisor.class.getName() + "> or <"
          + FlushingModelSourceAdvisor.class.getName() + "> but was: <"
          + advisor1.getClass().getName() + ">");
    }

    verify();
  }

  public void testAfterPropertiesSetWithProxyInterfacesEqualToNullAndProxyTargetFlagEqualToFalseAndTargetInstanceOfTargetSource()
      throws Exception {
    expectAfterPropertiesSetOnInterceptors();
    replay();

    factoryBean.setProxyTargetClass(false);
    Object targetInstanceOfTargetSource = EmptyTargetSource.INSTANCE;
    factoryBean.setTarget(targetInstanceOfTargetSource);

    try {
      factoryBean.afterPropertiesSet();
      fail();
    } catch (AopConfigException exception) {
      // we are expecting this exception.
    }

    verify();
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#afterPropertiesSet()}</code> creates a
   * new proxy from on the given target object if the target object and the
   * specified proxy interfaces are not equal to <code>null</code>.
   */
  public void testAfterPropertiesSetWithProxyInterfacesNotEqualToNull()
      throws Exception {
    expectAfterPropertiesSetOnInterceptors();
    replay();

    String[] proxyInterfaces = { Person.class.getName() };
    factoryBean.setProxyInterfaces(proxyInterfaces);

    Person targetObject = new PersonImpl("Darth", "Vader");
    factoryBean.setTarget(targetObject);

    factoryBean.afterPropertiesSet();

    Object proxy = factoryBean.getProxy();
    assertNotNull(proxy);

    Class[] targetObjectInterfaces = ClassUtils.getAllInterfaces(targetObject);
    int interfacesCount = targetObjectInterfaces.length;
    Class proxyClass = proxy.getClass();
    for (int i = 0; i < interfacesCount; i++) {
      Class targetObjectInterface = targetObjectInterfaces[i];
      assertTrue("The proxy should implement the interface <"
          + targetObjectInterface.getName() + ">", targetObjectInterface
          .isAssignableFrom(proxyClass));
    }

    assertTrue("The proxy should implement the interface <"
        + Advised.class.getName() + ">", proxy instanceof Advised);

    verify();
  }

  /**
   * Verifies that the constructor
   * <code>{@link CacheProxyFactoryBean#CacheProxyFactoryBean()}</code>
   * creates a new <code>{@link NameMatchFlushingInterceptor}</code> and a new
   * <code>{@link NameMatchCachingInterceptor}</code>.
   */
  public void testCacheProxyFactoryBean() {
    assertEquals(NameMatchFlushingInterceptor.class, factoryBean
        .getFlushingInterceptor().getClass());
    assertEquals(NameMatchCachingInterceptor.class, factoryBean
        .getCachingInterceptor().getClass());
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#createTargetSource(Object)}</code>
   * returns the same object sent as argument if such argument is an instance of
   * <code>org.springframework.aop.TargetSource</code>.
   */
  public void testCreateTargetSourceWithTargetObjectInstanceOfTargetSource() {
    Object targetObject = EmptyTargetSource.INSTANCE;
    assertSame(targetObject, factoryBean.createTargetSource(targetObject));
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#createTargetSource(Object)}</code>
   * creates a new <code>org.springframework.aop.TargetSource</code> setting
   * the given object as its target.
   */
  public void testCreateTargetSourceWithTargetObjectNotInstanceOfTargetSource()
      throws Exception {
    Object targetObject = new Object();
    TargetSource targetSource = factoryBean.createTargetSource(targetObject);
    assertSame(targetObject, targetSource.getTarget());
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#getObject()}</code> returns the
   * created proxy.
   */
  public void testGetObject() {
    expectAfterPropertiesSetOnInterceptors();
    replay();

    factoryBean.setTarget(target);
    factoryBean.afterPropertiesSet();
    assertSame(factoryBean.getProxy(), factoryBean.getObject());

    verify();
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#getObjectType()}</code> returns the
   * class of the proxy if the proxy is not <code>null</code>.
   */
  public void testGetObjectTypeWhenProxyIsNotNull() {
    expectAfterPropertiesSetOnInterceptors();
    replay();

    factoryBean.setTarget(target);
    factoryBean.afterPropertiesSet();
    assertEquals(factoryBean.getProxy().getClass(), factoryBean.getObjectType());

    verify();
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#getObjectType()}</code> returns the
   * class of the target if:
   * <ul>
   * <li>The proxy is <code>null</code>.</li>
   * <li>The target is not <code>null</code> and it is an instance of
   * <code>org.springframework.aop.TargetSource</code>.</li>
   * </ul>
   */
  public void testGetObjectTypeWhenProxyIsNullAndTargetIsNotNullAndTargetIsInstanceOfTargetSource() {
    TargetSource instanceOfTargetSource = EmptyTargetSource.INSTANCE;
    factoryBean.setTarget(instanceOfTargetSource);
    // verify that the proxy is null before running the method to test.
    assertNull(factoryBean.getProxy());
    assertEquals(instanceOfTargetSource.getClass(), factoryBean.getObjectType());
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#getObjectType()}</code> returns
   * <code>null</code> if:
   * <ul>
   * <li>The proxy is <code>null</code>.</li>
   * <li>The target is not <code>null</code> and it is not an instance of
   * <code>org.springframework.aop.TargetSource</code>.</li>
   * </ul>
   */
  public void testGetObjectTypeWhenProxyIsNullAndTargetIsNotNullAndTargetIsNotInstanceOfTargetSource() {
    factoryBean.setTarget(target);
    // verify that the proxy is null before running the method to test.
    assertNull(factoryBean.getProxy());
    assertNull(factoryBean.getObjectType());
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#getObjectType()}</code> returns
   * <code>null</code> if both the proxy and the target are <code>null</code>.
   */
  public void testGetObjectTypeWhenProxyIsNullAndTargetIsNull() {
    // verify that the proxy is null before running the method to test.
    assertNull(factoryBean.getProxy());
    assertNull(factoryBean.getObjectType());
  }

  /**
   * Verifies that <code>{@link CacheProxyFactoryBean}</code> notifies the
   * Spring IoC container that is a singleton.
   */
  public void testIsSingleton() {
    assertTrue(factoryBean.isSingleton());
  }

  public void testSetCacheKeyGenerator() {
    CacheKeyGenerator keyGenerator = new HashCodeCacheKeyGenerator();
    factoryBean.setCacheKeyGenerator(keyGenerator);

    assertSame(keyGenerator, factoryBean.getCachingInterceptor()
        .cacheKeyGenerator());
  }

  public void testSetFlushingModelsWithEmptyModelMap() {
    assertFlushingModelSourceIsNotSetIfFlushingModelMapIsNullOrEmpty(new HashMap());
  }

  public void testSetFlushingModelsWithModelMapEqualToNull() {
    assertFlushingModelSourceIsNotSetIfFlushingModelMapIsNullOrEmpty(null);
  }

  public void testSetProxyInterfaces() throws Exception {
    String[] interfaceNames = new String[] { CacheableService.class.getName() };
    factoryBean.setProxyInterfaces(interfaceNames);

    Class[] expectedProxyInterfaces = new Class[] { CacheableService.class };
    Class[] actualProxyInterfaces = factoryBean.getProxyInterfaces();

    assertEquals(expectedProxyInterfaces.length, actualProxyInterfaces.length);
    assertEquals(expectedProxyInterfaces[0], actualProxyInterfaces[0]);
  }

  protected void setUp() {
    flushingModels = new HashMap();
    flushingModels.put("update*", new MockFlushingModel());

    factoryBean = new CacheProxyFactoryBean();
    factoryBean.setFlushingModels(flushingModels);
    setUpCachingModels();

    target = new CacheableServiceImpl();
  }

  private void assertFlushingModelSourceIsNotSetIfFlushingModelMapIsNullOrEmpty(
      Map models) {
    assertTrue(models == null || models.isEmpty());

    factoryBean.setFlushingModels(models);
    assertFalse(factoryBean.isHasFlushingModels());
    assertNull(factoryBean.getFlushingInterceptor().getFlushingModelSource());
  }

  private void expectAfterPropertiesSetOnCachingInterceptor() {
    cacheProviderFacadeControl.expectAndReturn(cacheProviderFacade
        .modelValidator(), validator);
    for (Iterator i = cachingModels.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();
      CachingModel model = (CachingModel) entry.getValue();
      validator.validateCachingModel(model);
    }
  }

  private void expectAfterPropertiesSetOnCachingInterceptorOnly() {
    setUpCacheModelValidator();
    setUpCacheProviderFacade();
    expectAfterPropertiesSetOnCachingInterceptor();
  }

  private void expectAfterPropertiesSetOnFlushingInterceptor() {
    cacheProviderFacadeControl.expectAndReturn(cacheProviderFacade
        .modelValidator(), validator);
    for (Iterator i = flushingModels.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();
      FlushingModel model = (FlushingModel) entry.getValue();
      validator.validateFlushingModel(model);
    }
  }

  private void expectAfterPropertiesSetOnInterceptors() {
    setUpCacheModelValidator();
    setUpCacheProviderFacade();

    expectAfterPropertiesSetOnCachingInterceptor();
    expectAfterPropertiesSetOnFlushingInterceptor();
  }

  private void replay() {
    cacheProviderFacadeControl.replay();
    validatorControl.replay();
  }

  private void setUpCacheModelValidator() {
    validatorControl = MockControl.createControl(CacheModelValidator.class);
    validator = (CacheModelValidator) validatorControl.getMock();
  }

  private void setUpCacheProviderFacade() {
    cacheProviderFacadeControl = MockControl
        .createStrictControl(CacheProviderFacade.class);
    cacheProviderFacade = (CacheProviderFacade) cacheProviderFacadeControl
        .getMock();
    factoryBean.setCacheProviderFacade(cacheProviderFacade);
  }

  private void setUpCachingModels() {
    cachingModels = new HashMap();
    cachingModels.put("get*", new MockCachingModel());
    factoryBean.setCachingModels(cachingModels);
  }

  private void verify() {
    cacheProviderFacadeControl.verify();
    validatorControl.verify();
  }
}