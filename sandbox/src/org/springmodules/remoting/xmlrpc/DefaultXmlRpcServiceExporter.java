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
import org.springframework.remoting.support.RemoteExporter;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElement;
import org.springmodules.remoting.xmlrpc.support.XmlRpcRequest;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/17 09:57:50 $
 */
public class DefaultXmlRpcServiceExporter extends RemoteExporter implements
    XmlRpcServiceExporter, InitializingBean {

  /**
   * Service being exported.
   */
  private Object service;

  /**
   * Constructor.
   */
  public DefaultXmlRpcServiceExporter() {
    super();
  }

  /**
   * @see InitializingBean#afterPropertiesSet()
   */
  public void afterPropertiesSet() throws Exception {
    this.service = this.getProxyForService();
  }

  /**
   * @see XmlRpcServiceExporter#invoke(XmlRpcRequest)
   */
  public RemoteInvocationResult invoke(XmlRpcRequest xmlRpcRequest) {
    Method targetMethod = null;

    Method[] methods = this.getServiceInterface().getDeclaredMethods();
    int methodCount = methods.length;

    Object[] arguments = null;
    
    for (int i = 0; i < methodCount; i++) {
      Method method = methods[i];

      if (method.getName().equals(xmlRpcRequest.getMethodName())) {
        XmlRpcElement[] requestParameters = xmlRpcRequest.getParameters();

        Class[] parameterTypes = method.getParameterTypes();
        int parameterTypeCount = parameterTypes.length;

        boolean matchingMethod = false;
        
        if (parameterTypeCount != requestParameters.length) {
          continue;
        }
        
        matchingMethod = true;
        arguments = new Object[parameterTypeCount];
        
        for (int j = 0; j < parameterTypeCount; j++) {
          Object argument = requestParameters[j]
              .getMatchingValue(parameterTypes[j]);
          
          if (argument == XmlRpcElement.NOT_MATCHING) {
            matchingMethod = false;
            break;
          }
        }
        
        if (matchingMethod) {
          targetMethod = method;
          break;
        }
      }
    }

    if (targetMethod == null) {
      
    }
    else {
      
    }
    
    return null;
  }

}
