/*
 * Created on May 3, 2005
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
 * Copyright @2007 the original author or authors.
 */
package org.springmodules.cache.provider.ehcache;

import junit.framework.TestCase;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.easymock.AbstractMatcher;
import org.easymock.classextension.MockClassControl;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.util.ObjectUtils;
import org.springmodules.cache.CacheException;
import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.CacheNotFoundException;
import org.springmodules.cache.provider.ReflectionCacheModelEditor;

import java.beans.PropertyEditor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Unit Tests for <code>{@link EhCacheFacade}</code>.
 *
 * @author Omar Irbouh
 * @author Alex Ruiz
 */
public class EhCacheFacadeTests extends TestCase {

	protected class ElementMatcher extends AbstractMatcher {
		/**
		 * @see AbstractMatcher#argumentMatches(Object,Object)
		 */
		protected boolean argumentMatches(Object expected, Object actual) {
			if (!(expected instanceof Element)) {
				throw new IllegalArgumentException(
						"Element matcher only evaluates instances of <"
								+ Element.class.getName() + ">");
			}
			if (!(actual instanceof Element)) {
				return false;
			}
			return equals((Element) expected, (Element) actual);
		}

		private boolean equals(Element expected, Element actual) {
			if (expected == actual) {
				return true;
			}
			if (!ObjectUtils.nullSafeEquals(expected.getKey(), actual.getKey())) {
				return false;
			}
			if (!ObjectUtils.nullSafeEquals(expected.getValue(), actual.getValue())) {
				return false;
			}
			return true;
		}

	}

	private static final String CACHE_NAME = "testCache";

	private static final String KEY = "key";

	private Cache cache;

	private MockClassControl cacheControl;

	private EhCacheFacade cacheFacade;

	private CacheManager cacheManager;

	private EhCacheCachingModel cachingModel;

	private EhCacheFlushingModel flushingModel;

	public EhCacheFacadeTests(String name) {
		super(name);
	}

	/**
	 * Verifies that the method
	 * <code>{@link EhCacheFacade#modelValidator()}</code> returns an an
	 * instance of <code>{@link EhCacheModelValidator}</code> not equal to
	 * <code>null</code>.
	 */
	public void testGetCacheModelValidator() {
		CacheModelValidator validator = cacheFacade.modelValidator();
		assertNotNull(validator);
		assertEquals(EhCacheModelValidator.class, validator.getClass());
	}

	public void testGetCacheWhenCacheAccessThrowsException() {
		setUpCache();

		// we can't mock the cache manager since it doesn't have a public
		// constructor.
		// force a NullPointerException.
		cacheFacade.setCacheManager(null);

		try {
			cacheFacade.getCache(CACHE_NAME);
			fail();

		} catch (CacheAccessException exception) {
			Throwable cause = exception.getCause();
			assertNotNull(cause);
			assertTrue(cause instanceof NullPointerException);
		}
	}

	public void testGetCacheWithExistingCache() {
		setUpCache();
		assertSame(cacheManager.getCache(CACHE_NAME), cacheFacade
				.getCache(CACHE_NAME));
	}

	public void testGetCacheWithNotExistingCache() {
		setUpCache();
		try {
			cacheFacade.getCache("AnotherCache");
			fail();
		} catch (CacheNotFoundException exception) {
			// we are expecting this exception.
		}
	}

	public void testGetCachingModelEditor() {
		PropertyEditor editor = cacheFacade.getCachingModelEditor();

		assertNotNull(editor);
		assertEquals(ReflectionCacheModelEditor.class, editor.getClass());

		ReflectionCacheModelEditor modelEditor = (ReflectionCacheModelEditor) editor;
		assertEquals(EhCacheCachingModel.class, modelEditor.getCacheModelClass());
		assertNull(modelEditor.getCacheModelPropertyEditors());
	}

	public void testGetFlushingModelEditor() {
		PropertyEditor editor = cacheFacade.getFlushingModelEditor();

		assertNotNull(editor);
		assertEquals(ReflectionCacheModelEditor.class, editor.getClass());

		ReflectionCacheModelEditor modelEditor = (ReflectionCacheModelEditor) editor;
		assertEquals(EhCacheFlushingModel.class, modelEditor.getCacheModelClass());
		Map propertyEditors = modelEditor.getCacheModelPropertyEditors();
		assertEquals(1, propertyEditors.size());
		assertSame(StringArrayPropertyEditor.class, propertyEditors.get("cacheNames").getClass());
	}

	public void testIsSerializableCacheElementRequired() {
		assertTrue(cacheFacade.isSerializableCacheElementRequired());
	}

	/**
	 * Verifies that the method
	 * <code>{@link EhCacheFacade#onFlushCache(org.springmodules.cache.FlushingModel)}</code>
	 * flushes the cache specified in the given cache model.
	 */
	public void testOnFlushCache() throws Exception {
		setUpCache();
		cache.put(new Element(KEY, "A Value"));
		cacheFacade.onFlushCache(flushingModel);
		Object cachedValue = cache.get(KEY);
		assertNull("The cache '" + CACHE_NAME + "' should be empty", cachedValue);
	}

