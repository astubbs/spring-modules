/* 
 * Created on Oct 22, 2004
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

package org.springmodules.cache.interceptor.flush;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.MockControl;
import org.springframework.metadata.Attributes;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.mock.MockFlushingModel;

/**
 * <p>
 * Unit Tests for <code>{@link MetadataFlushingInterceptor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class MetadataFlushingInterceptorTests extends TestCase {

  private class MockMetadataFlushingInterceptor extends
      MetadataFlushingInterceptor {
    FlushCache flushingAttribute;

    protected FlushCache getFlushingAttribute(MethodInvocation methodInvocation) {
      return flushingAttribute;
    }
  }

  private MetadataFlushingInterceptor interceptor;

  private MethodInvocation invocation;

  private MockControl invocationControl;

  private FlushingAttributeSource source;

  private MockControl sourceControl;

  public MetadataFlushingInterceptorTests(String name) {
    super(name);
  }

  private void replayMocks() {
    invocationControl.replay();
    sourceControl.replay();
  }

  protected void setUp() {
    sourceControl = MockControl
        .createStrictControl(FlushingAttributeSource.class);
    source = (FlushingAttributeSource) sourceControl.getMock();

    invocationControl = MockControl.createStrictControl(MethodInvocation.class);
    invocation = (MethodInvocation) invocationControl.getMock();

    interceptor = new MetadataFlushingInterceptor();
    interceptor.setFlushingAttributeSource(source);
  }

  public void testGetFlushingAttribute() throws Exception {
    Object thisObject = "Anakin";
    invocationControl.expectAndReturn(invocation.getThis(), thisObject);

    Method method = String.class.getDeclaredMethod("toLowerCase", null);
    invocationControl.expectAndReturn(invocation.getMethod(), method);

    FlushCache expected = new FlushCache();
    sourceControl.expectAndReturn(source.getFlushingAttribute(method,
        thisObject.getClass()), expected);

    replayMocks();

    assertSame(expected, interceptor.getFlushingAttribute(invocation));
    verifyMocks();
  }

  public void testGetModel() {
    MockMetadataFlushingInterceptor mockInterceptor = new MockMetadataFlushingInterceptor();

    String modelId = "Han";
    FlushCache flushingAttribute = new FlushCache(modelId);
    mockInterceptor.flushingAttribute = flushingAttribute;

    FlushingModel expected = new MockFlushingModel();
    Map models = new HashMap();
    models.put(modelId, expected);
    mockInterceptor.setFlushingModels(models);

    assertSame(expected, mockInterceptor.getModel(invocation));
  }

  public void testGetModelWhenFlushingAttributeDoesNotHaveModelId() {
    MockMetadataFlushingInterceptor mockInterceptor = new MockMetadataFlushingInterceptor();
    mockInterceptor.flushingAttribute = new FlushCache();
    assertNull(mockInterceptor.getModel(invocation));
  }

  public void testGetModelWhenFlushingAttributeIsNull() {
    MockMetadataFlushingInterceptor mockInterceptor = new MockMetadataFlushingInterceptor();
    mockInterceptor.flushingAttribute = null;
    assertNull(mockInterceptor.getModel(invocation));
  }

  public void testSetAttributes() {
    MockControl attributesControl = MockControl.createControl(Attributes.class);
    Attributes attributes = (Attributes) attributesControl.getMock();

    interceptor.setAttributes(attributes);
    FlushingAttributeSource newSource = interceptor
        .getFlushingAttributeSource();
    assertEquals(MetadataFlushingAttributeSource.class, newSource.getClass());
    assertSame(attributes, ((MetadataFlushingAttributeSource) newSource)
        .getAttributes());
  }

  private void verifyMocks() {
    invocationControl.verify();
    sourceControl.verify();
  }
}