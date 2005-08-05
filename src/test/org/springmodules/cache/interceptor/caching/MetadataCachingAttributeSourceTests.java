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
 * Unit Tests for <code>{@link MetadataCachingAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/08/05 02:18:48 $
 */
public final class MetadataCachingAttributeSourceTests extends TestCase {

  private Attributes attributes;

  private MockControl attributesControl;

  /**
   * Primary object that is under test.
   */
  private MetadataCachingAttributeSource source;

  public MetadataCachingAttributeSourceTests(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.attributesControl = MockControl.createControl(Attributes.class);
    this.attributes = (Attributes) this.attributesControl.getMock();

    this.source = new MetadataCachingAttributeSource();
    this.source.setAttributes(this.attributes);
  }

  /**
   * Verifies that the method
   * <code>{@link MetadataCachingAttributeSource#findAllAttributes(Method)}</code>
   * retrieves the attributes for the specified method using
   * <code>{@link #attributes}</code>.
   */
  public void testFindAllAttributesMethod() throws Exception {
    Class clazz = String.class;
    Method method = clazz.getMethod("charAt", new Class[] { int.class });

    List attributeList = new ArrayList();
    this.attributes.getAttributes(method);
    this.attributesControl.setReturnValue(attributeList);
    this.attributesControl.replay();

    Collection returnedAttributes = this.source.findAllAttributes(method);

    assertSame("<returned Attributes>", attributeList, returnedAttributes);
    
    this.attributesControl.verify();
  }

}