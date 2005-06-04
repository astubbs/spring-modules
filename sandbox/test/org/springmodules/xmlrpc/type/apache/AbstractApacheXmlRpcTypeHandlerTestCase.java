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

import org.springmodules.xmlrpc.type.XmlRpcTypeHandlerRegistry;

import junit.framework.TestCase;

/**
 * <p>
 * Template for classes that test concrete subclasses of
 * <code>{@link AbstractApacheXmlRpcTypeHandler}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/04 01:23:08 $
 */
public abstract class AbstractApacheXmlRpcTypeHandlerTestCase extends TestCase {

  /**
   * Primary object that is under test.
   */
  private AbstractApacheXmlRpcTypeHandler typeHandler;

  /**
   * Constructor.
   */
  public AbstractApacheXmlRpcTypeHandlerTestCase() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public AbstractApacheXmlRpcTypeHandlerTestCase(String name) {
    super(name);
  }

  /**
   * Returns the expected result of the method
   * <code>{@link AbstractApacheXmlRpcTypeHandler#handle(Object, XmlRpcTypeHandlerRegistry)}</code>.
   * 
   * @return the expected result of the method '<code>handle</code>'.
   */
  protected abstract Object getExpectedHandledObject();

  /**
   * Returns the class that the method
   * <code>{@link org.springmodules.xmlrpc.type.XmlRpcTypeHandler#getSupportedClass()}</code>
   * should return.
   * 
   * @return the class that the object we are testing should support.
   */
  protected abstract Class getExpectedSupportedClass();

  /**
   * Returns the object to be sent as argument of the method
   * <code>{@link AbstractApacheXmlRpcTypeHandler#handle(Object, XmlRpcTypeHandlerRegistry)}</code>.
   * 
   * @return the object to be handled by the object we are testing.
   */
  protected abstract Object getObjectToHandle();

  /**
   * Returns the primary object that is under test.
   * 
   * @return the primary object that is under test.
   */
  protected abstract AbstractApacheXmlRpcTypeHandler getTypeHandler();

  /**
   * Returns the registry of data type handlers to be used to test the method
   * <code>{@link AbstractApacheXmlRpcTypeHandler#handle(Object, XmlRpcTypeHandlerRegistry)}</code>.
   * 
   * @return the registry of data type handlers to use in our tests.
   */
  protected XmlRpcTypeHandlerRegistry getTypeHandlerRegistry() {
    return null;
  }

  /**
   * Gives subclasses the opportunity to set up their own test fixture.
   */
  protected void onSetUp() throws Exception {
    // subclasses may override this method.
  }

  /**
   * Sets up the test fixture.
   */
  public final void setUp() throws Exception {
    super.setUp();

    this.onSetUp();
    this.typeHandler = this.getTypeHandler();
  }

  /**
   * Verifies that the class supported by <code>{@link #typeHandler}</code> is
   * correct.
   */
  public final void testGetSupportedClass() {
    assertEquals("<Supported class>", this.getExpectedSupportedClass(),
        this.typeHandler.getSupportedClass());
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractApacheXmlRpcTypeHandler#handle(Object, XmlRpcTypeHandlerRegistry)}</code>
   * works correctly.
   */
  public final void testHandle() {
    Object expectedHandledObject = this.getExpectedHandledObject();
    Object objectToHandle = this.getObjectToHandle();
    XmlRpcTypeHandlerRegistry typeHandlerRegistry = this
        .getTypeHandlerRegistry();

    Object actual = this.typeHandler
        .handle(objectToHandle, typeHandlerRegistry);

    assertEquals("<Handled object>", expectedHandledObject, actual);
  }
}
