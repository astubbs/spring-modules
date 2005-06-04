/* 
 * Created on Jun 3, 2005
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
package org.springmodules.xmlrpc.type.apache;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.springmodules.xmlrpc.type.XmlRpcTypeHandlerRegistry;

/**
 * <p>
 * Unit Tests for <code>{@link MapHandler}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/04 01:23:08 $
 */
public class MapHandlerTests extends AbstractApacheXmlRpcTypeHandlerTestCase {

  /**
   * Object to be handled by <code>{@link #typeHandler}</code>
   */
  private Map objectToHandle;

  /**
   * Registry of available data type handlers;
   */
  private XmlRpcTypeHandlerRegistry registry;

  /**
   * Primary object that is under test.
   */
  private MapHandler typeHandler;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public MapHandlerTests(String name) {
    super(name);
  }

  /**
   * @see AbstractApacheXmlRpcTypeHandlerTestCase#getExpectedHandledObject()
   */
  protected Object getExpectedHandledObject() {
    Hashtable hashtable = new Hashtable();

    Iterator iterator = this.objectToHandle.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry) iterator.next();

      Short value = (Short) entry.getValue();
      Integer newValue = new Integer(value.intValue());
      
      hashtable.put(entry.getKey(), newValue);
    }

    return hashtable;
  }

  /**
   * @see AbstractApacheXmlRpcTypeHandlerTestCase#getExpectedSupportedClass()
   */
  protected Class getExpectedSupportedClass() {
    return Map.class;
  }

  /**
   * @see AbstractApacheXmlRpcTypeHandlerTestCase#getObjectToHandle()
   */
  protected Object getObjectToHandle() {
    return this.objectToHandle;
  }

  /**
   * @see AbstractApacheXmlRpcTypeHandlerTestCase#getTypeHandler()
   */
  protected AbstractApacheXmlRpcTypeHandler getTypeHandler() {
    return this.typeHandler;
  }

  /**
   * @see AbstractApacheXmlRpcTypeHandlerTestCase#getTypeHandlerRegistry()
   */
  protected XmlRpcTypeHandlerRegistry getTypeHandlerRegistry() {
    return this.registry;
  }

  /**
   * @see AbstractApacheXmlRpcTypeHandlerTestCase#onSetUp()
   */
  protected void onSetUp() throws Exception {
    super.onSetUp();

    this.objectToHandle = new HashMap();
    this.objectToHandle.put("Leia", new Short((short) 18));
    this.objectToHandle.put("Luke", new Short((short) 18));
    this.objectToHandle.put("Han", new Short((short) 20));

    this.registry = new ListTypeHandlerRegistry();
    this.typeHandler = new MapHandler();
  }

}
