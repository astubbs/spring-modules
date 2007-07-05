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
 * Copyright @2007 the original author or authors.
 */
package org.springmodules.cache.provider;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.springmodules.cache.CacheException;
import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.mock.MockCachingModel;
import org.springmodules.cache.mock.MockFlushingModel;
import org.springmodules.cache.serializable.SerializableFactory;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * Unit Tests for <code>{@link AbstractCacheProviderFacade}</code>.
 *
 * @author Omar Irbouh
 * @author Alex Ruiz
 */
public final class CacheProviderFacadeTests extends TestCase {

	private AbstractCacheProviderFacade cacheProviderFacade;

	private MockControl cacheProviderFacadeControl;

	private CachingModel cachingModel;

	private FlushingModel flushingModel;

	/**
	 * Key of the cached object to be retrieved from
	 * <code>{@link #cacheProviderFacade}</code>.
	 */
	private Serializable key;

	public CacheProviderFacadeTests(String name) {
		super(name);
	}

	public void testAfterPropertiesSet() throws Exception {
		cacheProviderFacade.validateCacheManager();
		cacheProviderFacade.onAfterPropertiesSet();
		cacheProviderFacadeControl.replay();

		cacheProviderFacade.afterPropertiesSet();
		cacheProviderFacadeControl.verify();
	}

	public void testAssertCacheManagerIsNotNullWithCacheManagerEqualToNull() {
		try {
			cacheProviderFacade.assertCacheManagerIsNotNull(null);
			fail();
		} catch (FatalCacheException exception) {
			// we are expecting this exception.
		}
	}

	public void testAssertCacheManagerIsNotNullWithCacheManagerNotEqualToNull() {
		// shouldn't throw an exception
		cacheProviderFacade.assertCacheManagerIsNotNull(new Object());
	}

	public void testCancelCacheUpdate() throws Exception {
		cacheProviderFacade.onCancelCacheUpdate(key);
		cacheProviderFacadeControl.replay();

		cacheProviderFacade.cancelCacheUpdate(key);
		cacheProviderFacadeControl.verify();
	}

	public void testCancelCacheUpdateWhenCacheAccessThrowsExceptionAndFailQuietlyIsFalse()
			throws Exception {
		cacheProviderFacade.setFailQuietlyEnabled(false);

		CacheException expected = expectOnCancelCacheUpdateToThrowException();

		cacheProviderFacadeControl.replay();

		try {
			cacheProviderFacade.cancelCacheUpdate(key);
			fail();

		} catch (CacheException exception) {
			assertSame(expected, exception);
		}

		cacheProviderFacadeControl.verify();
	}

	public void testCancelCacheUpdateWhenCacheAccessThrowsExceptionAndFailQuietlyIsTrue()
			throws Exception {
		cacheProviderFacade.setFailQuietlyEnabled(true);

		expectOnCancelCacheUpdateToThrowException();

		cacheProviderFacadeControl.replay();

		// shouldn't throw exceptions.
		cacheProviderFacade.cancelCacheUpdate(key);
		cacheProviderFacadeControl.verify();
	}

	public void testFlushCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsFalse()
			throws Exception {
		cacheProviderFacade.setFailQuietlyEnabled(false);
		CacheException expected = expectOnFlushCacheToThrowException();
		cacheProviderFacadeControl.replay();

		try {
			cacheProviderFacade.flushCache(flushingModel);
			fail();

		} catch (CacheException exception) {
			assertSame(expected, exception);
		}

		cacheProviderFacadeControl.verify();
	}

	public void testFlushCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsTrue()
			throws Exception {
		cacheProviderFacade.setFailQuietlyEnabled(true);
		expectOnFlushCacheToThrowException();
		cacheProviderFacadeControl.replay();

		cacheProviderFacade.flushCache(flushingModel);
		cacheProviderFacadeControl.verify();
	}

	/**
	 * Verifies that the method
	 * <code>{@link AbstractCacheProviderFacade#flushCache(FlushingModel)}</code>
	 * does not flush the cache if the model is <code>null</code>.
	 */
	public void testFlushCacheWhenModelIsNull() throws Exception {
		cacheProviderFacadeControl.replay();
		cacheProviderFacade.flushCache(null);
		cacheProviderFacadeControl.verify();
	}

	public void testGetFromCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsFalse()
			throws Exception {
		cacheProviderFacade.setFailQuietlyEnabled(false);
		CacheException expectedException = expectOnGetFromCacheToThrowException();
		cacheProviderFacadeControl.replay();

		try {
			cacheProviderFacade.getFromCache(key, cachingModel);
			fail();

		} catch (CacheException exception) {
			assertSame(expectedException, exception);
		}

		cacheProviderFacadeControl.verify();
	}

