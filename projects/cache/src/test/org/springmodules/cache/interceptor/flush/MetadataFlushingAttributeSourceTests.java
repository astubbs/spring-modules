/* 
 * Created on Sep 23, 2004
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.MockControl;

import org.springframework.metadata.Attributes;

/**
 * <p>
 * Unit Tests for <code>{@link MetadataFlushingAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class MetadataFlushingAttributeSourceTests extends TestCase {

  private Attributes attributes;

  private MockControl attributesControl;

  private MetadataFlushingAttributeSource source;

  public MetadataFlushingAttributeSourceTests(String name) {
    super(name);
  }

  /**
   * Verifies that the method
   * <code>{@link MetadataFlushingAttributeSource#findAllAttributes(Method)}</code>.
   * retrieves the attributes for the specified method using
   * <code>{@link #attributes}</code>.
   */
  public void testFindAllAttributesMethod() throws Exception {
    Class clazz = String.class;
    Method method = clazz.getMethod("charAt", new Class[] { int.class });

    List attributeList = new ArrayList();
    attributesControl.expectAndReturn(attributes.getAttributes(method),
        attributeList);
    attributesControl.replay();

    Collection returnedAttributes = source.findAllAttributes(method);

    attributesControl.verify();
    assertSame(attributeList, returnedAttributes);
  }

  protected void setUp() {
    attributesControl = MockControl.createStrictControl(Attributes.class);
    attributes = (Attributes) attributesControl.getMock();

    source = new MetadataFlushingAttributeSource();
    source.setAttributes(attributes);
  }

}