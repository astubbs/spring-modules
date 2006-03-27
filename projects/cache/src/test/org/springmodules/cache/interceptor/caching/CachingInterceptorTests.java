/* 
 * Created on Oct 7, 2005
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
 * Copyright @2005 the original author or authors.
 */
package org.springmodules.cache.interceptor.caching;

import java.beans.PropertyEditor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.MockControl;

import org.springmodules.AssertExt;
import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.key.CacheKeyGenerator;
import org.springmodules.cache.key.HashCodeCacheKeyGenerator;
import org.springmodules.cache.mock.MockCachingModel;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.CacheProviderFacade;
import org.springmodules.cache.provider.InvalidCacheModelException;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCachingInterceptor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CachingInterceptorTests extends TestCase {

  private class MockCachingInterceptor extends AbstractCachingInterceptor {

    CachingModel model;

    boolean onAfterPropertiesSetCalled;

    protected CachingModel getModel(MethodInvocation newMethodInvocation) {
      return model;
    }

    protected void onAfterPropertiesSet() throws FatalCacheException {
      onAfterPropertiesSetCalled = true;
    }
  }

  private static final String CACHE_ENTRY_KEY = "C-3PO";

  private CacheProviderFacade cacheProviderFacade;

  private MockControl cacheProviderFacadeControl;

  private PropertyEditor editor;

  private MockControl editorControl;

  private MockCachingInterceptor interceptor;

  private MethodInvocation invocation;

  private MockControl invocationControl;

  private CacheKeyGenerator keyGenerator;

  private MockControl keyGeneratorControl;

  private CachingListener listener;

  private MockControl listenerControl;

  private CacheModelValidator validator;

  private MockControl validatorControl;

  public CachingInterceptorTests(String name) {
    super(name);
  }

  public void testAfterPropertiesSetWhenCacheModelValidatorThrowsException() {
    CachingModel model = new MockCachingModel();
    Map models = new HashMap();
    models.put("key", model);

    setUpValidator();
    expectGetCacheModelValidator();

    InvalidCacheModelException expected = new InvalidCacheModelException("");
    validator.validateCachingModel(model);
    validatorControl.setThrowable(expected);

    replay();

    interceptor.setCachingModels(models);

    try {
      interceptor.afterPropertiesSet();
      fail();
    } catch (FatalCacheException exception) {
      assertSame(expected, exception.getCause());
    }

    verify();
    assertFalse(interceptor.onAfterPropertiesSetCalled);
  }

  public void testAfterPropertiesSetWhenCachingModelEditorThrowsException() {
    Properties models = createModelsAsProperties(1);

    setUpValidator();
    expectGetCacheModelValidator();

    setUpCachingModelEditor();
    expectGetCachingModelEditor();

    // create a Map of CachingModels from each of the properties.
    RuntimeException expected = new RuntimeException();
    for (Iterator i = models.keySet().iterator(); i.hasNext();) {
      String key = (String) i.next();
      String value = models.getProperty(key);

      editor.setAsText(value);
      editorControl.expectAndThrow(editor.getValue(), expected);
    }

    replay();

    interceptor.setCachingModels(models);
    try {
      interceptor.afterPropertiesSet();
      fail();
    } catch (FatalCacheException exception) {
      assertSame(expected, exception.getCause());
    }

    verify();
    assertFalse(interceptor.onAfterPropertiesSetCalled);
  }

  public void testAfterPropertiesSetWithCacheProviderFacadeEqualToNull() {
    interceptor.setCacheProviderFacade(null);
    assertAfterPropertiesSetThrowsException();
  }

  public void testAfterPropertiesSetWithCachingModelMapBeingProperties() {
    Properties models = createModelsAsProperties(2);

    setUpValidator();
    expectGetCacheModelValidator();

    setUpCachingModelEditor();
    expectGetCachingModelEditor();

    // create a Map of CachingModels from each of the properties.
    Map expected = new HashMap();
    for (Iterator i = models.keySet().iterator(); i.hasNext();) {
      String key = (String) i.next();
      String value = models.getProperty(key);

      MockCachingModel model = new MockCachingModel();

      editor.setAsText(value);
      editorControl.expectAndReturn(editor.getValue(), model);

      validator.validateCachingModel(model);

      expected.put(key, model);
    }

    replay();

    interceptor.setCachingModels(models);
    interceptor.afterPropertiesSet();
    assertEquals(expected, interceptor.getCachingModels());

    verify();
    assertTrue(interceptor.onAfterPropertiesSetCalled);
  }

  public void testAfterPropertiesSetWithCachingModelMapEqualToNull() {
    interceptor.setCachingModels(null);
    assertAfterPropertiesSetThrowsException();
  }

  public void testAfterPropertiesSetWithEmptyCachingModelMap() {
    interceptor.setCachingModels(new HashMap());
    assertAfterPropertiesSetThrowsException();
  }

  public void testAfterPropertiesSetWithNotEmptyCachingModelMapAndKeyGeneratorEqualToNull() {
    setUpValidator();
    interceptor.setCacheKeyGenerator(null);

    Map models = new HashMap();
    for (int i = 0; i < 2; i++) {
      models.put(Integer.toString(i), new MockCachingModel());
    }

    expectGetCacheModelValidator();

    for (Iterator i = models.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();
      CachingModel model = (CachingModel) entry.getValue();
      validator.validateCachingModel(model);
    }

    replay();

    interceptor.setCachingModels(models);
    interceptor.afterPropertiesSet();

    AssertExt.assertInstanceOf(HashCodeCacheKeyGenerator.class, interceptor
        .getCacheKeyGenerator());

    verify();
    assertTrue(interceptor.onAfterPropertiesSetCalled);
  }

  public void testInvokeWhenCacheReturnsNullAndProceedReturnsNull()
      throws Throwable {
    setUpCachingListener();
    expectMethodInvocationReturnsCacheableMethod();

    Serializable key = "Luke Skywalker";
    CachingModel model = expectGetFromCache(key, null);

    invocationControl.expectAndReturn(invocation.proceed(), null);

    cacheProviderFacade.putInCache(key, model,
        AbstractCachingInterceptor.NULL_ENTRY);

    listener.onCaching(key, null, model);
    replay();

    assertNull(interceptor.invoke(invocation));
    verify();
  }

  public void testInvokeWhenCacheReturnsNullAndProceedReturnsObjectNotEqualToNull()
      throws Throwable {
    setUpCachingListener();
    expectMethodInvocationReturnsCacheableMethod();

    Serializable key = "Anakin Skywalker";
    Object expected = new Object();
    CachingModel model = expectGetFromCache(key, null);

    invocationControl.expectAndReturn(invocation.proceed(), expected);

    cacheProviderFacade.putInCache(key, model, expected);

    listener.onCaching(key, expected, model);
    replay();

    assertSame(expected, interceptor.invoke(invocation));
    verify();
  }

  public void testInvokeWhenCacheReturnsNullAndProceedThrowsException()
      throws Throwable {
    expectMethodInvocationReturnsCacheableMethod();

    expectGetFromCache(CACHE_ENTRY_KEY, null);

    Exception expected = new Exception();
    invocationControl.expectAndThrow(invocation.proceed(), expected);

    cacheProviderFacade.cancelCacheUpdate(CACHE_ENTRY_KEY);
    replay();

    try {
      interceptor.invoke(invocation);
      fail();
    } catch (Exception exception) {
      assertSame(expected, exception);
    }

    verify();
  }

  public void testInvokeWhenCacheReturnsNullEntryObject() throws Throwable {
    expectMethodInvocationReturnsCacheableMethod();

    expectGetFromCache(CACHE_ENTRY_KEY, AbstractCachingInterceptor.NULL_ENTRY);
    replay();

    assertNull(interceptor.invoke(invocation));
    verify();
  }

  public void testInvokeWhenCacheReturnsNullWithoutCachingListeners()
      throws Throwable {
    expectMethodInvocationReturnsCacheableMethod();

    Serializable key = "Luke Skywalker";
    CachingModel model = expectGetFromCache(key, null);

    Object expected = new Object();
    invocationControl.expectAndReturn(invocation.proceed(), expected);

    cacheProviderFacade.putInCache(key, model, expected);

    replay();

    assertSame(expected, interceptor.invoke(invocation));
    verify();
  }

  public void testInvokeWhenCacheReturnsStoredObject() throws Throwable {
    expectMethodInvocationReturnsCacheableMethod();

    Serializable key = "R2-D2";
    Object expected = new Object();
    expectGetFromCache(key, expected);
    replay();

    assertSame(expected, interceptor.invoke(invocation));
    verify();
  }

  public void testInvokeWithReturnedCachingModelEqualToNull() throws Throwable {
    interceptor.model = null;
    expectMethodInvocationReturnsCacheableMethod();

    Object expected = new Object();
    invocationControl.expectAndReturn(invocation.proceed(), expected);

    replay();

    assertSame(expected, interceptor.invoke(invocation));
    verify();
  }

  protected void setUp() {
    cacheProviderFacadeControl = MockControl
        .createStrictControl(CacheProviderFacade.class);
    cacheProviderFacade = (CacheProviderFacade) cacheProviderFacadeControl
        .getMock();

    keyGeneratorControl = MockControl
        .createStrictControl(CacheKeyGenerator.class);
    keyGenerator = (CacheKeyGenerator) keyGeneratorControl.getMock();

    interceptor = new MockCachingInterceptor();
    interceptor.setCacheKeyGenerator(keyGenerator);
    interceptor.setCacheProviderFacade(cacheProviderFacade);
  }

  private void assertAfterPropertiesSetThrowsException() {
    try {
      interceptor.afterPropertiesSet();
      fail();
    } catch (FatalCacheException exception) {
      // we are expecting this exception.
    }
  }

  private Properties createModelsAsProperties(int modelCount) {
    String keyPrefix = "key";
    String valuePrefix = "value";

    Properties models = new Properties();
    for (int i = 0; i < modelCount; i++) {
      models.setProperty(keyPrefix + i, valuePrefix + i);
    }

    return models;
  }

  private void expectGetCacheModelValidator() {
    cacheProviderFacadeControl.expectAndReturn(cacheProviderFacade
        .getCacheModelValidator(), validator);
  }

  private void expectGetCachingModelEditor() {
    cacheProviderFacadeControl.expectAndReturn(cacheProviderFacade
        .getCachingModelEditor(), editor);
  }

  private CachingModel expectGetFromCache(Serializable key, Object expected) {
    MockCachingModel model = new MockCachingModel();
    interceptor.model = model;

    keyGenerator.generateKey(invocation);
    keyGeneratorControl.setReturnValue(key);

    cacheProviderFacadeControl.expectAndReturn(cacheProviderFacade
        .getFromCache(key, model), expected);
    return model;
  }

  private void expectMethodInvocationReturnsCacheableMethod() throws Exception {
    setUpMethodInvocation();
    Method method = CachingTestUtils.createCacheableMethod();
    invocationControl.expectAndReturn(invocation.getMethod(), method);
  }

  private void replay() {
    cacheProviderFacadeControl.replay();

    if (editorControl != null) {
      editorControl.replay();
    }

    if (invocationControl != null) {
      invocationControl.replay();
    }

    if (listenerControl != null) {
      listenerControl.replay();
    }

    keyGeneratorControl.replay();

    if (validatorControl != null) {
      validatorControl.replay();
    }
  }

  private void setUpCachingListener() {
    listenerControl = MockControl.createControl(CachingListener.class);
    listener = (CachingListener) listenerControl.getMock();

    interceptor.setCachingListeners(new CachingListener[] { listener });
  }

  private void setUpCachingModelEditor() {
    editorControl = MockControl.createControl(PropertyEditor.class);
    editor = (PropertyEditor) editorControl.getMock();
  }

  private void setUpMethodInvocation() {
    invocationControl = MockControl.createControl(MethodInvocation.class);
    invocation = (MethodInvocation) invocationControl.getMock();
  }

  private void setUpValidator() {
    validatorControl = MockControl.createControl(CacheModelValidator.class);
    validator = (CacheModelValidator) validatorControl.getMock();
  }

  private void verify() {
    cacheProviderFacadeControl.verify();

    if (editorControl != null) {
      editorControl.verify();
    }

    if (invocationControl != null) {
      invocationControl.verify();
    }

    if (listenerControl != null) {
      listenerControl.verify();
    }

    keyGeneratorControl.verify();

    if (validatorControl != null) {
      validatorControl.verify();
    }
  }
}
