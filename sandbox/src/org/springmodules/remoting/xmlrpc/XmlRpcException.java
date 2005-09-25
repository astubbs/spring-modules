/* 
 * Created on Jun 4, 2005
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

import org.springframework.core.NestedRuntimeException;

/**
 * <p>
 * Abstract superclass for all exceptions thrown in the XML-RPC module.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/09/25 05:20:00 $
 */
public abstract class XmlRpcException extends NestedRuntimeException {

  public XmlRpcException(String msg) {
    super(msg);
  }

  public XmlRpcException(String msg, Throwable nestedException) {
    super(msg, nestedException);
  }

  public abstract int getCode();
}
