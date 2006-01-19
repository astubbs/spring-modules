/* 
 * Created on Oct 26, 2005
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
package org.springmodules.cache.interceptor.flush;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractFlushingAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class FlushingAttributeSourceTests extends TestCase {

  private class FlushingAttributeSource extends AbstractFlushingAttributeSource {
    protected Collection findAllAttributes(Method newMethod) {
      return null;
    }
  }

  private FlushingAttributeSource source;

  public FlushingAttributeSourceTests(String name) {
    super(name);
  }

  public void testFindAttribute() {
    FlushCache expected = new FlushCache();
    List attributes = new ArrayList();
    attributes.add(expected);
    assertSame(expected, source.findAttribute(attributes));
  }

  public void testFindAttributeWithCollectionEqualToNull() {
    assertNull(source.findAttribute(null));
  }

  public void testFindAttributeWithCollectionHavingObjectsThatAreNotAttributes() {
    List attributes = new ArrayList();
    attributes.add("Luke");
    attributes.add("Leia");
    assertNull(source.findAttribute(attributes));
  }

  public void testFindAttributeWithEmptyCollection() {
    assertNull(source.findAttribute(new ArrayList()));
  }

  protected void setUp() {
    source = new FlushingAttributeSource();
  }
}
