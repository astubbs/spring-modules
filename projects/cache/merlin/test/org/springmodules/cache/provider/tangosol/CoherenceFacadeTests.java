/*
 * Created on Jan 26, 2006
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
package org.springmodules.cache.provider.tangosol;

import com.tangosol.net.NamedCache;
import junit.framework.TestCase;
import org.springframework.util.StringUtils;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.ReflectionCacheModelEditor;
import org.springmodules.cache.util.TangosolUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyEditor;

/**
 * <p/>
 * Unit Tests for <code>{@link CoherenceFacade}</code>.
 * <p/>
 * <Strong>Note:</Strong> This class requires
 * <a href="http://www.tangosol.com">Tangosol Coherence</a> jars in the
 * classpath to work.
 * </p>
 *
 * @author Omar Irbouh
 * @author Alex Ruiz
 */
public class CoherenceFacadeTests extends TestCase {

	private static final String CACHE_NAME = "VirtualCache";

	private static final String ENTRY_KEY = "KEY";

	private static final String ENTRY_VALUE = "VALUE";

	private NamedCache cache;

	private CoherenceFacade facade;

	protected final Log logger = LogFactory.getLog(getClass());

	// check whether required Tangosol API is on classpath
	public void runBare() throws Throwable {
		if (TangosolUtils.isApiPresent()) {
			super.runBare();
		} else {
			logger.info("Unable to run tests. Tangosol API is missing.");
		}
	}

	/**
	 * Verifies that the method
	 * <code>{@link CoherenceFacade#modelValidator()}</code> returns an
	 * an instance of <code>{@link CoherenceModelValidator}</code> not equal to
	 * <code>null</code>.
	 */
	public void testGetCacheModelValidator() {
		CacheModelValidator validator = facade.modelValidator();
		assertNotNull(validator);
		assertEquals(CoherenceModelValidator.class, validator.getClass());
	}

	public void testGetCachingModelEditor() {
		PropertyEditor editor = facade.getCachingModelEditor();

		assertNotNull(editor);
		assertEquals(ReflectionCacheModelEditor.class, editor.getClass());

		ReflectionCacheModelEditor modelEditor = (ReflectionCacheModelEditor) editor;
		assertEquals(CoherenceCachingModel.class, modelEditor.getCacheModelClass());
		assertNull(modelEditor.getCacheModelPropertyEditors());
	}

	public void testGetFlushingModelEditor() {
		PropertyEditor editor = facade.getFlushingModelEditor();

		assertNotNull(editor);
		assertEquals(ReflectionCacheModelEditor.class, editor.getClass());

		ReflectionCacheModelEditor modelEditor = (ReflectionCacheModelEditor) editor;
		assertEquals(CoherenceFlushingModel.class, modelEditor.getCacheModelClass());
		assertNull(modelEditor.getCacheModelPropertyEditors());
	}

	public void testIsSerializableCacheElementRequired() {
		assertFalse(facade.isSerializableCacheElementRequired());
	}

	public void testOnFlushCache() {
		setUpCache();
		cache.put(ENTRY_KEY, ENTRY_VALUE);

		CoherenceFlushingModel model = new CoherenceFlushingModel(
				new String[]{CACHE_NAME});
		facade.onFlushCache(model);
		assertValueIsNotInCache();
		tearDownCache();
	}

	public void testOnGetFromCache() {
		setUpCache();
		cache.put(ENTRY_KEY, ENTRY_VALUE);

		CoherenceCachingModel model = defaultCachingModel();
		assertEquals("Value stored under key " + StringUtils.quote(ENTRY_KEY),
				ENTRY_VALUE, facade.onGetFromCache(ENTRY_KEY, model));

		tearDownCache();
	}

	public void testOnPutInCache() {
		setUpCache();

		CoherenceCachingModel model = defaultCachingModel();
		facade.onPutInCache(ENTRY_KEY, model, ENTRY_VALUE);
		assertValueIsInCache();

		tearDownCache();
	}

	public void testOnPutInCacheWithTimeToLeave() {
		setUpCache();

		long timeToLive = 3000;
		CoherenceCachingModel model = defaultCachingModel();
		model.setTimeToLive(timeToLive);

		facade.onPutInCache(ENTRY_KEY, model, ENTRY_VALUE);
		assertValueIsInCache();

		try {
			Thread.sleep(timeToLive * 2);
		} catch (InterruptedException exception) {
			exception.printStackTrace();
		}

		assertValueIsNotInCache();

		tearDownCache();
	}

	public void testOnRemoveFromCache() {
		setUpCache();
		cache.put(ENTRY_KEY, ENTRY_VALUE);

		CoherenceCachingModel model = defaultCachingModel();
		facade.onRemoveFromCache(ENTRY_KEY, model);
		assertValueIsNotInCache();
	}

	protected void setUp() {
		facade = new CoherenceFacade();
	}

	private void assertValueIsInCache() {
		assertEquals("Value stored under key " + StringUtils.quote(ENTRY_KEY),
				ENTRY_VALUE, cache.get(ENTRY_KEY));
	}

	private void assertValueIsNotInCache() {
		assertNull("There should not be any value stored under the key "
				+ StringUtils.quote(ENTRY_KEY), cache.get(ENTRY_KEY));
	}

	private CoherenceCachingModel defaultCachingModel() {
		return new CoherenceCachingModel(CACHE_NAME);
	}

	private void setUpCache() {
		cache = TangosolUtils.getNamedCache(CACHE_NAME);
		assertTrue("Cache " + StringUtils.quote(CACHE_NAME) + " should be empty",
				cache.isEmpty());
	}

	private void tearDownCache() {
		cache.clear();
		TangosolUtils.shutdownCacheFactory();
	}

}
