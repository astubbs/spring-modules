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

import java.util.Properties;

import junit.framework.TestCase;

import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AopConfigException;
import org.springframework.aop.target.EmptyTargetSource;
import org.springframework.util.ClassUtils;
import org.springmodules.cache.integration.CacheableService;
import org.springmodules.cache.integration.CacheableServiceImpl;
import org.springmodules.cache.interceptor.caching.CachingAttributeSourceAdvisor;
import org.springmodules.cache.interceptor.flush.CacheFlushAttributeSourceAdvisor;

/**
 * <p>
 * Unit Tests for <code>{@link CacheProxyFactoryBean}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.8 $ $Date: 2005/09/27 04:37:34 $
 */
public final class CacheProxyFactoryBeanTests extends TestCase {

  private Properties cacheFlushAttributes;

  private CacheProxyFactoryBean cacheProxyFactoryBean;

  private Properties cachingAttributes;

  /**
   * Target of the proxy factory.
   */
  private CacheableServiceImpl target;

  public CacheProxyFactoryBeanTests(String name) {
    super(name);
  }

  private void assertCacheFlushAttributeSourceIsNotSetIfCacheFlushAttributesAreNullOrEmpty(
      Properties nullOrEmptyCacheFlushAttributes) {
    assertTrue(nullOrEmptyCacheFlushAttributes == null
        || nullOrEmptyCacheFlushAttributes.isEmpty());

    cacheProxyFactoryBean
        .setCacheFlushAttributes(nullOrEmptyCacheFlushAttributes);
    assertFalse(cacheProxyFactoryBean.isHasCacheFlushAttributes());
    assertNull(cacheProxyFactoryBean.getCacheFlushInterceptor()
        .getCacheFlushAttributeSource());
  }

  protected void setUp() throws Exception {
    super.setUp();

    cacheProxyFactoryBean = new CacheProxyFactoryBean();

    setUpCachingAttributes();

    cacheFlushAttributes = new Properties();
    cacheFlushAttributes.setProperty("update*", "[cacheProfileIds=test]");
    cacheProxyFactoryBean.setCacheFlushAttributes(cacheFlushAttributes);

    target = new CacheableServiceImpl();
  }

  private void setUpCachingAttributes() {
    cachingAttributes = new Properties();
    cachingAttributes.setProperty("get*", "[cacheProfileId=main]");
    cacheProxyFactoryBean.setCachingAttributes(cachingAttributes);
  }

  public void testAfterPropertiesSetWithCacheFlushAttributesNotSet()
      throws Exception {
    cacheProxyFactoryBean = new CacheProxyFactoryBean();
    setUpCachingAttributes();

    Person targetObject = new PersonImpl("Anakin", "Skywalker");
    cacheProxyFactoryBean.setTarget(targetObject);

    // cacheProxyFactoryBean.setCacheFlushAttributes(null);

    cacheProxyFactoryBean.afterPropertiesSet();

    // verify that the target is only advised for caching.
    Advised advised = (Advised) cacheProxyFactoryBean.getProxy();
    Advisor[] advisors = advised.getAdvisors();
    assertEquals(1, advisors.length);
    assertEquals(CachingAttributeSourceAdvisor.class, advisors[0].getClass());
  }

  public void testAfterPropertiesSetWithNullTarget() {
    try {
      cacheProxyFactoryBean.afterPropertiesSet();
      fail();
    } catch (IllegalStateException exception) {
      // we are expecting this exception.
    }
  }

  public void testAfterPropertiesSetWithProxyInterfacesEqualToNullAndProxyTargetClassEqualToTrue() {
    Person targetObject = new PersonImpl("Darth", "Vader");
    cacheProxyFactoryBean.setTarget(targetObject);
    cacheProxyFactoryBean.setProxyTargetClass(true);

    cacheProxyFactoryBean.afterPropertiesSet();

    Advised advised = (Advised) cacheProxyFactoryBean.getProxy();
    Advisor[] advisors = advised.getAdvisors();
    assertEquals(2, advisors.length);

    Advisor firstAdvisor = advisors[0];
    Advisor secondAdvisor = advisors[1];

    if (firstAdvisor instanceof CachingAttributeSourceAdvisor) {
      assertEquals(CacheFlushAttributeSourceAdvisor.class, secondAdvisor
          .getClass());

    } else if (firstAdvisor instanceof CacheFlushAttributeSourceAdvisor) {
      assertEquals(CachingAttributeSourceAdvisor.class, secondAdvisor
          .getClass());
    } else {
      fail("Expected: <" + CachingAttributeSourceAdvisor.class.getName()
          + "> or <" + CacheFlushAttributeSourceAdvisor.class.getName()
          + "> but was: <" + firstAdvisor.getClass().getName() + ">");
    }
  }

