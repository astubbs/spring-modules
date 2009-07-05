/* 
 * Created on Oct 22, 2004
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
 * Copyright @2004 the original author or authors.
 */

package org.springmodules.cache.integration;

import java.io.Serializable;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import org.springmodules.cache.integration.KeyAndModelListCachingListener.KeyAndModel;
import org.springmodules.cache.interceptor.caching.AbstractCachingInterceptor;
import org.springmodules.cache.provider.PathUtils;
import org.springmodules.cache.util.SystemUtils;

/**
 * <p>
 * Template for test cases that verify that caching module works correctly
 * inside a Spring bean context.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public abstract class AbstractIntegrationTests extends TestCase {

  protected static final String CACHE_MANAGER_BEAN_NAME = "cacheManager";

  private static final String ANNOTATIONS_CONTEXT = "annotationsContext.xml";

  private static final String BEAN_NAME_AUTOPROXY_CREATOR_CONTEXT = "beanNameAutoproxyCreatorContext.xml";

  private static final String CACHE_PROXY_FACTORY_BEAN_CONTEXT = "cacheProxyFactoryBeanContext.xml";

  private static final String COMMONS_ATTRIBUTES_CONTEXT = "commonsAttributesContext.xml";

  private static final String DTD_FOLDER = "/dtd/";

  private static final String SCHEMA_FOLDER = "/schema/";

  protected final Log logger = LogFactory.getLog(getClass());

  private ApplicationContext applicationContext;

  /**
   * Listener that stores the keys used to store objects in the cache. This
   * listener is used to verify that objects are being cached.
   */
  private KeyAndModelListCachingListener cachingListener;

  /**
   * Bean managed by the Spring bean context. Should be advised by the
   * interceptors that perform caching and flush the cache.
   */
  private CacheableService target;

  public AbstractIntegrationTests() {
    super();
  }

  public AbstractIntegrationTests(String name) {
    super(name);
  }

  public final void testAnnotationsWithDtd() throws Exception {
    performCachingAndFlushingWithAnnotations(DTD_FOLDER);
  }

  public final void testAnnotationsWithSchema() throws Exception {
    performCachingAndFlushingWithAnnotations(SCHEMA_FOLDER);
  }

  public final void testBeanNameAutoProxyCreatorWithDtd() throws Exception {
    performCachingAndFlushingWithBeanNameAutoProxyCreator(DTD_FOLDER);
  }

  public final void testBeanNameAutoProxyCreatorWithSchema() throws Exception {
    performCachingAndFlushingWithBeanNameAutoProxyCreator(SCHEMA_FOLDER);
  }

  public final void testCacheProxyFactoryBeanWithDtd() throws Exception {
    performCachingAndFlushingWithCacheProxyFactoryBean(DTD_FOLDER);
  }

  public final void testCacheProxyFactoryBeanWithSchema() throws Exception {
    performCachingAndFlushingWithCacheProxyFactoryBean(SCHEMA_FOLDER);
  }

  public final void testCommonsAttributesWithDtd() throws Exception {
    performCachingAndFlushingWithCommonsAttributes(DTD_FOLDER);
  }

  public final void testCommonsAttributesWithSchema() throws Exception {
    performCachingAndFlushingWithCommonsAttributes(SCHEMA_FOLDER);
  }

  protected final void assertCacheEntryFromCacheIsNull(Object cacheEntry,
      Serializable key) {
    assertNull("There should not be any object stored under the key <"
        + StringUtils.quoteIfString(key) + ">", cacheEntry);
  }

  protected abstract void assertCacheWasFlushed() throws Exception;

  /**
   * Asserts that the given object was cached.
   * 
   * @param expectedCachedObject
   *          the object that should have been cached.
   * @param keyAndModelIndex
   *          the index of the key/model stored in <code>cachingListener</code>.
   * @see KeyAndModelListCachingListener
   */
  protected abstract void assertObjectWasCached(Object expectedCachedObject,
      int keyAndModelIndex) throws Exception;

  protected final ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  protected final Object cacheManager() {
    return applicationContext.getBean(CACHE_MANAGER_BEAN_NAME);
  }

  protected final KeyAndModelListCachingListener getCachingListener() {
    return cachingListener;
  }

  protected final KeyAndModel getKeyAndModel(int index) {
    List keyAndModelPairs = cachingListener.getKeyAndModelPairs();
    KeyAndModel keyModel = (KeyAndModel)keyAndModelPairs.get(index);
    return keyModel;
  }

  /**
   * Verifies that the caching and the cache-flushing are working correctly.
   */
  private void performCachingAndFlushing(String configStrategyConfig)
      throws Exception {
    setUpApplicationContext(configStrategyConfig);

    cachingListener = (KeyAndModelListCachingListener)applicationContext
        .getBean("cachingListener");

    target = (CacheableService)applicationContext.getBean("cacheableService");

    logger.debug("Storing in the cache...");
    int nameIndex = 0;

    String cachedObject = target.getName(nameIndex);
    assertTrue("The retrieved name should not be empty", StringUtils
        .hasText(cachedObject));

    assertObjectWasCached(cachedObject, nameIndex);

    // call the same method again. This time the return value should be read
    // from the cache.
    logger.debug("Reading from the cache...");
    cachedObject = target.getName(nameIndex);

    // verify that the element was cached only once. There should be only
    // one caching event registered in the listener.
    int expectedTimesObjectWasCached = 1;
    int actualTimesObjectWasCached = cachingListener.getKeyAndModelPairs()
        .size();
    assertEquals("<Number of times the same object was cached>",
        expectedTimesObjectWasCached, actualTimesObjectWasCached);

    // call the method 'updateName(int, String)'. When executed, the cache
    // should be flushed.
    logger.debug("Flushing the cache ...");
    target.updateName(nameIndex, "Darth Maul");

    assertCacheWasFlushed();

    // NULL_ENTRY should be cached, and null should be returned.
    target.updateName(++nameIndex, null);
    cachedObject = target.getName(nameIndex);
    assertObjectWasCached(AbstractCachingInterceptor.NULL_ENTRY, nameIndex);

    assertNull(cachedObject);
  }

  private void performCachingAndFlushingWithAnnotations(String configFolder)
      throws Exception {
    if (SystemUtils.atLeastJ2SE5()) {
      performCachingAndFlushing(configFolder + ANNOTATIONS_CONTEXT);
      return;
    }
    logger.info("Unable to run tests using file \"" + ANNOTATIONS_CONTEXT
        + ".\" Annotations are not supported by this version of Java");
  }

  private void performCachingAndFlushingWithBeanNameAutoProxyCreator(
      String configFolder) throws Exception {
    String configLocation = configFolder + BEAN_NAME_AUTOPROXY_CREATOR_CONTEXT;
    performCachingAndFlushing(configLocation);
  }

  private void performCachingAndFlushingWithCacheProxyFactoryBean(
      String configFolder) throws Exception {
    String configLocation = configFolder + CACHE_PROXY_FACTORY_BEAN_CONTEXT;
    performCachingAndFlushing(configLocation);
  }

  private void performCachingAndFlushingWithCommonsAttributes(
      String configFolder) throws Exception {
    String configLocation = configFolder + COMMONS_ATTRIBUTES_CONTEXT;
    performCachingAndFlushing(configLocation);
  }

  private void setUpApplicationContext(String configStrategyConfig) {
    String folder = configStrategyConfig.startsWith(DTD_FOLDER) ? DTD_FOLDER
        : SCHEMA_FOLDER;

    String root = "classpath:" + PathUtils.getPackageNameAsPath(getClass());
    String[] configLocations = { root + folder + "cacheContext.xml",
        root + configStrategyConfig };
    applicationContext = new ClassPathXmlApplicationContext(configLocations);
  }
}