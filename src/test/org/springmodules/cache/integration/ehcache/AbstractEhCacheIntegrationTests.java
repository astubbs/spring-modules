package org.springmodules.cache.integration.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springmodules.cache.integration.AbstractCacheIntegrationTests;
import org.springmodules.cache.integration.KeyAndModelListCachingListener.KeyAndModel;
import org.springmodules.cache.provider.ehcache.EhCacheCachingModel;

/**
 * <p>
 * Template for test cases that verify that the caching module works correctly
 * inside a Spring bean context when using EHCache as the cache provider.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.12 $ $Date: 2005/10/13 04:52:53 $
 */
public abstract class AbstractEhCacheIntegrationTests extends
    AbstractCacheIntegrationTests {

  protected static final String CACHE_CONFIG = "**/ehCacheContext.xml";

  private CacheManager cacheManager;

  public AbstractEhCacheIntegrationTests() {
    super();
  }

  /**
   * @see AbstractCacheIntegrationTests#assertCacheWasFlushed()
   */
  protected final void assertCacheWasFlushed() throws Exception {
    int index = 0;
    Element element = getCacheElement(index);
    assertCacheEntryFromCacheIsNull(element, getKeyAndModel(index).key);
  }

  /**
   * @see AbstractCacheIntegrationTests#assertObjectWasCached(Object, int)
   */
  protected final void assertObjectWasCached(Object expectedCachedObject,
      int keyAndModelIndex) throws Exception {
    Element element = getCacheElement(keyAndModelIndex);
    assertEquals(expectedCachedObject, element.getValue());
  }

  private Element getCacheElement(int keyAndModelIndex) throws Exception {
    KeyAndModel keyAndModel = getKeyAndModel(keyAndModelIndex);
    EhCacheCachingModel model = (EhCacheCachingModel) keyAndModel.model;
    Cache cache = cacheManager.getCache(model.getCacheName());
    return cache.get(keyAndModel.key);
  }

  /**
   * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#onSetUp()
   */
  protected final void onSetUp() {
    cacheManager = (CacheManager) applicationContext
        .getBean(CACHE_MANAGER_BEAN_ID);
  }
}