	public void testGetFromCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsTrue()
			throws Exception {
		cacheProviderFacade.setFailQuietlyEnabled(true);
		expectOnGetFromCacheToThrowException();
		cacheProviderFacadeControl.replay();

		Object cachedObject = cacheProviderFacade.getFromCache(key, cachingModel);
		assertNull(cachedObject);
		cacheProviderFacadeControl.verify();
	}

	/**
	 * Verifies that the method
	 * <code>{@link AbstractCacheProviderFacade#getFromCache(Serializable,CachingModel)}</code>
	 * does not try to access the cache if the model is <code>null</code>.
	 */
	public void testGetFromCacheWhenModelIsNull() throws Exception {
		cacheProviderFacadeControl.replay();
		Object cachedObject = cacheProviderFacade.getFromCache(key, null);
		assertNull(cachedObject);
		cacheProviderFacadeControl.verify();
	}

	public void testHandleCacheAccessExceptionWhenFailedQuietlyIsFalse() {
		cacheProviderFacade.setFailQuietlyEnabled(false);
		CacheException expectedException = createCacheException();

		try {
			cacheProviderFacade.handleCatchedException(expectedException);
			fail();

		} catch (CacheException exception) {
			assertSame(expectedException, exception);
		}
	}

	public void testHandleCacheAccessExceptionWhenFailedQuietlyIsTrue() {
		cacheProviderFacade.setFailQuietlyEnabled(true);
		CacheException cacheException = createCacheException();

		// should not throw exceptions.
		cacheProviderFacade.handleCatchedException(cacheException);
	}

	/**
	 * Verifies that the method
	 * <code>{@link AbstractCacheProviderFacade#makeSerializableIfNecessary(Object)}</code>
	 * does not make the given non-serializable object serializable if the cache
	 * does not require serializable entries.
	 */
	public void testMakeSerializableIfNecessaryWhenSerializableIsNotRequiredAndEntryIsNotSerializable() {
		cacheProviderFacadeControl.expectAndReturn(cacheProviderFacade
				.isSerializableCacheElementRequired(), false);

		Object objectToStore = new Socket();
		assertIsNotSerializable(objectToStore);

		assertSame(objectToStore, cacheProviderFacade
				.makeSerializableIfNecessary(objectToStore));
	}

	public void testMakeSerializableIfNecessaryWhenSerializableIsRequiredAndSerializableFactoryIsNotNullAndEntryIsNotSerializable() {
		MockControl factoryControl = MockControl
				.createControl(SerializableFactory.class);
		SerializableFactory factory = (SerializableFactory) factoryControl
				.getMock();
		cacheProviderFacade.setSerializableFactory(factory);

		cacheProviderFacadeControl.expectAndReturn(cacheProviderFacade
				.isSerializableCacheElementRequired(), true);
		cacheProviderFacadeControl.replay();

		Serializable expected = "Leia";
		Object objectToStore = new Object();
		factoryControl.expectAndReturn(factory
				.makeSerializableIfNecessary(objectToStore), expected);
		factoryControl.replay();

		Object actual = cacheProviderFacade
				.makeSerializableIfNecessary(objectToStore);
		assertSame(expected, actual);

		cacheProviderFacadeControl.verify();
		factoryControl.verify();
	}

	public void testMakeSerializableIfNecessaryWhenSerializableIsRequiredAndSerializableFactoryIsNotNullAndEntryIsSerializable() {
		cacheProviderFacadeControl.expectAndReturn(cacheProviderFacade
				.isSerializableCacheElementRequired(), true);
		cacheProviderFacadeControl.replay();

		Object objectToStore = "Luke Skywalker";
		assertSame(objectToStore, cacheProviderFacade
				.makeSerializableIfNecessary(objectToStore));

		cacheProviderFacadeControl.verify();
	}

	/**
	 * Verifies that the method
	 * <code>{@link AbstractCacheProviderFacade#makeSerializableIfNecessary(Object)}</code>
	 * throws a <code>{@link ObjectCannotBeCachedException}</code> if:
	 * <ul>
	 * <li>the cache requires serializable entries</li>
	 * <li>the serializable factory of the facade is <code>null</code></li>
	 * <li>the given object is not serializable</li>
	 * </ul>
	 */
	public void testMakeSerializableIfNecessaryWhenSerializableIsRequiredAndSerializableFactoryIsNullAndEntryIsNotSerializable() {
		cacheProviderFacadeControl.expectAndReturn(cacheProviderFacade
				.isSerializableCacheElementRequired(), true);
		cacheProviderFacadeControl.replay();

		Object objectToStore = new Socket();
		assertIsNotSerializable(objectToStore);

		try {
			cacheProviderFacade.makeSerializableIfNecessary(objectToStore);
			fail();

		} catch (ObjectCannotBeCachedException exception) {
			// we are expecting this exception.
		}

		cacheProviderFacadeControl.verify();
	}