	public void testOnFlushCacheWhenCacheAccessThrowsIllegalStateException()
			throws Exception {
		Method removeAll = getRemoveAllMethodFromCache();
		setUpCacheAsMockObject(removeAll);

		IllegalStateException expected = new IllegalStateException();
		cache.removeAll();
		cacheControl.setThrowable(expected);
		cacheControl.replay();

		try {
			cacheFacade.onFlushCache(flushingModel);
			fail();
		} catch (CacheAccessException exception) {
			assertSame(expected, exception.getCause());
		}
		cacheControl.verify();
	}

	public void testOnFlushCacheWhenCacheIsNotFound() {
		setUpCache();
		cache.put(new Element(KEY, "A Value"));
		flushingModel.setCacheNames("NonExistingCache");

		try {
			cacheFacade.onFlushCache(flushingModel);
			fail();
		} catch (CacheNotFoundException exception) {
			// expecting this exception.
		}
	}

	public void testOnFlushCacheWithModelNotHavingCacheNames() throws Exception {
		Method removeAll = getRemoveAllMethodFromCache();
		setUpCacheAsMockObject(removeAll);

		cacheControl.replay();

		flushingModel.setCacheNames((String[]) null);
		cacheFacade.onFlushCache(flushingModel);

		cacheControl.verify();
	}

	/**
	 * Verifies that the method
	 * <code>{@link EhCacheFacade#onGetFromCache(java.io.Serializable,org.springmodules.cache.CachingModel)}</code>
	 * retrieves, from the cache specified in the given cache model, the entry
	 * stored under the given key.
	 */
	public void testOnGetFromCache() throws Exception {
		setUpCache();

		String expected = "An Object";
		cache.put(new Element(KEY, expected));

		Object cachedObject = cacheFacade.onGetFromCache(KEY, cachingModel);
		assertEquals(expected, cachedObject);
	}

	// DISABLED
	public void tstOnGetFromCacheWhenCacheAccessThrowsCacheException()
			throws Exception {
		assertOnGetFromCacheWrapsCatchedException(new net.sf.ehcache.CacheException());
	}

	// DISABLED
	public void tstOnGetFromCacheWhenCacheAccessThrowsIllegalStateException()
			throws Exception {
		assertOnGetFromCacheWrapsCatchedException(new IllegalStateException());
	}

	public void testOnGetFromCacheWhenCacheIsNotFound() {
		setUpCache();
		cachingModel.setCacheName("NonExistingCache");
		try {
			cacheFacade.onGetFromCache(KEY, cachingModel);
			fail();
		} catch (CacheNotFoundException exception) {
			// we are expecting this exception.
		}
	}

	/**
	 * Verifies that the method
	 * <code>{@link EhCacheFacade#onGetFromCache(java.io.Serializable,org.springmodules.cache.CachingModel)}</code>
	 * returns <code>null</code> if the specified key does not exist in the
	 * cache.
	 */
	public void testOnGetFromCacheWhenKeyIsNotFound() throws Exception {
		setUpCache();
		Object cachedObject = cacheFacade.onGetFromCache("NonExistingKey",
				cachingModel);
		assertNull(cachedObject);
	}

	/**
	 * Verifies that the method
	 * <code>{@link EhCacheFacade#onPutInCache(java.io.Serializable,org.springmodules.cache.CachingModel,Object)}</code>
	 * stores an entry in the cache specified in the given cache model using the
	 * given key.
	 */
	public void testOnPutInCache() throws Exception {
		setUpCache();
		String expected = "An Object";
		cacheFacade.onPutInCache(KEY, cachingModel, expected);

		Object cachedObject = cache.get(KEY).getValue();
		assertSame(expected, cachedObject);
	}

	// DISABLED
	public void tstOnPutInCacheWhenCacheAccessThrowsIllegalStateException()
			throws Exception {
		Method put = Cache.class.getMethod("put", new Class[]{Element.class});
		setUpCacheAsMockObject(put);

		IllegalStateException expected = new IllegalStateException();
		String objectToCache = "Luke";
		Element element = new Element(KEY, objectToCache);

		cache.put(element);
		cacheControl.setMatcher(new ElementMatcher());
		cacheControl.setThrowable(expected);
		cacheControl.replay();

		try {
			cacheFacade.onPutInCache(KEY, cachingModel, objectToCache);
			fail();
		} catch (CacheAccessException exception) {
			assertSame(expected, exception.getCause());
		}
		cacheControl.verify();
	}

