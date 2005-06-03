/* 
 * Created on Jun 2, 2005
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
package org.springmodules.xmlrpc.server.apache;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.xmlrpc.XmlRpcHandlerMapping;
import org.apache.xmlrpc.XmlRpcServer;
import org.springframework.beans.factory.BeanCreationException;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractApacheXmlRpcServiceExporter}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/03 02:20:10 $
 */
public class AbstractApacheXmlRpcServiceExporterTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private AbstractApacheXmlRpcServiceExporter xmlRpcServiceExporter;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public AbstractApacheXmlRpcServiceExporterTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.xmlRpcServiceExporter = new AbstractApacheXmlRpcServiceExporter() {
      // no extra implementation.
    };
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractApacheXmlRpcServiceExporter#afterPropertiesSet()}</code>
   * sets up the underlying XML-RPC server if the specified list of handlers is
   * not empty.
   */
  public void testAfterPropertiesSetWhenMapOfHandlersIsNotNullOrEmpty()
      throws Exception {
    Object firstHandler = new Object();
    Object secondHandler = new Object();

    Map handlers = new HashMap();
    handlers.put("firstHandler", firstHandler);
    handlers.put("secondHandler", secondHandler);

    this.xmlRpcServiceExporter.setHandlers(handlers);

    // execute the method to test.
    this.xmlRpcServiceExporter.afterPropertiesSet();

    // verify the XML-RPC is not null and contains the specified handlers.
    XmlRpcServer xmlRpcServer = this.xmlRpcServiceExporter.getXmlRpcServer();
    XmlRpcHandlerMapping handlerMapping = xmlRpcServer.getHandlerMapping();
    assertNotNull("Handler should not be null", handlerMapping
        .getHandler("firstHandler.toString"));
    assertNotNull("Handler should not be null", handlerMapping
        .getHandler("secondHandler.toString"));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractApacheXmlRpcServiceExporter#afterPropertiesSet()}</code>
   * throws a <code>{@link BeanCreationException}</code> if the specified list
   * of handlers is <code>null</code>.
   */
  public void testAfterPropertiesSetWithMapOfHandlersEqualToNull() {
    try {
      this.xmlRpcServiceExporter.afterPropertiesSet();
      fail("A 'BeanCreationException' should have been thrown");
    } catch (BeanCreationException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractApacheXmlRpcServiceExporter#afterPropertiesSet()}</code>
   * throws a <code>{@link BeanCreationException}</code> if the specified list
   * of handlers is <code>null</code>.
   */
  public void testAfterPropertiesSetWithMapOfHandlersIsEmpty() {
    this.xmlRpcServiceExporter.setHandlers(new HashMap());
    
    try {
      this.xmlRpcServiceExporter.afterPropertiesSet();
      fail("A 'BeanCreationException' should have been thrown");
    } catch (BeanCreationException exception) {
      // we are expecting this exception.
    }
  }
}
