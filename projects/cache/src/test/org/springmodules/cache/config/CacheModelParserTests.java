/* 
 * Created on Mar 13, 2006
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
 * Copyright @2006 the original author or authors.
 */
package org.springmodules.cache.config;

import java.lang.reflect.Method;

import org.easymock.classextension.MockClassControl;
import org.w3c.dom.Element;

import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.mock.MockFlushingModel;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheModelParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheModelParserTests extends TestCase {

  private AbstractCacheModelParser parser;

  private MockClassControl parserControl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public CacheModelParserTests(String name) {
    super(name);
  }

  public void testParseFlushingModel() {
    Element element = new DomElementStub("flushing");
    element.setAttribute("when", "after");

    FlushingModel expected = new MockFlushingModel();

    parser.doParseFlushingModel(element, false);
    parserControl.setReturnValue(expected);

    parserControl.replay();

    FlushingModel actual = parser.parseFlushingModel(element);
    assertSame(expected, actual);

    parserControl.verify();
  }

  protected void setUp() throws Exception {
    Class targetClass = AbstractCacheModelParser.class;

    Method doParseFlushingModelMethod = targetClass.getDeclaredMethod(
        "doParseFlushingModel", new Class[] { Element.class, boolean.class });

    Method[] methodsToMock = { doParseFlushingModelMethod };

    parserControl = MockClassControl.createControl(targetClass, null, null,
        methodsToMock);

    parser = (AbstractCacheModelParser) parserControl.getMock();
  }

}
