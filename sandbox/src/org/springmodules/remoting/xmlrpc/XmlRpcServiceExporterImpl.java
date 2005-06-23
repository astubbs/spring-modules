/* 
 * Created on Jun 8, 2005
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
package org.springmodules.remoting.xmlrpc;

import java.lang.reflect.Method;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationBasedExporter;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElement;
import org.springmodules.remoting.xmlrpc.support.XmlRpcRequest;

/**
 * <p>
 * Default implementation of <code>{@link RemoteInvocationBasedExporter}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/23 01:43:23 $
 */
public final class XmlRpcServiceExporterImpl extends
    RemoteInvocationBasedExporter implements XmlRpcServiceExporter,
    InitializingBean {

  /**
   * Exported service.
   */
  private Object service;

  /**
   * Constructor.
   */
  public XmlRpcServiceExporterImpl() {
    super();
  }

  /**
   * @see InitializingBean#afterPropertiesSet()
   */
  public void afterPropertiesSet() throws Exception {
    this.service = this.getProxyForService();
  }

  /**
   * Returns a <code>{@link RemoteInvocation}</code> encapsulating the method
   * to execute and the arguments to use.
   * 
   * @param xmlRpcRequest
   *          the XML-RPC request.
   * @return a <code>RemoteInvocation</code> created from the XML-RPC request.
   * @throws XmlRpcMethodNotFoundException
   *           if the method specified in the XML-RPC request does not exist.
   */
  protected RemoteInvocation findMatchingMethod(XmlRpcRequest xmlRpcRequest) {
    String requestMethodName = xmlRpcRequest.getMethodName();
    XmlRpcElement[] requestParameters = xmlRpcRequest.getParameters();

    Method targetMethod = null;
    Object[] arguments = null;

    Method[] methods = this.getServiceInterface().getDeclaredMethods();
    int methodCount = methods.length;

    for (int i = 0; i < methodCount; i++) {
      Method method = methods[i];

      if (method.getName().equals(requestMethodName)) {
        Class[] parameterTypes = method.getParameterTypes();
        int parameterTypeCount = parameterTypes.length;

        if (parameterTypeCount != requestParameters.length) {
          continue;
        }

        boolean matchingMethod = true;
        arguments = new Object[parameterTypeCount];

        for (int j = 0; j < parameterTypeCount; j++) {
          XmlRpcElement requestParameter = requestParameters[j];
          Class targetType = parameterTypes[j];
          Object argument = requestParameter.getMatchingValue(targetType);

          if (argument == XmlRpcElement.NOT_MATCHING) {
            matchingMethod = false;
            break;
          }

          arguments[j] = argument;
        }

        if (matchingMethod) {
          targetMethod = method;
          break;
        }
      }
    }

    if (targetMethod == null) {
      throw new XmlRpcMethodNotFoundException("Unable to find method '"
          + requestMethodName + "'");
    }

    RemoteInvocation invocation = new RemoteInvocation(targetMethod.getName(),
        targetMethod.getParameterTypes(), arguments);
    return invocation;
  }

  /**
   * @see XmlRpcServiceExporter#invoke(XmlRpcRequest)
   */
  public RemoteInvocationResult invoke(XmlRpcRequest xmlRpcRequest) {
    RemoteInvocation invocation = this.findMatchingMethod(xmlRpcRequest);
    return this.invokeAndCreateResult(invocation, this.service);
  }
}
