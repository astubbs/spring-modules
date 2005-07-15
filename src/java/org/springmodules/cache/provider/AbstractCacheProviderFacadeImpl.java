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
import org.springmodules.cache.EntryRetrievalException;

/**
 * <p>
 * Template for implementations of <code>{@link CacheProviderFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/07/15 18:02:14 $
 */
public abstract class AbstractCacheProviderFacadeImpl implements
    CacheProviderFacade {

  /**
   * Message logger.
   */
  private static Log logger = LogFactory
      .getLog(AbstractCacheProviderFacadeImpl.class);

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

  /**
   * Constructor.
   */
  public AbstractCacheProviderFacadeImpl() {
    super();
  }

  /**
   * Validates the properties of this class after being set by the BeanFactory.
   * 
   * @throws IllegalStateException
   *           if the cache manager is <code>null</code> or one or more of its
   *           properties contain invalid values.
   * @throws IllegalStateException
   *           if the map of cache profiles is <code>null</code> or empty.
   * @throws IllegalStateException
   *           if one or more cache profiles contain invalid values in any of
   *           their properties.
   */
  public final void afterPropertiesSet() {

    // validate the cache manager.
    this.validateCacheManager();

    // validate the cache profiles.
    if (this.cacheProfiles == null || this.cacheProfiles.isEmpty()) {
      throw new IllegalStateException(
          "The Map of cache profiles should not be empty");
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
        } catch (Exception exception) {
          String message = "Exception thrown while validating cache profile '"
              + cacheProfileId + "': " + exception.getMessage();

          throw new IllegalStateException(message);
        } // end 'catch (Exception exception)'
      } // end 'while (keySetIterator.hasNext())'
    } // end 'if (!this.isCacheProfileMapValidated())'
  }

  /**
   * @see CacheProviderFacade#cancelCacheUpdate(Serializable)
   */
  public final void cancelCacheUpdate(Serializable cacheKey) {
    if (logger.isDebugEnabled()) {
      logger
          .debug("Method 'cancelCacheUpdates(CacheKey)'.  Argument 'cacheKey': "
              + cacheKey);
    }

    try {
      this.onCancelCacheUpdate(cacheKey);
    } catch (RuntimeException exception) {
      this.handleCacheAccessException("Method 'cancelCacheUpdates(CacheKey)'.",
          exception);
    }
  }

  /**
   * @see CacheProviderFacade#flushCache(String[])
   */
  public final void flushCache(String[] cacheProfileIds) {
    if (logger.isDebugEnabled()) {
      String formattedCacheProfileIds = null;
      if (cacheProfileIds != null) {
        int cacheProfileIdCount = cacheProfileIds.length;

        if (cacheProfileIdCount == 0) {
          formattedCacheProfileIds = "{}";
        } else {
          StringBuffer buffer = new StringBuffer();

          for (int i = 0; i < cacheProfileIdCount; i++) {
            if (i == 0) {
              buffer.append("{");
            } else {
              buffer.append(", ");
            }

            String cacheProfileId = cacheProfileIds[i];
            String formattedCacheProfileId = null;
            if (cacheProfileId != null) {
              formattedCacheProfileId = "'" + cacheProfileId + "'";
            }
            buffer.append(formattedCacheProfileId);
          }

          buffer.append("}");
          formattedCacheProfileIds = buffer.toString();
        }
      }

      logger
          .debug("Method 'flushCache(String[])'. Argument 'cacheProfileIds': "
              + formattedCacheProfileIds);
    }

    if (cacheProfileIds != null) {
      int cacheProfileIdCount = cacheProfileIds.length;

      try {
        for (int i = 0; i < cacheProfileIdCount; i++) {
          String cacheProfileId = cacheProfileIds[i];

          CacheProfile cacheProfile = this.getCacheProfile(cacheProfileId);
          if (cacheProfile != null) {
            this.onFlushCache(cacheProfile);
          } // end 'if (cacheProfile != null)'
        } // end 'for (int i = 0; i < cacheProfileIdCount; i++)'
      } // end 'try'
      catch (RuntimeException exception) {
        this.handleCacheAccessException("Method 'flushCache(String[])'.",
            exception);
      } // end 'catch (RuntimeException exception)'
    } // end 'if (cacheProfileIds != null)'

    if (logger.isDebugEnabled()) {
      logger.debug("Method 'flushCache(String[])'. Cache flushed.");
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
    if (logger.isDebugEnabled()) {
      logger
          .debug("Method 'getCacheProfile(String)'. Argument 'cacheProfileId': "
              + cacheProfileId);
    }
    CacheProfile cacheProfile = null;

    if (StringUtils.hasText(cacheProfileId) && this.cacheProfiles != null) {
      cacheProfile = (CacheProfile) this.cacheProfiles.get(cacheProfileId);
    }

    if (logger.isDebugEnabled()) {
      logger
          .debug("Method 'getCacheProfile(String)'. Variable 'cacheProfile': "
              + cacheProfile);
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
      throws EntryRetrievalException {
    if (logger.isDebugEnabled()) {
      String message = "Method 'getFromCache(CacheKey, String)'. Argument 'cacheKey': "
          + cacheKey;
      logger.debug(message);
    }

    Object cachedObject = null;

    try {
      CacheProfile cacheProfile = this.getCacheProfile(cacheProfileId);
      if (cacheProfile != null) {
        cachedObject = this.onGetFromCache(cacheKey, cacheProfile);
      }

      if (logger.isDebugEnabled()) {
        String message = "Method 'getFromCache(CacheKey, String)'. Variable 'cachedObject': "
            + cachedObject;
        logger.debug(message);
      }
    } catch (RuntimeException exception) {
      String message = "Method 'getFromCache(Cachekey, String)'.";
      this.handleCacheAccessException(message, exception);
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
   * @param loggerMessage
   *          the detail message to be used to log the exception.
   * @param exception
   *          the exception thrown while accessing the cache.
   */
  protected final void handleCacheAccessException(String loggerMessage,
      RuntimeException exception) {

    logger.error(loggerMessage, exception);

    if (!this.isFailQuietlyEnabled()) {
      // if the this provider should not "fail quietly", throw the catched
      // exception.
      throw exception;
    }
  }

  /**
   * Getter for field <code>{@link #cacheProfileMapValidated}</code>.
   * 
   * @return the field <code>cacheProfileMapValidated</code>.
   */
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
   */
  protected void onCancelCacheUpdate(Serializable cacheKey) {
    if (logger.isDebugEnabled()) {
      logger
          .debug("Method 'onCancelCacheUpdate(CacheKey)'.  Argument 'cacheKey': "
              + cacheKey);
    }
    // not all subclasses will implement this method.
  }

  /**
   * Flushes the caches specified in the given profile.
   * 
   * @param cacheProfile
   *          the cache profile that specifies what and how to flush.
   */
  protected abstract void onFlushCache(CacheProfile cacheProfile);

  /**
   * Retrieves an entry from the cache.
   * 
   * @param cacheKey
   *          the key under which the entry is stored.
   * @param cacheProfile
   *          the the cache profile that specifies how to retrieve an entry.
   * @return the cached entry.
   * @throws EntryRetrievalException
   *           if an unexpected error takes place when retrieving the entry from
   *           the cache.
   */
  protected abstract Object onGetFromCache(Serializable cacheKey,
      CacheProfile cacheProfile) throws EntryRetrievalException;

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
   */
  protected abstract void onPutInCache(Serializable cacheKey,
      CacheProfile cacheProfile, Object objectToCache);

  /**
   * @see CacheProviderFacade#putInCache(Serializable, String, Object)
   */
  public void putInCache(Serializable cacheKey, String cacheProfileId,
      Object objectToCache) {

    if (logger.isDebugEnabled()) {
      logger
          .debug("Method 'putInCache(CacheKey, String)'. Argument 'cacheKey': "
              + cacheKey);
      logger
          .debug("Method 'putInCache(CacheKey, String)'. Argument 'objectToCache': "
              + objectToCache);
    }

    CacheProfile cacheProfile = this.getCacheProfile(cacheProfileId);
    if (cacheProfile != null) {
      try {
        this.onPutInCache(cacheKey, cacheProfile, objectToCache);
      } catch (RuntimeException exception) {
        String message = "Method 'putInCache(CacheKey, String, Object)'.";
        this.handleCacheAccessException(message, exception);
      }
    }

    if (logger.isDebugEnabled()) {
      logger
          .debug("Method 'putInCache(CacheKey, String)'. Object stored in the cache");
    }
  }

  /**
   * Setter for the field <code>{@link #cacheProfiles}</code>.
   * 
   * @param cacheProfiles
   *          the new value to set.
   */
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

  /**
   * Setter for the field <code>{@link #failQuietlyEnabled}</code>.
   * 
   * @param failQuietlyEnabled
   *          the new value to set.
   */
  public final void setFailQuietlyEnabled(boolean failQuietlyEnabled) {
    this.failQuietlyEnabled = failQuietlyEnabled;
  }

  /**
   * Validates the cache manager used by this facade.
   * 
   * @throws IllegalStateException
   *           if the cache manager is <code>null</code> or one or more of its
   *           properties contain invalid values.
   */
  protected abstract void validateCacheManager();
}