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
import org.springmodules.remoting.xmlrpc.support.XmlRpcElement;
import org.springmodules.remoting.xmlrpc.support.XmlRpcRequest;
import org.springmodules.util.Strings;

/**
 * <p>
 * Default implementation of <code>{@link RemoteInvocationBasedExporter}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/09/25 05:20:00 $
 */
public final class XmlRpcServiceExporterImpl extends
    RemoteInvocationBasedExporter implements XmlRpcServiceExporter,
    InitializingBean {

  /**
   * Exported service.
   */
  private Object service;

  public XmlRpcServiceExporterImpl() {
    super();
  }

  /**
   * @see InitializingBean#afterPropertiesSet()
   */
  public void afterPropertiesSet() throws Exception {
    service = getProxyForService();
  }

  /**
   * @param xmlRpcRequest
   *          the XML-RPC request that specifies the method to execute.
   * @return a <code>RemoteInvocation</code> containing the service method
   *         that matches the one specified in the given XML-RPC request.
   * @throws XmlRpcMethodNotFoundException
   *           if the a matching service method cannot be found.
   */
  protected RemoteInvocation findMatchingMethod(XmlRpcRequest xmlRpcRequest) {
    String requestMethodName = xmlRpcRequest.getMethodName();
    XmlRpcElement[] requestParameters = xmlRpcRequest.getParameters();
    int requestParameterCount = requestParameters.length;

    Method targetMethod = null;
    Object[] arguments = null;

    Method[] serviceMethods = getServiceInterface().getDeclaredMethods();
    int serviceMethodCount = serviceMethods.length;

    for (int i = 0; i < serviceMethodCount; i++) {
      Method serviceMethod = serviceMethods[i];

      if (serviceMethod.getName().equals(requestMethodName)) {
        Class[] parameterTypes = serviceMethod.getParameterTypes();
        int parameterTypeCount = parameterTypes.length;

        if (parameterTypeCount != requestParameterCount) {
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
          targetMethod = serviceMethod;
          break;
        }
      }
    }

    if (targetMethod == null) {
      throw new XmlRpcMethodNotFoundException("Unable to find method "
          + Strings.quote(requestMethodName) + "");
    }

    RemoteInvocation invocation = new RemoteInvocation(targetMethod.getName(),
        targetMethod.getParameterTypes(), arguments);
    return invocation;
  }

  /**
   * @see XmlRpcServiceExporter#invoke(XmlRpcRequest)
   * @see #findMatchingMethod(XmlRpcRequest)
   */
  public Object invoke(XmlRpcRequest xmlRpcRequest) throws Exception {
    RemoteInvocation invocation = findMatchingMethod(xmlRpcRequest);
    return invoke(invocation, service);
  }
}