	public void testMakeSerializableIfNecessaryWhenSerializableIsRequiredAndSerializableFactoryIsNullAndEntryIsSerializable() {
		cacheProviderFacadeControl.expectAndReturn(cacheProviderFacade
				.isSerializableCacheElementRequired(), true);
		cacheProviderFacadeControl.replay();

		Object objectToStore = "R2-D2";
		assertSame(objectToStore, cacheProviderFacade
				.makeSerializableIfNecessary(objectToStore));

		cacheProviderFacadeControl.verify();
	}

	public void testPutInCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsFalse()
			throws Exception {
		cacheProviderFacade.setFailQuietlyEnabled(false);
		Object objectToStore = new Object();

		cacheProviderFacadeControl.expectAndReturn(cacheProviderFacade
				.isSerializableCacheElementRequired(), false);
		CacheException expectedException = expectOnPutInCacheThrowsException(objectToStore);
		cacheProviderFacadeControl.replay();

		try {
			cacheProviderFacade.putInCache(key, cachingModel, objectToStore);
			fail();

		} catch (CacheException exception) {
			assertSame(expectedException, exception);
		}

		cacheProviderFacadeControl.verify();
	}

	public void testPutInCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsTrue()
			throws Exception {
		Object objectToStore = new Object();
		cacheProviderFacade.setFailQuietlyEnabled(true);

		cacheProviderFacadeControl.expectAndReturn(cacheProviderFacade
				.isSerializableCacheElementRequired(), false);
		expectOnPutInCacheThrowsException(objectToStore);
		cacheProviderFacadeControl.replay();

		cacheProviderFacade.putInCache(key, cachingModel, objectToStore);

		cacheProviderFacadeControl.verify();
	}

	public void testPutInCacheWhenMakeSerializableThrowsExceptionAndFailQuietlyIsFalse() {
		cacheProviderFacadeControl.expectAndReturn(cacheProviderFacade
				.isSerializableCacheElementRequired(), true);
		cacheProviderFacade.setFailQuietlyEnabled(false);

		Object objectToStore = new Object();
		assertIsNotSerializable(objectToStore);

		cacheProviderFacadeControl.replay();

		try {
			cacheProviderFacade.putInCache(key, cachingModel, objectToStore);
			fail();

		} catch (ObjectCannotBeCachedException exception) {
			// expecting exception.
		}

		cacheProviderFacadeControl.verify();
	}

	public void testPutInCacheWhenMakeSerializableThrowsExceptionAndFailQuietlyIsTrue() {
		cacheProviderFacade.setFailQuietlyEnabled(true);
		Object objectToStore = new Object();
		assertIsNotSerializable(objectToStore);

		cacheProviderFacadeControl.expectAndReturn(cacheProviderFacade
				.isSerializableCacheElementRequired(), true);
		cacheProviderFacadeControl.replay();

		cacheProviderFacade.putInCache(key, cachingModel, objectToStore);
		cacheProviderFacadeControl.verify();
	}

	/**
	 * Verifies that the method
	 * <code>{@link AbstractCacheProviderFacade#putInCache(Serializable,CachingModel,Object)}</code>.
	 * does not try to access the cache if the model is <code>null</code>.
	 */
	public void testPutInCacheWhenModelIsNull() throws Exception {
		cacheProviderFacadeControl.expectAndReturn(cacheProviderFacade
				.isSerializableCacheElementRequired(), false);
		cacheProviderFacadeControl.replay();
		cacheProviderFacade.putInCache(key, null, new Object());
		cacheProviderFacadeControl.verify();
	}

	public void testRemoveFromCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsFalse()
			throws Exception {
		cacheProviderFacade.setFailQuietlyEnabled(false);

		CacheException expectedException = expectOnRemoveFromCacheThrowsException();
		cacheProviderFacadeControl.replay();

		try {
			cacheProviderFacade.removeFromCache(key, cachingModel);
			fail();

		} catch (CacheException exception) {
			assertSame(expectedException, exception);
		}

		cacheProviderFacadeControl.verify();
	}

