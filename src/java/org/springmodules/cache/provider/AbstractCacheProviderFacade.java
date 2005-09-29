/* 
 * Created on Nov 10, 2004
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

package org.springmodules.cache.provider;

import java.beans.PropertyEditor;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springmodules.cache.serializable.SerializableFactory;
import org.springmodules.util.ArrayUtils;
import org.springmodules.util.Strings;

/**
 * <p>
 * Template for implementations of <code>{@link CacheProviderFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public abstract class AbstractCacheProviderFacade implements
    CacheProviderFacade {

  /**
   * Map that stores implementations of <code>{@link CacheModel}</code>. Each
   * entry is stored using a unique id (a <code>String</code>).
   */
  private Map cacheModels;

  private boolean failQuietlyEnabled;

  protected final Log logger = LogFactory.getLog(getClass());

  private SerializableFactory serializableFactory;

  private CacheProviderFacadeStatus status;

  public AbstractCacheProviderFacade() {
    super();
    setStatus(CacheProviderFacadeStatus.UNINITIALIZED);
  }

  /**
   * Validates the properties of this class after being set by the
   * <code>BeanFactory</code>.
   * 
   * @see #validateCacheManager()
   * @see #validateCacheModels()
   */
  public final void afterPropertiesSet() throws FatalCacheException {
    validateCacheManager();

    if (cacheModels instanceof Properties) {
      setCacheModelsFromProperties((Properties) cacheModels);
    }

    validateCacheModels();

    setStatus(CacheProviderFacadeStatus.READY);
  }

  protected final void assertCacheManagerIsNotNull(Object cacheManager)
      throws FatalCacheException {
    if (cacheManager == null) {
      throw new FatalCacheException("The cache manager should not be null");
    }
  }

  /**
   * @see CacheProviderFacade#cancelCacheUpdate(Serializable)
   */
  public final void cancelCacheUpdate(Serializable cacheKey)
      throws CacheException {

    if (logger.isDebugEnabled()) {
      logger.debug("Attempt to cancel a cache update using the key <"
          + cacheKey + ">");
    }

    try {
      onCancelCacheUpdate(cacheKey);

    } catch (CacheException exception) {
      handleCatchedException(exception);
    }
  }

  /**
   * @see CacheProviderFacade#flushCache(String[])
   */
  public final void flushCache(String[] cacheModelIds) throws CacheException {

    if (logger.isDebugEnabled()) {
      logger.debug("Attempt to flush the cache using cache model ids <"
          + ArrayUtils.toString(cacheModelIds) + ">");
    }

    if (cacheModelIds != null) {
      int cacheModelIdCount = cacheModelIds.length;

      try {
        for (int i = 0; i < cacheModelIdCount; i++) {
          CacheModel cacheModel = getCacheModel(cacheModelIds[i]);
          if (cacheModel != null) {
            onFlushCache(cacheModel);
          }
        }
        if (logger.isDebugEnabled()) {
          logger.debug("Cache has been flushed.");
        }

      } catch (CacheException exception) {
        handleCatchedException(exception);
      }
    }
  }

  /**
   * Returns an implementation of <code>{@link CacheModel}</code> stored in
   * <code>{@link #cacheModels}</code>.
   * 
   * @param cacheModelId
   *          the id of the cache model to retrieve.
   * @return a cache model.
   */
  protected final CacheModel getCacheModel(String cacheModelId) {
    CacheModel cacheModel = null;

    if (StringUtils.hasText(cacheModelId) && cacheModels != null) {
      cacheModel = (CacheModel) cacheModels.get(cacheModelId);
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Obtained cache model <" + cacheModel + ">");
    }

    return cacheModel;
  }

  /**
   * Returns a property editor for an implementation of
   * <code>{@link CacheModel}</code>.
   * 
   * @return a property editor for cache models.
   */
  protected abstract PropertyEditor getCacheModelEditor();

  /**
   * Returns an unmodifiable view of <code>{@link #cacheModels}</code>.
   * 
   * @return an unmodifiable view of the existing cache models.
   */
  public final Map getCacheModels() {
    return (cacheModels != null) ? Collections.unmodifiableMap(cacheModels)
        : null;
  }

  /**
   * Returns a validator for the properties of cache models.
   * 
   * @return a validator for the properties of cache models.
   */
  protected abstract CacheModelValidator getCacheModelValidator();

  /**
   * @see CacheProviderFacade#getFromCache(Serializable, String)
   */
  public final Object getFromCache(Serializable cacheKey, String cacheModelId)
      throws CacheException {

    if (logger.isDebugEnabled()) {
      logger.debug("Attempt to retrieve a cache entry using key <" + cacheKey
          + "> and cache model id " + Strings.quote(cacheModelId));
    }

    Object cachedObject = null;

    try {
      CacheModel cacheModel = getCacheModel(cacheModelId);

      if (cacheModel != null) {
        cachedObject = onGetFromCache(cacheKey, cacheModel);
      }

      if (logger.isDebugEnabled()) {
        logger.debug("Retrieved cache element <" + cachedObject + ">");
      }

    } catch (CacheException exception) {
      handleCatchedException(exception);
    }
    return cachedObject;
  }

  /**
   * @see CacheProviderFacade#getStatus()
   */
  public CacheProviderFacadeStatus getStatus() {
    return status;
  }

  /**
   * Rethrows the given exception if
   * <code>{@link #isFailQuietlyEnabled()}</code> returns <code>true</code>.
   * 
   * @param exception
   *          the catched exception to be potentially rethrown.
   * @throws CacheException
   *           if this cache provider has not been configured to "fail quietly."
   */
  protected final void handleCatchedException(CacheException exception)
      throws CacheException {
    logger.error(exception.getMessage(), exception);
    if (!isFailQuietlyEnabled()) {
      throw exception;
    }
  }

  /**
   * @see CacheProviderFacade#isFailQuietlyEnabled()
   */
  public final boolean isFailQuietlyEnabled() {
    return failQuietlyEnabled;
  }

  /**
   * @return <code>true</code> if the cache used by this facade can only store
   *         serializable objects.
   */
  protected abstract boolean isSerializableCacheElementRequired();

  /**
   * Makes the given object serializable if:
   * <ul>
   * <li>The cache can only store serializable objects</li>
   * <li>The given object does not implement <code>java.io.Serializable</code>
   * </li>
   * </ul>
   * Otherwise, will return the same object passed as argument.
   * 
   * @param obj
   *          the object to check.
   * @return the given object as a serializable object if necessary.
   * @throws ObjectCannotBeCachedException
   *           if the cache requires serializable elements, the given object
   *           does not implement <code>java.io.Serializable</code> and the
   *           factory of serializable objects is <code>null</code>.
   * 
   * @see #setSerializableFactory(SerializableFactory)
   */
  protected final Object makeSerializableIfNecessary(Object obj) {
    if (!isSerializableCacheElementRequired()) {
      return obj;
    }
    if (obj instanceof Serializable) {
      return obj;
    }
    if (serializableFactory != null) {
      return serializableFactory.makeSerializableIfNecessary(obj);
    }
    throw new ObjectCannotBeCachedException(
        "The cache can only store implementations of java.io.Serializable");
  }

  /**
   * Cancels the update being made to the cache.
   * 
   * @param cacheKey
   *          the key being used in the cache update.
   * @throws CacheException
   *           if an unexpected error takes place when attempting to cancel the
   *           update.
   */
  protected void onCancelCacheUpdate(Serializable cacheKey)
      throws CacheException {
    logger.info("Cache provider does not support cancelation of updates");
  }

  /**
   * Flushes the caches specified in the given model.
   * 
   * @param cacheModel
   *          the cache model that specifies what and how to flush.
   * @throws CacheException
   *           if an unexpected error takes place when flushing the cache.
   */
  protected abstract void onFlushCache(CacheModel cacheModel)
      throws CacheException;

  /**
   * Retrieves an entry from the cache.
   * 
   * @param cacheKey
   *          the key under which the entry is stored.
   * @param cacheModel
   *          the the cache model that specifies how to retrieve an entry.
   * @return the cached entry.
   * @throws CacheException
   *           if an unexpected error takes place when retrieving the entry from
   *           the cache.
   */
  protected abstract Object onGetFromCache(Serializable cacheKey,
      CacheModel cacheModel) throws CacheException;

  /**
   * Stores an object in the cache.
   * 
   * @param cacheKey
   *          the key used to store the object.
   * @param cacheModel
   *          the cache model that specifies how to store an object in the
   *          cache.
   * @param objectToCache
   *          the object to store in the cache.
   * @throws CacheException
   *           if an unexpected error takes place when storing an object in the
   *           cache.
   */
  protected abstract void onPutInCache(Serializable cacheKey,
      CacheModel cacheModel, Object objectToCache) throws CacheException;

  /**
   * Removes an entry from the cache.
   * 
   * @param cacheKey
   *          the key the entry to remove is stored under.
   * @param cacheModel
   *          the cache model that specifies how to remove the entry from the
   *          cache.
   * @throws CacheException
   *           if an unexpected error takes place when removing an entry from
   *           the cache.
   */
  protected abstract void onRemoveFromCache(Serializable cacheKey,
      CacheModel cacheModel) throws CacheException;

  /**
   * @see CacheProviderFacade#putInCache(Serializable, String, Object)
   * @see #makeSerializableIfNecessary(Object)
   */
  public final void putInCache(Serializable cacheKey, String cacheModelId,
      Object objectToCache) throws CacheException {

    if (logger.isDebugEnabled()) {
      logger.debug("Attempt to store the object <" + objectToCache
          + "> in the cache using key <" + cacheKey + "> and cache model id "
          + Strings.quote(cacheModelId));
    }

    try {
      Object newCacheElement = makeSerializableIfNecessary(objectToCache);

      CacheModel cacheModel = getCacheModel(cacheModelId);
      if (cacheModel != null) {
        onPutInCache(cacheKey, cacheModel, newCacheElement);

        if (logger.isDebugEnabled()) {
          logger.debug("Object was successfully stored in the cache");
        }
      }
    } catch (CacheException exception) {
      handleCatchedException(exception);
    }
  }

  /**
   * @see CacheProviderFacade#removeFromCache(Serializable, String)
   */
  public final void removeFromCache(Serializable cacheKey, String cacheModelId)
      throws CacheException {

    if (logger.isDebugEnabled()) {
      logger.debug("Attempt to remove an entry from the cache using key <"
          + cacheKey + "> and cache model id " + Strings.quote(cacheModelId));
    }

    CacheModel cacheModel = getCacheModel(cacheModelId);
    if (cacheModel != null) {
      try {
        onRemoveFromCache(cacheKey, cacheModel);

        if (logger.isDebugEnabled()) {
          logger.debug("Object removed from the cache");
        }

      } catch (CacheException exception) {
        handleCatchedException(exception);
      }
    }
  }

  public final void setCacheModels(Map newCacheModels) {
    cacheModels = newCacheModels;
  }

  /**
   * @throws FatalCacheException
   *           if one or more cache models cannot be created from the given
   *           properties.
   */
  private void setCacheModelsFromProperties(Properties properties)
      throws FatalCacheException {
    Map newCacheModels = new HashMap();
    PropertyEditor cacheModelEditor = getCacheModelEditor();

    String cacheModelId = null;
    String cacheModelAsProperties = null;

    try {
      for (Iterator i = properties.keySet().iterator(); i.hasNext();) {
        cacheModelId = (String) i.next();
        cacheModelAsProperties = properties.getProperty(cacheModelId);

        cacheModelEditor.setAsText(cacheModelAsProperties);
        CacheModel cacheModel = (CacheModel) cacheModelEditor.getValue();

        newCacheModels.put(cacheModelId, cacheModel);
      }
    } catch (RuntimeException exception) {
      throw new FatalCacheException(
          "Unable to create cache model from the properties "
              + Strings.quote(cacheModelAsProperties) + " with id "
              + Strings.quote(cacheModelId), exception);
    }

    setCacheModels(newCacheModels);
  }

  public final void setFailQuietlyEnabled(boolean newFailQuietlyEnabled) {
    failQuietlyEnabled = newFailQuietlyEnabled;
  }

  /**
   * Sets the factory that makes serializable the objects to be stored in the
   * cache (if the cache requires serializable elements).
   */
  public final void setSerializableFactory(
      SerializableFactory newSerializableFactory) {
    serializableFactory = newSerializableFactory;
  }

  private void setStatus(CacheProviderFacadeStatus newStatus) {
    status = newStatus;
  }

  /**
   * Validates the cache manager used by this facade.
   * 
   * @throws FatalCacheException
   *           if the cache manager is in an invalid state.
   */
  protected void validateCacheManager() throws FatalCacheException {
    // no implementation.
  }

  /**
   * @throws FatalCacheException
   *           if the map of cache models is <code>null</code> or empty.
   * @throws FatalCacheException
   *           if one or more cache models have invalid values of any of their
   *           properties.
   */
  private void validateCacheModels() throws FatalCacheException {
    if (cacheModels == null || cacheModels.isEmpty()) {
      throw new FatalCacheException(
          "The map of cache models should not be empty");
    }

    CacheModelValidator cacheModelValidator = getCacheModelValidator();

    String cacheModelId = null;
    Object cacheModel = null;

    try {
      for (Iterator i = cacheModels.keySet().iterator(); i.hasNext();) {
        cacheModelId = (String) i.next();
        cacheModel = cacheModels.get(cacheModelId);

        cacheModelValidator.validateCacheModel(cacheModel);
      }

    } catch (InvalidCacheModelException exception) {
      throw new FatalCacheException("Invalid cache model <" + cacheModel
          + "> with id " + Strings.quote(cacheModelId), exception);
    }
  }

}