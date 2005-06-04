/* 
 * Created on May 31, 2005
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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>
 * Advice that handles the conversion of the return type of the advised method
 * to a data type supported by XML-RPC.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/04 01:19:03 $
 */
public final class ReturnValueTypeHandlerAdvice implements MethodInterceptor,
    InitializingBean {

  /**
   * Registry containing the serializers to use.
   */
  private XmlRpcTypeHandlerRegistry typeHandlerRegistry;

  /**
   * Constructor.
   */
  public ReturnValueTypeHandlerAdvice() {
    super();
  }

  /**
   * Validates that the properties of this advice has been properly set.
   * 
   * @throws BeanCreationException
   *           if the property '<code>typeHandlerRegistry</code>' is
   *           <code>null</code>.
   * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  public void afterPropertiesSet() {
    if (this.typeHandlerRegistry == null) {
      throw new BeanCreationException(
          "The property 'typeHandlerRegistry' should not be null");
    }
  }

  /**
   * Handles the conversion of the return type of the advised method to a data
   * type supported by XML-RPC.
   * 
   * @see MethodInterceptor#invoke(MethodInvocation)
   */
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {
    Object proceedReturnValue = methodInvocation.proceed();
    XmlRpcTypeHandler typeHandler = this.typeHandlerRegistry
        .findTypeHandler(proceedReturnValue);
    return typeHandler.handleType(proceedReturnValue, this.typeHandlerRegistry);
  }

  /**
   * Setter for the field <code>{@link #typeHandlerRegistry}</code>.
   * 
   * @param typeHandlerRegistry
   *          the new value to set.
   */
  public void setTypeHandlerRegistry(
      XmlRpcTypeHandlerRegistry typeHandlerRegistry) {
    this.typeHandlerRegistry = typeHandlerRegistry;
  }
}
