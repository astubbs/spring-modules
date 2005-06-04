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
package org.springmodules.xmlrpc.type;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.MockControl;
import org.springframework.beans.factory.BeanCreationException;

/**
 * <p>
 * Unit Test for <code>{@link ReturnValueTypeHandlerAdvice}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/04 01:21:31 $
 */
public class ReturnValueTypeHandlerAdviceTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private ReturnValueTypeHandlerAdvice advice;

  /**
   * Mock object that simulates <code>{@link XmlRpcTypeHandlerRegistry}</code>.
   */
  private XmlRpcTypeHandlerRegistry typeHandlerRegistry;

  /**
   * Controls the behavior of <code>{@link #typeHandlerRegistry}</code>.
   */
  private MockControl typeHandlerRegistryControl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public ReturnValueTypeHandlerAdviceTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.typeHandlerRegistryControl = MockControl
        .createControl(XmlRpcTypeHandlerRegistry.class);
    this.typeHandlerRegistry = (XmlRpcTypeHandlerRegistry) this.typeHandlerRegistryControl
        .getMock();

    this.advice = new ReturnValueTypeHandlerAdvice();
    this.advice.setTypeHandlerRegistry(this.typeHandlerRegistry);
  }

  /**
   * Verifies that the method
   * <code>{@link ReturnValueTypeHandlerAdvice#afterPropertiesSet()}</code>
   * does not throw any exception if the advice contains a
   * <code>{@link XmlRpcTypeHandlerRegistry}</code> not equal to
   * <code>null</code>.
   */
  public void testAfterPropertiesSetWhenXmlRcpServiceExporterIsNotNull() {
    this.advice.afterPropertiesSet();
  }

  /**
   * Verifies that the method
   * <code>{@link ReturnValueTypeHandlerAdvice#afterPropertiesSet()}</code>
   * throws a <code>{@link BeanCreationException}</code> if the
   * <code>XmlRcpServiceController</code> contains a
   * <code>{@link XmlRpcTypeHandlerRegistry}</code> equal to <code>null</code>.
   */
  public void testAfterPropertiesSetWhenXmlRcpServiceExporterIsEqualToNull() {
    this.advice.setTypeHandlerRegistry(null);
    try {
      this.advice.afterPropertiesSet();
      fail("A 'BeanCreationException' should have been thrown");
    } catch (BeanCreationException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link ReturnValueTypeHandlerAdvice#invoke(MethodInvocation)}</code>
   * converts the return value of the advised method into an instance of any of
   * data types supported by XML-RPC.
   */
  public void testInvoke() throws Throwable {
    MockControl methodInvocationControl = MockControl
        .createControl(MethodInvocation.class);
    MethodInvocation methodInvocation = (MethodInvocation) methodInvocationControl
        .getMock();

    MockControl typeHandlerControl = MockControl
        .createControl(XmlRpcTypeHandler.class);
    XmlRpcTypeHandler typeHandler = (XmlRpcTypeHandler) typeHandlerControl
        .getMock();

    Object proceedReturnValue = new Object();
    Object handledObject = new Object();

    // expectation: call proceed()
    methodInvocation.proceed();
    methodInvocationControl.setReturnValue(proceedReturnValue);

    // expectation: get a type handler.
    this.typeHandlerRegistry.findTypeHandler(proceedReturnValue);
    this.typeHandlerRegistryControl.setReturnValue(typeHandler);

    // expectation: handle the return value of proceed().
    typeHandler.handleType(proceedReturnValue, this.typeHandlerRegistry);
    typeHandlerControl.setReturnValue(handledObject);

    // set the state of the mock objects to "replay".
    methodInvocationControl.replay();
    typeHandlerControl.replay();
    this.typeHandlerRegistryControl.replay();

    // execute the method to test.
    Object actual = this.advice.invoke(methodInvocation);

    assertSame("<Handled object>", handledObject, actual);

    // verify the expectations of the mock objects were met.
    methodInvocationControl.verify();
    typeHandlerControl.verify();
    this.typeHandlerRegistryControl.verify();
  }

}