	/**
	 * Verifies that the method
	 * <code>{@link EhCacheFacade#onPutInCache(java.io.Serializable,org.springmodules.cache.CachingModel,Object)}</code>
	 * does not store any entry in any cache if the cache specified in the given
	 * cache model does not exist.
	 */
	public void testOnPutInCacheWhenCacheIsNotFound() throws Exception {
		setUpCache();
		cachingModel.setCacheName("NonExistingCache");
		try {
			cacheFacade.onPutInCache(KEY, cachingModel, "An Object");
			fail();
		} catch (CacheException exception) {
			assertCacheExceptionIsCacheNotFoundException(exception);
		}
	}

	public void testOnRemoveFromCache() throws Exception {
		setUpCache();
		cache.put(new Element(KEY, "An Object"));
		cacheFacade.onRemoveFromCache(KEY, cachingModel);

		assertNull("The element with key '" + KEY
				+ "' should have been removed from the cache", cache.get(KEY));
	}

	// DISABLED
	public void tstOnRemoveFromCacheWhenCacheAccessThrowsIllegalStateException()
			throws Exception {
		Method removeMethod = Cache.class.getDeclaredMethod("remove",
				new Class[]{Serializable.class});
		setUpCacheAsMockObject(removeMethod);

		IllegalStateException expected = new IllegalStateException();
		cache.remove(KEY);
		cacheControl.setThrowable(expected);
		cacheControl.replay();

		try {
			cacheFacade.onRemoveFromCache(KEY, cachingModel);
			fail();
		} catch (CacheAccessException exception) {
			assertSame(expected, exception.getCause());
		}
		cacheControl.verify();
	}

	public void testOnRemoveFromCacheWhenCacheIsNotFound() throws Exception {
		setUpCache();
		cache.put(new Element(KEY, "An Object"));
		cachingModel.setCacheName("NonExistingCache");
		try {
			cacheFacade.onRemoveFromCache(KEY, cachingModel);
			fail();

		} catch (CacheException exception) {
			assertCacheExceptionIsCacheNotFoundException(exception);
		}
	}

	public void testValidateCacheManagerWithCacheManagerEqualToNull() {
		cacheFacade.setCacheManager(null);
		try {
			cacheFacade.validateCacheManager();
			fail();
		} catch (FatalCacheException exception) {
			// we are expecting this exception.
		}
	}

	/**
	 * Verifies that the method
	 * <code>{@link EhCacheFacade#validateCacheManager()}</code> does not throw
	 * any exception if the cache manager is not <code>null</code>.
	 */
	public void testValidateCacheManagerWithCacheManagerNotEqualToNull()
			throws Exception {
		setUpCache();
		cacheFacade.validateCacheManager();
	}

	protected void setUp() throws Exception {
		cacheManager = CacheManager.create();

		cachingModel = new EhCacheCachingModel();
		cachingModel.setCacheName(CACHE_NAME);

		flushingModel = new EhCacheFlushingModel();
		flushingModel.setCacheNames(CACHE_NAME);

		cacheFacade = new EhCacheFacade();
	}

	protected void tearDown() {
		cacheManager.shutdown();
	}

	private void assertCacheExceptionIsCacheNotFoundException(
			CacheException exception) {
		assertEquals(CacheNotFoundException.class, exception.getClass());
	}

	private void assertOnGetFromCacheWrapsCatchedException(
			Exception expectedCatchedException) throws Exception {
		Method get = Cache.class.getDeclaredMethod("get",
				new Class[]{Serializable.class});
		setUpCacheAsMockObject(get);

		cacheControl.reset();
		cache.get(KEY);
		cacheControl.setThrowable(expectedCatchedException);

		cacheControl.replay();

		try {
			cacheFacade.onGetFromCache(KEY, cachingModel);
			fail();

		} catch (CacheAccessException cacheAccessException) {
			assertSame(expectedCatchedException, cacheAccessException.getCause());
		}

		cacheControl.verify();
	}

	private Method getRemoveAllMethodFromCache() throws Exception {
		return Cache.class.getMethod("removeAll", new Class[0]);
	}

	private void setUpCache() {
		cache = cacheManager.getCache(CACHE_NAME);
		cacheFacade.setCacheManager(cacheManager);
	}

	private void setUpCacheAsMockObject(Method methodToMock) throws Exception {
		setUpCacheAsMockObject(new Method[]{methodToMock});
	}

	private void setUpCacheAsMockObject(Method[] methodsToMock) throws Exception {
		Class[] constructorTypes = new Class[]{String.class, int.class,
				boolean.class, boolean.class, long.class, long.class};

		Object[] constructorArgs = new Object[]{CACHE_NAME, new Integer(10),
				new Boolean(false), new Boolean(false), new Long(300), new Long(600)};

		Class classToMock = Cache.class;

		cacheControl = MockClassControl.createControl(classToMock,
				constructorTypes, constructorArgs, methodsToMock);

		cache = (Cache) cacheControl.getMock();

//    Field field = classToMock.getDeclaredField("status");
//    field.setAccessible(true);
//    field.set(cache, Status.STATUS_UNINITIALISED);


		cacheFacade.setCacheManager(cacheManager);

		cacheManager.removeCache(CACHE_NAME);
		cacheManager.addCache(cache);
	}
}
