/* 
 * Created on Sep 24, 2004
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

package org.springmodules.cache.interceptor.caching;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.MockControl;
import org.springframework.metadata.Attributes;
import org.springmodules.cache.CachingModel;
import org.springmodules.cache.mock.MockCachingModel;

/**
 * <p>
 * Unit Tests for <code>{@link MetadataCachingInterceptor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class MetadataCachingInterceptorTests extends TestCase {

  private class MockMetadataCachingInterceptor extends
      MetadataCachingInterceptor {
    Cached cachingAttribute;

    protected Cached getCachingAttribute(MethodInvocation methodInvocation) {
      return cachingAttribute;
    }
  }

  private MetadataCachingInterceptor interceptor;

  private MethodInvocation invocation;

  private MockControl invocationControl;

  private CachingAttributeSource source;

  private MockControl sourceControl;

  public MetadataCachingInterceptorTests(String name) {
    super(name);
  }

  private void setStateOfMockControlsToReplay() {
    invocationControl.replay();
    sourceControl.replay();
  }

  protected void setUp() {
    sourceControl = MockControl
        .createStrictControl(CachingAttributeSource.class);
    source = (CachingAttributeSource) sourceControl.getMock();

    invocationControl = MockControl.createStrictControl(MethodInvocation.class);
    invocation = (MethodInvocation) invocationControl.getMock();

    interceptor = new MetadataCachingInterceptor();
    interceptor.setCachingAttributeSource(source);
  }

  public void testGetCachingAttribute() throws Exception {
    Object thisObject = "Anakin";
    invocation.getThis();
    invocationControl.setReturnValue(thisObject);

    Method method = String.class.getDeclaredMethod("toLowerCase", null);
    invocation.getMethod();
    invocationControl.setReturnValue(method);

    Cached expected = new Cached();
    source.getCachingAttribute(method, thisObject.getClass());
    sourceControl.setReturnValue(expected);

    setStateOfMockControlsToReplay();

    assertSame(expected, interceptor.getCachingAttribute(invocation));
    verifyExpectationsOfMockControlsWereMet();
  }

  public void testGetModel() {
    MockMetadataCachingInterceptor mockInterceptor = new MockMetadataCachingInterceptor();

    String modelId = "Han";
    Cached cachingAttribute = new Cached(modelId);
    mockInterceptor.cachingAttribute = cachingAttribute;

    CachingModel expected = new MockCachingModel();
    Map models = new HashMap();
    models.put(modelId, expected);
    mockInterceptor.setCachingModels(models);

    assertSame(expected, mockInterceptor.getModel(invocation));
  }

  public void testGetModelWhenCachingAttributeDoesNotHaveModelId() {
    MockMetadataCachingInterceptor mockInterceptor = new MockMetadataCachingInterceptor();
    mockInterceptor.cachingAttribute = new Cached();
    assertNull(mockInterceptor.getModel(invocation));
  }

  public void testGetModelWhenCachingAttributeIsNull() {
    MockMetadataCachingInterceptor mockInterceptor = new MockMetadataCachingInterceptor();
    mockInterceptor.cachingAttribute = null;
    assertNull(mockInterceptor.getModel(invocation));
  }

  public void testSetAttributes() {
    MockControl attributesControl = MockControl.createControl(Attributes.class);
    Attributes attributes = (Attributes) attributesControl.getMock();

    interceptor.setAttributes(attributes);
    CachingAttributeSource newSource = interceptor.getCachingAttributeSource();
    assertEquals(MetadataCachingAttributeSource.class, newSource.getClass());
    assertSame(attributes, ((MetadataCachingAttributeSource) newSource)
        .getAttributes());
  }

  private void verifyExpectationsOfMockControlsWereMet() {
    invocationControl.verify();
    sourceControl.verify();
  }
}