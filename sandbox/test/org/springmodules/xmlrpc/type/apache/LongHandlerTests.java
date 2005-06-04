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

/**
 * <p>
 * Unit Tests for <code>{@link LongHandler}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/04 01:23:08 $
 */
public final class LongHandlerTests extends
    AbstractApacheXmlRpcTypeHandlerTestCase {

  /**
   * Object to be handled by <code>{@link #typeHandler}</code>
   */
  private Long objectToHandle;

  /**
   * Primary object that is under test.
   */
  private LongHandler typeHandler;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public LongHandlerTests(String name) {
    super(name);
  }

  /**
   * @see AbstractApacheXmlRpcTypeHandlerTestCase#getExpectedHandledObject()
   */
  protected Object getExpectedHandledObject() {
    return this.objectToHandle.toString();
  }

  /**
   * @see AbstractApacheXmlRpcTypeHandlerTestCase#getExpectedSupportedClass()
   */
  protected Class getExpectedSupportedClass() {
    return this.objectToHandle.getClass();
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
   * @see AbstractApacheXmlRpcTypeHandlerTestCase#onSetUp()
   */
  protected void onSetUp() throws Exception {
    super.onSetUp();

    this.objectToHandle = new Long(999434827l);
    this.typeHandler = new LongHandler();
  }

}
