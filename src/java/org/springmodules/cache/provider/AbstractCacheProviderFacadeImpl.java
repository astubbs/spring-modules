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
import org.springmodules.cache.util.ArrayUtils;
import org.springmodules.cache.util.Strings;

/**
 * <p>
 * Template for implementations of <code>{@link CacheProviderFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.18 $ $Date: 2005/09/21 02:45:45 $
 */
public abstract class AbstractCacheProviderFacadeImpl implements
    CacheProviderFacade {

  /**
   * Map that stores implementations of <code>{@link CacheProfile}</code>.
   * Each entry is stored using a unique id (a <code>String</code>).
   */
  private Map cacheProfiles;

  private boolean failQuietlyEnabled;

  protected final Log logger = LogFactory.getLog(getClass());

  private SerializableFactory serializableFactory;

  private CacheProviderFacadeStatus status;

  public AbstractCacheProviderFacadeImpl() {
    super();
    setStatus(CacheProviderFacadeStatus.UNINITIALIZED);
  }

  /**
   * Validates the properties of this class after being set by the
   * <code>BeanFactory</code>.
   * 
   * @see #validateCacheManager()
   * @see #validateCacheProfiles()
   */
  public final void afterPropertiesSet()
      throws IllegalCacheProviderStateException {

    try {
      validateCacheManager();

      if (cacheProfiles instanceof Properties) {
        setCacheProfilesFromProperties((Properties) cacheProfiles);
      }

      validateCacheProfiles();

    } catch (IllegalCacheProviderStateException exception) {
      setStatus(CacheProviderFacadeStatus.INVALID);

      handleCatchedException(exception);
    }

    setStatus(CacheProviderFacadeStatus.READY);
  }

  /**
   * @see CacheProviderFacade#cancelCacheUpdate(Serializable)
   */
  public final void cancelCacheUpdate(Serializable cacheKey)
      throws CacheException {

    if (logger.isDebugEnabled()) {
      String logMessage = "Attempt to cancel a cache update using the key <"
          + cacheKey + ">";

      logger.debug(logMessage);
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
  public final void flushCache(String[] cacheProfileIds) throws CacheException {

    if (logger.isDebugEnabled()) {
      logger.debug("Attempt to flush the cache using cache profile ids <"
          + ArrayUtils.toString(cacheProfileIds) + ">");
    }

    if (cacheProfileIds != null) {
      int cacheProfileIdCount = cacheProfileIds.length;

      try {
        for (int i = 0; i < cacheProfileIdCount; i++) {
          String cacheProfileId = cacheProfileIds[i];

          CacheProfile cacheProfile = getCacheProfile(cacheProfileId);
          if (cacheProfile != null) {
            onFlushCache(cacheProfile);
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
   * Returns an implementation of <code>{@link CacheProfile}</code> stored in
   * <code>{@link #cacheProfiles}</code>.
   * 
   * @param cacheProfileId
   *          the id of the cache profile to retrieve.
   * @return a cache profile.
   */
  protected final CacheProfile getCacheProfile(String cacheProfileId) {
    CacheProfile cacheProfile = null;

    if (StringUtils.hasText(cacheProfileId) && cacheProfiles != null) {
      cacheProfile = (CacheProfile) cacheProfiles.get(cacheProfileId);
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Obtained cache profile <" + cacheProfile + ">");
    }

    return cacheProfile;
  }

  /**
   * Returns a property editor for an implementation of
   * <code>{@link CacheProfile}</code>.
   * 
   * @return a property editor for cache profiles.
   */
  protected abstract PropertyEditor getCacheProfileEditor();

  /**
   * Returns an unmodifiable view of <code>{@link #cacheProfiles}</code>.
   * 
   * @return an unmodifiable view of the member variable
   *         <code>cacheProfiles</code>.
   */
  public final Map getCacheProfiles() {
    return (cacheProfiles != null) ? Collections.unmodifiableMap(cacheProfiles)
        : null;
  }

  /**
   * Returns a validator for the properties of cache profiles.
   * 
   * @return a validator for the properties of cache profiles.
   */
  protected abstract CacheProfileValidator getCacheProfileValidator();

  /**
   * @see CacheProviderFacade#getFromCache(Serializable, String)
   */
  public final Object getFromCache(Serializable cacheKey, String cacheProfileId)
      throws CacheException {

    if (logger.isDebugEnabled()) {
      logger.debug("Attempt to retrieve a cache entry using key <" + cacheKey
          + "> and cache profile id " + Strings.quote(cacheProfileId));
    }

    Object cachedObject = null;

    try {
      CacheProfile cacheProfile = getCacheProfile(cacheProfileId);

      if (cacheProfile != null) {
        cachedObject = onGetFromCache(cacheKey, cacheProfile);
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
   * @throws IllegalObjectToCacheException
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
    throw new IllegalObjectToCacheException(
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
   * Flushes the caches specified in the given profile.
   * 
   * @param cacheProfile
   *          the cache profile that specifies what and how to flush.
   * @throws CacheException
   *           if an unexpected error takes place when flushing the cache.
   */
  protected abstract void onFlushCache(CacheProfile cacheProfile)
      throws CacheException;

  /**
   * Retrieves an entry from the cache.
   * 
   * @param cacheKey
   *          the key under which the entry is stored.
   * @param cacheProfile
   *          the the cache profile that specifies how to retrieve an entry.
   * @return the cached entry.
   * @throws CacheException
   *           if an unexpected error takes place when retrieving the entry from
   *           the cache.
   */
  protected abstract Object onGetFromCache(Serializable cacheKey,
      CacheProfile cacheProfile) throws CacheException;

  /**
   * Stores an object in the cache.
   * 
   * @param cacheKey
   *          the key used to store the object.
   * @param cacheProfile
   *          the cache profile that specifies how to store an object in the
   *          cache.
   * @param objectToCache
   *          the object to store in the cache.
   * @throws CacheException
   *           if an unexpected error takes place when storing an object in the
   *           cache.
   */
  protected abstract void onPutInCache(Serializable cacheKey,
      CacheProfile cacheProfile, Object objectToCache) throws CacheException;

  /**
   * Removes an entry from the cache.
   * 
   * @param cacheKey
   *          the key the entry to remove is stored under.
   * @param cacheProfile
   *          the cache profile that specifies how to remove the entry from the
   *          cache.
   * @throws CacheException
   *           if an unexpected error takes place when removing an entry from
   *           the cache.
   */
  protected abstract void onRemoveFromCache(Serializable cacheKey,
      CacheProfile cacheProfile) throws CacheException;

  /**
   * @see CacheProviderFacade#putInCache(Serializable, String, Object)
   * @see #makeSerializableIfNecessary(Object)
   */
  public final void putInCache(Serializable cacheKey, String cacheProfileId,
      Object objectToCache) throws CacheException {

    if (logger.isDebugEnabled()) {
      logger.debug("Attempt to store the object <" + objectToCache
          + "> in the cache using key <" + cacheKey + "> and cache profile id "
          + Strings.quote(cacheProfileId));
    }

    try {
      Object newCacheElement = makeSerializableIfNecessary(objectToCache);

      CacheProfile cacheProfile = getCacheProfile(cacheProfileId);
      if (cacheProfile != null) {
        onPutInCache(cacheKey, cacheProfile, newCacheElement);

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
  public final void removeFromCache(Serializable cacheKey, String cacheProfileId)
      throws CacheException {

    if (logger.isDebugEnabled()) {
      logger.debug("Attempt to remove an entry from the cache using key <"
          + cacheKey + "> and cache profile id "
          + Strings.quote(cacheProfileId));
    }

    CacheProfile cacheProfile = getCacheProfile(cacheProfileId);
    if (cacheProfile != null) {
      try {
        onRemoveFromCache(cacheKey, cacheProfile);

        if (logger.isDebugEnabled()) {
          logger.debug("Object removed from the cache");
        }

      } catch (CacheException exception) {
        handleCatchedException(exception);
      }
    }
  }

  public final void setCacheProfiles(Map newCacheProfiles) {
    cacheProfiles = newCacheProfiles;
  }

  /**
   * @throws IllegalCacheProviderStateException
   *           if one or more cache profiles cannot be created from the given
   *           properties.
   */
  private void setCacheProfilesFromProperties(Properties properties)
      throws IllegalCacheProviderStateException {
    Map newCacheProfiles = new HashMap();
    PropertyEditor cacheProfileEditor = getCacheProfileEditor();

    String cacheProfileId = null;
    String cacheProfileAsProperties = null;

    try {
      for (Iterator i = properties.keySet().iterator(); i.hasNext();) {
        cacheProfileId = (String) i.next();
        cacheProfileAsProperties = properties.getProperty(cacheProfileId);

        cacheProfileEditor.setAsText(cacheProfileAsProperties);
        CacheProfile cacheProfile = (CacheProfile) cacheProfileEditor
            .getValue();

        newCacheProfiles.put(cacheProfileId, cacheProfile);
      }
    } catch (RuntimeException exception) {
      throw new IllegalCacheProviderStateException(
          "Unable to create cache profile from the properties "
              + Strings.quote(cacheProfileAsProperties) + " with id "
              + Strings.quote(cacheProfileId), exception);
    }

    setCacheProfiles(newCacheProfiles);
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
   * @throws IllegalCacheProviderStateException
   *           if the cache manager is <code>null</code> or any of its
   *           properties contain invalid values.
   */
  protected abstract void validateCacheManager()
      throws IllegalCacheProviderStateException;

  /**
   * @throws IllegalCacheProviderStateException
   *           if the map of cache profiles is <code>null</code> or empty.
   * @throws IllegalCacheProviderStateException
   *           if one or more cache profiles have invalid values of any of their
   *           properties.
   */
  private void validateCacheProfiles()
      throws IllegalCacheProviderStateException {

    if (cacheProfiles == null || cacheProfiles.isEmpty()) {
      throw new IllegalCacheProviderStateException(
          "The map of cache profiles should not be empty");
    }

    CacheProfileValidator cacheProfileValidator = getCacheProfileValidator();

    String cacheProfileId = null;
    Object cacheProfile = null;

    try {
      for (Iterator i = cacheProfiles.keySet().iterator(); i.hasNext();) {
        cacheProfileId = (String) i.next();
        cacheProfile = cacheProfiles.get(cacheProfileId);

        cacheProfileValidator.validateCacheProfile(cacheProfile);
      }

    } catch (InvalidCacheProfileException exception) {
      throw new IllegalCacheProviderStateException("Invalid cache profile <"
          + cacheProfile + "> with id " + Strings.quote(cacheProfileId),
          exception);
    }
  }
  
  
}