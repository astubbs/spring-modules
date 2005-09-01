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

/**
 * <p>
 * Template for implementations of <code>{@link CacheProviderFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.14 $ $Date: 2005/09/01 03:59:28 $
 */
public abstract class AbstractCacheProviderFacadeImpl implements
    CacheProviderFacade {

  /**
   * Flag that indicates if the cache profiles in
   * <code>{@link #cacheProfiles}</code> were validated or not.
   */
  private boolean cacheProfileMapValidated;

  /**
   * Map that stores implementations of <code>{@link CacheProfile}</code>.
   */
  private Map cacheProfiles;

  /**
   * Flag that indicates if an exception should thrown or not when an error
   * occurrs when accessing the cache provider.
   */
  private boolean failQuietlyEnabled;

  protected final Log logger = LogFactory.getLog(this.getClass());

  private SerializableFactory serializableFactory;

  public AbstractCacheProviderFacadeImpl() {
    super();
  }

  /**
   * Validates the properties of this class after being set by the BeanFactory.
   * 
   * @throws InvalidConfigurationException
   *           if the cache manager is <code>null</code> or one or more of its
   *           properties contain invalid values.
   * @throws InvalidConfigurationException
   *           if the map of cache profiles is <code>null</code> or empty.
   * @throws InvalidConfigurationException
   *           if one or more cache profiles contain invalid values in any of
   *           their properties.
   */
  public final void afterPropertiesSet() throws InvalidConfigurationException {

    // validate the cache manager.
    this.validateCacheManager();

    // validate the cache profiles.
    if (this.cacheProfiles == null || this.cacheProfiles.isEmpty()) {
      throw new InvalidConfigurationException(
          "The map of cache profiles should not be empty");
    }

    if (this.isCacheProfileMapValidated())
      return;

    // validate each of the cache profiles.
    CacheProfileValidator cacheProfileValidator = this
        .getCacheProfileValidator();

    Iterator keySetIterator = this.cacheProfiles.keySet().iterator();

    Object invalidCacheProfile = null;
    InvalidCacheProfileException invalidCacheProfileException = null;

    while (keySetIterator.hasNext()) {
      String cacheProfileId = (String) keySetIterator.next();
      Object cacheProfile = this.cacheProfiles.get(cacheProfileId);

      try {
        cacheProfileValidator.validateCacheProfile(cacheProfile);

      } catch (InvalidCacheProfileException exception) {
        invalidCacheProfileException = exception;
        invalidCacheProfile = cacheProfile;
      }

      if (invalidCacheProfileException != null)
        break;
    }

    if (invalidCacheProfileException != null) {
      String errorMessage = "Invalid cache profile: " + invalidCacheProfile;
      this.logger.error(errorMessage, invalidCacheProfileException);
      throw new InvalidConfigurationException(errorMessage,
          invalidCacheProfileException);
    }
  }

  /**
   * @see CacheProviderFacade#cancelCacheUpdate(Serializable)
   */
  public final void cancelCacheUpdate(Serializable cacheKey)
      throws CacheException {

    if (this.logger.isDebugEnabled()) {
      String logMessage = "Attempt to cancel a cache update using the key <"
          + cacheKey + ">";

      this.logger.debug(logMessage);
    }

    try {
      this.onCancelCacheUpdate(cacheKey);

    } catch (CacheException exception) {
      this.handleCacheException(exception);
    }
  }

  /**
   * @see CacheProviderFacade#flushCache(String[])
   */
  public final void flushCache(String[] cacheProfileIds) throws CacheException {

    if (this.logger.isDebugEnabled()) {
      String formattedCacheProfileIds = ArrayUtils.toString(cacheProfileIds);
      String logMessage = "Attempt to flush the cache using the cache profile ids <"
          + formattedCacheProfileIds + ">";

      this.logger.debug(logMessage);
    }

    if (cacheProfileIds != null) {
      int cacheProfileIdCount = cacheProfileIds.length;

      try {
        for (int i = 0; i < cacheProfileIdCount; i++) {
          String cacheProfileId = cacheProfileIds[i];

          CacheProfile cacheProfile = this.getCacheProfile(cacheProfileId);
          if (cacheProfile != null) {
            this.onFlushCache(cacheProfile);
          }
        }
        if (this.logger.isDebugEnabled()) {
          this.logger.debug("Cache has been flushed.");
        }

      } catch (CacheException exception) {
        this.handleCacheException(exception);
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

    if (StringUtils.hasText(cacheProfileId) && this.cacheProfiles != null) {
      cacheProfile = (CacheProfile) this.cacheProfiles.get(cacheProfileId);
    }

    if (this.logger.isDebugEnabled()) {
      String logMessage = "Obtained cache profile <" + cacheProfile + ">";
      this.logger.debug(logMessage);
    }

    return cacheProfile;
  }

  /**
   * Returns a property editor for <code>{@link CacheProfile}</code>.
   * 
   * @return a property editor for cache profiles.
   */
  protected abstract AbstractCacheProfileEditor getCacheProfileEditor();

  /**
   * Returns an unmodifiable view of <code>{@link #cacheProfiles}</code>.
   * 
   * @return an unmodifiable view of the member variable
   *         <code>cacheProfiles</code>.
   */
  public final Map getCacheProfiles() {
    return Collections.unmodifiableMap(this.cacheProfiles);
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

    if (this.logger.isDebugEnabled()) {
      String logMessage = "Attempt to retrieve a cache entry using key <"
          + cacheKey + "> and cache profile id '" + cacheProfileId + "'";

      this.logger.debug(logMessage);
    }

    Object cachedObject = null;

    try {
      CacheProfile cacheProfile = this.getCacheProfile(cacheProfileId);

      if (cacheProfile != null) {
        cachedObject = this.onGetFromCache(cacheKey, cacheProfile);
      }

      if (this.logger.isDebugEnabled()) {
        String logMessage = "Retrieved cache entry <" + cachedObject + ">";
        this.logger.debug(logMessage);
      }

    } catch (CacheException exception) {
      this.handleCacheException(exception);
    }
    return cachedObject;
  }

  /**
   * Handles the exception thrown while accessing the cache:
   * <ul>
   * <li>Creates a log entry including a detail message and the thrown
   * exception</li>
   * <li>Rethrows the exception if <code>{@link #failQuietlyEnabled}</code>
   * is <code>false</code>.</li>
   * </ul>
   * 
   * @param exception
   *          the exception thrown when trying to access the cache.
   */
  protected final void handleCacheException(CacheException exception)
      throws CacheException {

    this.logger.error(exception.getMessage(), exception);

    if (!this.isFailQuietlyEnabled()) {
      throw exception;
    }
  }

  public final boolean isCacheProfileMapValidated() {
    return this.cacheProfileMapValidated;
  }

  /**
   * @see CacheProviderFacade#isFailQuietlyEnabled()
   */
  public final boolean isFailQuietlyEnabled() {
    return this.failQuietlyEnabled;
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
   * @throws InvalidObjectToCacheException
   *           if the cache requires serializable elements, the given object
   *           does not implement <code>java.io.Serializable</code> and the
   *           factory of serializable objects is <code>null</code>.
   * 
   * @see #setSerializableFactory(SerializableFactory)
   */
  protected final Object makeSerializableIfNecessary(Object obj) {
    if (!this.isSerializableCacheElementRequired()) {
      return obj;
    }
    if (this.serializableFactory != null) {
      return this.serializableFactory.makeSerializableIfNecessary(obj);
    }
    if (obj instanceof Serializable) {
      return obj;
    }
    throw new InvalidObjectToCacheException(
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
    this.logger.info("Cache provider does not support cancelation of updates");
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

    if (this.logger.isDebugEnabled()) {
      String logMessage = "Attempt to store in the cache the object <"
          + objectToCache + ">  using key <" + cacheKey
          + "> and cache profile id '" + cacheProfileId + "'";

      this.logger.debug(logMessage);
    }

    try {
      Object newCacheElement = this.makeSerializableIfNecessary(objectToCache);

      CacheProfile cacheProfile = this.getCacheProfile(cacheProfileId);
      if (cacheProfile != null) {
        this.onPutInCache(cacheKey, cacheProfile, newCacheElement);

        if (this.logger.isDebugEnabled()) {
          this.logger.debug("Object was successfully stored in the cache");
        }
      }
    } catch (CacheException exception) {
      this.handleCacheException(exception);
    }
  }

  /**
   * @see CacheProviderFacade#removeFromCache(Serializable, String)
   */
  public final void removeFromCache(Serializable cacheKey, String cacheProfileId)
      throws CacheException {

    if (this.logger.isDebugEnabled()) {
      String logMessage = "Attempt to remove an entry from the cache using key <"
          + cacheKey + "> and cache profile id '" + cacheProfileId + "'";

      this.logger.debug(logMessage);
    }

    CacheProfile cacheProfile = this.getCacheProfile(cacheProfileId);
    if (cacheProfile != null) {
      try {
        this.onRemoveFromCache(cacheKey, cacheProfile);

        if (this.logger.isDebugEnabled()) {
          this.logger.debug("Object removed from the cache");
        }

      } catch (CacheException exception) {
        this.handleCacheException(exception);
      }
    }
  }

  protected final void setCacheProfileMapValidated(
      boolean cacheProfileMapValidated) {
    this.cacheProfileMapValidated = cacheProfileMapValidated;
  }

  public final void setCacheProfiles(Map cacheProfiles) {
    if (cacheProfiles instanceof Properties) {
      this.setCacheProfiles((Properties) cacheProfiles);
    } else {
      this.cacheProfiles = cacheProfiles;
      this.cacheProfileMapValidated = false;
    }
  }

  /**
   * Sets up <code>{@link #cacheProfiles}</code> by parsing the specified
   * properties (which are validated by the property editor).
   * 
   * @param cacheProfiles
   *          the properties of the cache profiles to create.
   * 
   * @throws IllegalArgumentException
   *           if the specified set of properties is <code>null</code> or
   *           empty.
   * @throws InvalidCacheProfileException
   *           if the properties contain invalid values.
   */
  public final void setCacheProfiles(Properties cacheProfiles) {
    if (cacheProfiles == null || cacheProfiles.isEmpty()) {
      throw new IllegalArgumentException(
          "The properties of the cache profiles should not be empty");
    }

    Map newCacheProfiles = new HashMap();
    Iterator keySetIterator = cacheProfiles.keySet().iterator();

    AbstractCacheProfileEditor cacheProfileEditor = this
        .getCacheProfileEditor();

    while (keySetIterator.hasNext()) {
      String cacheProfileId = (String) keySetIterator.next();
      String cacheProfileProperties = cacheProfiles.getProperty(cacheProfileId);

      cacheProfileEditor.setAsText(cacheProfileProperties);
      CacheProfile cacheProfile = (CacheProfile) cacheProfileEditor.getValue();
      newCacheProfiles.put(cacheProfileId, cacheProfile);
    }

    this.cacheProfiles = newCacheProfiles;
    this.setCacheProfileMapValidated(true);
  }

  public final void setFailQuietlyEnabled(boolean failQuietlyEnabled) {
    this.failQuietlyEnabled = failQuietlyEnabled;
  }

  /**
   * Sets the factory that makes serializable the objects to be stored in the
   * cache (if the cache requires serializable elements).
   */
  public final void setSerializableFactory(
      SerializableFactory serializableFactory) {
    this.serializableFactory = serializableFactory;
  }

  /**
   * Validates the cache manager used by this facade.
   * 
   * @throws InvalidConfigurationException
   *           if the cache manager is <code>null</code> or one or more of its
   *           properties contain invalid values.
   */
  protected abstract void validateCacheManager()
      throws InvalidConfigurationException;
}