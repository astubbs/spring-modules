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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springmodules.cache.util.ArrayUtils;

/**
 * <p>
 * Template for implementations of <code>{@link CacheProviderFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.6 $ $Date: 2005/08/05 02:18:46 $
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

    if (!this.isCacheProfileMapValidated()) {
      // validate each of the cache profiles.
      CacheProfileValidator cacheProfileValidator = this
          .getCacheProfileValidator();

      Set keySet = this.cacheProfiles.keySet();
      Iterator keySetIterator = keySet.iterator();

      while (keySetIterator.hasNext()) {
        String cacheProfileId = (String) keySetIterator.next();
        Object cacheProfile = this.cacheProfiles.get(cacheProfileId);

        try {
          cacheProfileValidator.validateCacheProfile(cacheProfile);

        } catch (InvalidCacheProfileException exception) {
          String errorMessage = "Invalid cache profile: " + cacheProfile;

          if (this.logger.isErrorEnabled()) {
            this.logger.error(errorMessage, exception);
          }

          throw new InvalidConfigurationException(errorMessage, exception);
        }
      }
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

            if (this.logger.isDebugEnabled()) {
              String logMessage = "Cache profile <" + cacheProfile + ">";
              this.logger.debug(logMessage);
            }

            this.onFlushCache(cacheProfile);
          }
        }

      } catch (CacheException exception) {
        this.handleCacheException(exception);
      }
    }

    if (this.logger.isDebugEnabled()) {
      this.logger.debug("Cache has been flushed.");
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
      String logMessage = "Cache profile <" + cacheProfile + ">";
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

    if (this.logger.isErrorEnabled()) {
      this.logger.error(exception.getMessage(), exception);
    }

    if (!this.isFailQuietlyEnabled()) {
      // if the this provider should not "fail quietly", throw the catched
      // exception.
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
   * Cancels the update being made to the cache.
   * 
   * @param cacheKey
   *          the key being used in the cache update.
   * @throws CacheException
   *           if an unexpected error takes place when attempting to cancel the
   *           update.
   */
  protected abstract void onCancelCacheUpdate(Serializable cacheKey)
      throws CacheException;

  /**
   * Flushes the caches specified in the given profile.
   * 
   * @param cacheProfile
   *          the cache profile that specifies what and how to flush.
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
   */
  public final void putInCache(Serializable cacheKey, String cacheProfileId,
      Object objectToCache) throws CacheException {

    if (this.logger.isDebugEnabled()) {
      String logMessage = "Attempt to store the object <" + objectToCache
          + "> in the cache using key <" + cacheKey
          + "> and cache profile id '" + cacheProfileId + "'";

      this.logger.debug(logMessage);
    }

    CacheProfile cacheProfile = this.getCacheProfile(cacheProfileId);
    if (cacheProfile != null) {
      try {
        this.onPutInCache(cacheKey, cacheProfile, objectToCache);

      } catch (CacheException exception) {
        this.handleCacheException(exception);
      }
    }

    if (this.logger.isDebugEnabled()) {
      this.logger.debug("Object stored in the cache");
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

      } catch (CacheException exception) {
        this.handleCacheException(exception);
      }
    }

    if (this.logger.isDebugEnabled()) {
      this.logger.debug("Object removed from the cache");
    }
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
   * properties.
   * 
   * @param cacheProfiles
   *          the properties of the cache profiles to create.
   * 
   * @throws IllegalArgumentException
   *           if the specified set of properties is <code>null</code> or
   *           empty.
   * @throws IllegalArgumentException
   *           if any exception is thrown when creating the cache profiles.
   */
  public final void setCacheProfiles(Properties cacheProfiles) {

    if (cacheProfiles == null || cacheProfiles.isEmpty()) {
      throw new IllegalArgumentException(
          "The properties of the cache profiles should not be empty");
    }

    Map newCacheProfiles = null;
    Iterator keySetIterator = cacheProfiles.keySet().iterator();

    AbstractCacheProfileEditor cacheProfileEditor = this
        .getCacheProfileEditor();

    newCacheProfiles = new LinkedHashMap();
    while (keySetIterator.hasNext()) {

      String cacheProfileId = (String) keySetIterator.next();
      String cacheProfileProperties = cacheProfiles.getProperty(cacheProfileId);

      CacheProfile cacheProfile = null;
      try {
        cacheProfileEditor.setAsText(cacheProfileProperties);
        cacheProfile = (CacheProfile) cacheProfileEditor.getValue();
      } catch (Exception exception) {
        String message = "Exception thrown while creating the cache profile with id '"
            + cacheProfileId + "': " + exception.getMessage();

        throw new IllegalArgumentException(message);
      }

      newCacheProfiles.put(cacheProfileId, cacheProfile);

    } // end 'while (keyIterator.hasNext())'

    this.cacheProfiles = newCacheProfiles;
    this.cacheProfileMapValidated = true;
  }

  public final void setFailQuietlyEnabled(boolean failQuietlyEnabled) {
    this.failQuietlyEnabled = failQuietlyEnabled;
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