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

package org.springmodules.cache.interceptor.caching;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.springframework.metadata.Attributes;

/**
 * <p>
 * Unit Test for <code>{@link MetadataCachingAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:41:12 $
 */
public final class MetadataCachingAttributeSourceTests extends TestCase {

  /**
   * Mock object that simulates the access of attributes at runtime.
   */
  private Attributes mockAttributes;

  /**
   * Controls the behavior of <code>{@link #mockAttributes}</code>.
   */
  private MockControl mockAttributesControl;

  /**
   * Primary object (instance of the class to test).
   */
  private MetadataCachingAttributeSource source;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public MetadataCachingAttributeSourceTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.mockAttributesControl = MockControl.createControl(Attributes.class);
    this.mockAttributes = (Attributes) this.mockAttributesControl.getMock();

    this.source = new MetadataCachingAttributeSource();
    this.source.setAttributes(this.mockAttributes);
  }

  /**
   * Verifies that the method
   * <code>{@link MetadataCachingAttributeSource#findAllAttributes(Method)}</code>
   * retrieves the attributes for the specified method using
   * <code>{@link #mockAttributes}</code>.
   */
  public void testFindAllAttributesMethod() throws Exception {

    Class clazz = String.class;
    Method method = clazz.getMethod("charAt", new Class[] { int.class });

    List attributeList = new ArrayList();
    this.mockAttributes.getAttributes(method);
    this.mockAttributesControl.setReturnValue(attributeList);
    this.mockAttributesControl.replay();

    Collection returnedAttributes = this.source.findAllAttributes(method);

    this.mockAttributesControl.verify();
    assertSame("<returned Attributes>", attributeList, returnedAttributes);
  }

}