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
package org.springmodules.xmlrpc.serializer;

/**
 * <p>
 * Template for XML-RPC Serializers.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 00:27:19 $
 */
public abstract class AbstractXmlRpcSerializer implements XmlRpcSerializer {

  /**
   * Constructor.
   */
  public AbstractXmlRpcSerializer() {
    super();
  }

  /**
   * @see XmlRpcSerializer#isSupported(Class)
   */
  public final boolean isSupported(Class clazz) {
    return this.getSupportedClass().isAssignableFrom(clazz);
  }

}
