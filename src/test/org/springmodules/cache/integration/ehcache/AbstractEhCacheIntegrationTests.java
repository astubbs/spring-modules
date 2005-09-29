package org.springmodules.cache.integration.ehcache;

import java.io.Serializable;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springmodules.cache.integration.AbstractIntegrationTests;
import org.springmodules.cache.provider.ehcache.EhCacheModel;

/**
 * <p>
 * Template for test cases that verify that the caching module works correctly
 * inside a Spring bean context when using EHCache as the cache provider.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.11 $ $Date: 2005/09/29 01:22:13 $
 */
public abstract class AbstractEhCacheIntegrationTests extends
    AbstractIntegrationTests {

  /**
   * EHCache cache.
   */
  private Cache cache;

  public AbstractEhCacheIntegrationTests() {
    super();
  }

  /**
   * @see AbstractIntegrationTests#assertCacheWasFlushed()
   */
  protected final void assertCacheWasFlushed() throws Exception {
    Serializable key = getGeneratedKey(0);

    Element cacheEntry = cache.get(key);

    assertCacheEntryFromCacheIsNull(cacheEntry, key);
  }

  /**
   * @see AbstractIntegrationTests#assertCorrectCacheModelConfiguration(Map)
   */
  protected final void assertCorrectCacheModelConfiguration(Map cacheModels) {
    EhCacheModel expected = new EhCacheModel();
    expected.setCacheName("testCache");

    String cacheModelId = "test";
    Object actual = cacheModels.get(cacheModelId);

    assertEqualCacheModules(expected, actual, cacheModelId);
  }

  /**
   * @see AbstractIntegrationTests#assertObjectWasCached(Object, int)
   */
  protected final void assertObjectWasCached(Object expectedCachedObject,
      int keyIndex) throws Exception {
    Serializable key = getGeneratedKey(keyIndex);

    // get the cache entry stored under the key we got.
    Element cachedElement = cache.get(key);
    Object actualCachedObject = cachedElement.getValue();

    assertEqualCachedObjects(expectedCachedObject, actualCachedObject);
  }

  /**
   * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#onSetUp()
   */
  protected final void onSetUp() throws Exception {
    CacheManager cacheManager = (CacheManager) applicationContext
        .getBean("cacheManager");

    cache = cacheManager.getCache("testCache");
  }
}