	public void testRemoveFromCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsTrue()
			throws Exception {
		cacheProviderFacade.setFailQuietlyEnabled(true);

		expectOnRemoveFromCacheThrowsException();
		cacheProviderFacadeControl.replay();

		cacheProviderFacade.removeFromCache(key, cachingModel);
		cacheProviderFacadeControl.verify();
	}

	/**
	 * Verifies that the method
	 * <code>{@link AbstractCacheProviderFacade#removeFromCache(Serializable,CachingModel)}</code>.
	 * does not try to access the cache if the model is <code>null</code>.
	 */
	public void testRemoveFromCacheWhenModelIsNull() throws Exception {
		cacheProviderFacadeControl.replay();
		cacheProviderFacade.removeFromCache(key, null);
		cacheProviderFacadeControl.verify();
	}

	protected void setUp() throws Exception {
		setUpCacheProviderFacadeAsMockObject();

		key = "Key";
		cachingModel = new MockCachingModel();
		flushingModel = new MockFlushingModel();
	}

	private void assertIsNotSerializable(Object obj) {
		assertFalse(obj.getClass().getName() + " should not be serializable",
				obj instanceof Serializable);
	}

	private CacheException createCacheException() {
		return new CacheNotFoundException("someCache");
	}

	private CacheException expectOnCancelCacheUpdateToThrowException() {
		CacheException expected = createCacheException();
		cacheProviderFacade.onCancelCacheUpdate(key);
		cacheProviderFacadeControl.setThrowable(expected);

		return expected;
	}

	private CacheException expectOnFlushCacheToThrowException() {
		CacheException expected = createCacheException();
		cacheProviderFacade.onFlushCache(flushingModel);
		cacheProviderFacadeControl.setThrowable(expected);

		return expected;
	}

	private CacheException expectOnGetFromCacheToThrowException() {
		CacheException expected = createCacheException();
		cacheProviderFacade.onGetFromCache(key, cachingModel);
		cacheProviderFacadeControl.setThrowable(expected);

		return expected;
	}

	private CacheException expectOnPutInCacheThrowsException(Object objectToStore) {
		CacheException expected = createCacheException();
		cacheProviderFacade.onPutInCache(key, cachingModel, objectToStore);
		cacheProviderFacadeControl.setThrowable(expected);
		return expected;
	}

	private CacheException expectOnRemoveFromCacheThrowsException() {
		CacheException expected = createCacheException();
		cacheProviderFacade.onRemoveFromCache(key, cachingModel);
		cacheProviderFacadeControl.setThrowable(expected);
		return expected;
	}

	private void setUpCacheProviderFacadeAsMockObject() throws Exception {
		Class classToMock = AbstractCacheProviderFacade.class;

		Method isSerializableCacheElementRequired = classToMock.getDeclaredMethod(
				"isSerializableCacheElementRequired", new Class[0]);

		Method onAfterPropertiesSet = classToMock.getDeclaredMethod(
				"onAfterPropertiesSet", new Class[0]);

		Method onCancelCacheUpdate = classToMock.getDeclaredMethod(
				"onCancelCacheUpdate", new Class[]{Serializable.class});

		Method onFlushCache = classToMock.getDeclaredMethod("onFlushCache",
				new Class[]{FlushingModel.class});

		Method onGetFromCache = classToMock.getDeclaredMethod("onGetFromCache",
				new Class[]{Serializable.class, CachingModel.class});

		Method onPutInCache = classToMock.getDeclaredMethod("onPutInCache",
				new Class[]{Serializable.class, CachingModel.class, Object.class});

		Method onRemoveFromCache = classToMock.getDeclaredMethod(
				"onRemoveFromCache", new Class[]{Serializable.class,
				CachingModel.class});

		Method validateCacheManager = classToMock.getDeclaredMethod(
				"validateCacheManager", new Class[0]);

		Method[] methodsToMock = new Method[]{isSerializableCacheElementRequired,
				onAfterPropertiesSet, onCancelCacheUpdate, onFlushCache,
				onGetFromCache, onPutInCache, onRemoveFromCache, validateCacheManager};

		cacheProviderFacadeControl = MockClassControl.createStrictControl(
				classToMock, null, null, methodsToMock);

//		cacheProviderFacadeControl = MockClassControl.createNiceControl(classToMock, methodsToMock);
		cacheProviderFacade = (AbstractCacheProviderFacade) cacheProviderFacadeControl
				.getMock();
//
		// initialize logger (avoid EasyMock ClassExt issue)
//		Field logger = classToMock.getDeclaredField("logger");
//		logger.setAccessible(true);
// 		logger.set(cacheProviderFacade, LogFactory.getLog(classToMock));
	}

}