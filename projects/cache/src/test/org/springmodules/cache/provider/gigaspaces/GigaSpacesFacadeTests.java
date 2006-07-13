/*
* Copyright 2006 GigaSpaces, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.springmodules.cache.provider.gigaspaces;

import java.beans.PropertyEditor;

import junit.framework.TestCase;

import com.j_spaces.core.client.FinderException;
import com.j_spaces.map.CacheFinder;
import com.j_spaces.map.IMap;

import org.springframework.util.StringUtils;

import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.ReflectionCacheModelEditor;

/**
 * <p>
 * Unit Tests for <code>{@link GigaSpacesFacade}</code>.
 * </p>
 *
 * @author Alex Ruiz
 */
public class GigaSpacesFacadeTests extends TestCase {

  private static final String CACHE_NAME = "/./myCache";

  private static final String ENTRY_KEY = "KEY";

  private static final String ENTRY_VALUE = "VALUE";

  private IMap cache;

  private GigaSpacesFacade facade;

  /**
   * Verifies that the method
   * <code>{@link GigaSpacesFacade#modelValidator()}</code> returns an
   * an instance of <code>{@link GigaSpacesModelValidator}</code> not equal to
   * <code>null</code>.
   */
  public void testGetCacheModelValidator() {
    CacheModelValidator validator = facade.modelValidator();
    assertNotNull(validator);
    assertEquals(GigaSpacesModelValidator.class, validator.getClass());
  }

  public void testGetCachingModelEditor() {
    PropertyEditor editor = facade.getCachingModelEditor();

    assertNotNull(editor);
    assertEquals(ReflectionCacheModelEditor.class, editor.getClass());

    ReflectionCacheModelEditor modelEditor = (ReflectionCacheModelEditor) editor;
    assertEquals(GigaSpacesCachingModel.class, modelEditor.getCacheModelClass());
    assertNull(modelEditor.getCacheModelPropertyEditors());
  }

  public void testGetFlushingModelEditor() {
    PropertyEditor editor = facade.getFlushingModelEditor();

    assertNotNull(editor);
    assertEquals(ReflectionCacheModelEditor.class, editor.getClass());

    ReflectionCacheModelEditor modelEditor = (ReflectionCacheModelEditor) editor;
    assertEquals(GigaSpacesFlushingModel.class, modelEditor.getCacheModelClass());
    assertNull(modelEditor.getCacheModelPropertyEditors());
  }

  public void testIsSerializableCacheElementRequired() {
    assertFalse(facade.isSerializableCacheElementRequired());
  }

  public void testOnFlushCache() {
    setUpCache();
    cache.put(ENTRY_KEY, ENTRY_VALUE);

    GigaSpacesFlushingModel model = new GigaSpacesFlushingModel(
        new String[] { CACHE_NAME });
    facade.onFlushCache(model);
    assertValueIsNotInCache();
    tearDownCache();
  }

  public void testOnGetFromCache() {
    setUpCache();
    cache.put(ENTRY_KEY, ENTRY_VALUE);

    GigaSpacesCachingModel model = defaultCachingModel();
    assertEquals("Value stored under key " + StringUtils.quote(ENTRY_KEY),
        ENTRY_VALUE, facade.onGetFromCache(ENTRY_KEY, model));

    tearDownCache();
  }

  public void testOnPutInCache() {
    setUpCache();

    GigaSpacesCachingModel model = defaultCachingModel();
    facade.onPutInCache(ENTRY_KEY, model, ENTRY_VALUE);
    assertValueIsInCache();

    tearDownCache();
  }

  public void testOnPutInCacheWithTimeToLeave() {
    setUpCache();

    long timeToLive = 3000;
    GigaSpacesCachingModel model = defaultCachingModel();
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

    GigaSpacesCachingModel model = defaultCachingModel();
    facade.onRemoveFromCache(ENTRY_KEY, model);
    assertValueIsNotInCache();
  }

  protected void setUp() {
    facade = new GigaSpacesFacade();
  }

  private void assertValueIsInCache() {
    assertEquals("Value stored under key " + StringUtils.quote(ENTRY_KEY),
        ENTRY_VALUE, cache.get(ENTRY_KEY));
  }

  private void assertValueIsNotInCache() {
    assertNull("There should not be any value stored under the key "
        + StringUtils.quote(ENTRY_KEY), cache.get(ENTRY_KEY));
  }

  private GigaSpacesCachingModel defaultCachingModel() {
    return new GigaSpacesCachingModel(CACHE_NAME);
  }

  private void setUpCache() {
    try
	{
		cache = (IMap) CacheFinder.find(CACHE_NAME);
	}
	catch (FinderException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    assertTrue("Cache " + StringUtils.quote(CACHE_NAME) + " should be empty",
        cache.isEmpty());
  }

  private void tearDownCache() {
    cache.clear();

  }
}