  public void testAfterPropertiesSetWithProxyInterfacesEqualToNullAndProxyTargetFlagEqualToFalseAndTargetInstanceOfTargetSource()
      throws Exception {
    cacheProxyFactoryBean.setProxyTargetClass(false);
    Object targetInstanceOfTargetSource = EmptyTargetSource.INSTANCE;
    cacheProxyFactoryBean.setTarget(targetInstanceOfTargetSource);

    try {
      cacheProxyFactoryBean.afterPropertiesSet();
      fail();
    } catch (AopConfigException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#afterPropertiesSet()}</code> creates a
   * new proxy from on the given target object if the target object and the
   * specified proxy interfaces are not equal to <code>null</code>.
   */
  public void testAfterPropertiesSetWithProxyInterfacesNotEqualToNull()
      throws Exception {
    String[] proxyInterfaces = { Person.class.getName() };
    cacheProxyFactoryBean.setProxyInterfaces(proxyInterfaces);

    Person targetObject = new PersonImpl("Darth", "Vader");
    cacheProxyFactoryBean.setTarget(targetObject);

    cacheProxyFactoryBean.afterPropertiesSet();

    Object proxy = cacheProxyFactoryBean.getProxy();
    assertNotNull("The proxy should not be null", proxy);

    Class[] targetObjectInterfaces = ClassUtils.getAllInterfaces(targetObject);
    int interfacesCount = targetObjectInterfaces.length;
    Class proxyClass = proxy.getClass();
    for (int i = 0; i < interfacesCount; i++) {
      Class targetObjectInterface = targetObjectInterfaces[i];
      assertTrue("The proxy should implement the interface '"
          + targetObjectInterface.getName(), targetObjectInterface
          .isAssignableFrom(proxyClass));
    }

    assertTrue("The proxy should implement the interface '"
        + Advised.class.getName() + "'", proxy instanceof Advised);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#createTargetSource(Object)}</code>
   * returns the same object sent as argument if such argument is an instance of
   * <code>org.springframework.aop.TargetSource</code>.
   */
  public void testCreateTargetSourceWithTargetObjectInstanceOfTargetSource() {
    Object targetObject = EmptyTargetSource.INSTANCE;

    TargetSource targetSource = cacheProxyFactoryBean
        .createTargetSource(targetObject);

    assertSame("<Target source>", targetObject, targetSource);
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

    TargetSource targetSource = cacheProxyFactoryBean
        .createTargetSource(targetObject);
    Object actualTarget = targetSource.getTarget();

    assertSame("<Target>", targetObject, actualTarget);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#getObject()}</code> returns the
   * created proxy.
   */
  public void testGetObject() {
    cacheProxyFactoryBean.setTarget(target);
    cacheProxyFactoryBean.afterPropertiesSet();

    Object expectedProxy = cacheProxyFactoryBean.getProxy();
    Object actualProxy = cacheProxyFactoryBean.getObject();

    assertSame(expectedProxy, actualProxy);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#getObjectType()}</code> returns the
   * class of the proxy if the proxy is not <code>null</code>.
   */
  public void testGetObjectTypeWhenProxyIsNotNull() {
    cacheProxyFactoryBean.setTarget(target);
    cacheProxyFactoryBean.afterPropertiesSet();

    Class expectedObjectType = cacheProxyFactoryBean.getProxy().getClass();
    Class actualObjectType = cacheProxyFactoryBean.getObjectType();

    assertEquals(expectedObjectType, actualObjectType);
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
    cacheProxyFactoryBean.setTarget(instanceOfTargetSource);

    // verify that the proxy is null before running the method to test.
    assertNull(cacheProxyFactoryBean.getProxy());

    Class expectedObjectType = instanceOfTargetSource.getClass();
    Class actualObjectType = cacheProxyFactoryBean.getObjectType();

    assertEquals(expectedObjectType, actualObjectType);
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
    cacheProxyFactoryBean.setTarget(target);

    // verify that the proxy is null before running the method to test.
    assertNull(cacheProxyFactoryBean.getProxy());

    Class actualObjectType = cacheProxyFactoryBean.getObjectType();

    assertNull(actualObjectType);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#getObjectType()}</code> returns
   * <code>null</code> if both the proxy and the target are <code>null</code>.
   */
  public void testGetObjectTypeWhenProxyIsNullAndTargetIsNull() {
    // verify that the proxy is null before running the method to test.
    assertNull(cacheProxyFactoryBean.getProxy());

    Class actualObjectType = cacheProxyFactoryBean.getObjectType();

    assertNull(actualObjectType);
  }

  /**
   * Verifies that <code>{@link CacheProxyFactoryBean}</code> notifies the
   * Spring IoC container that is a singleton.
   */
  public void testIsSingleton() {
    assertTrue("The proxy factory should be a singleton", cacheProxyFactoryBean
        .isSingleton());
  }

  public void testSetCacheFlushAttributesWithAttributesEqualToNull() {
    assertCacheFlushAttributeSourceIsNotSetIfCacheFlushAttributesAreNullOrEmpty(null);
  }

  public void testSetCacheFlushAttributesWithEmptyAttributes() {
    assertCacheFlushAttributeSourceIsNotSetIfCacheFlushAttributesAreNullOrEmpty(new Properties());
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#setProxyInterfaces(String[])}</code>
   * gets the classes of the given names of interfaces.
   */
  public void testSetProxyInterfaces() throws Exception {
    String[] interfaceNames = new String[] { CacheableService.class.getName() };

    cacheProxyFactoryBean.setProxyInterfaces(interfaceNames);

    Class[] expectedProxyInterfaces = new Class[] { CacheableService.class };
    Class[] actualProxyInterfaces = cacheProxyFactoryBean.getProxyInterfaces();

    assertEquals("<Number of proxy interfaces>",
        expectedProxyInterfaces.length, actualProxyInterfaces.length);

    Class expectedProxyInterface = expectedProxyInterfaces[0];
    Class actualProxyInterface = actualProxyInterfaces[0];

    assertEquals("<Proxy interface>", expectedProxyInterface,
        actualProxyInterface);
  }
}