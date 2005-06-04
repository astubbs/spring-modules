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

/**
 * <p>
 * Handles an instance of <code>{@link Short}</code> by converting it to
 * <code>{@link Integer}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/04 01:16:53 $
 */
public final class ShortHandler extends AbstractApacheXmlRpcTypeHandler {

  /**
   * Constructor.
   */
  public ShortHandler() {
    super();
  }

  /**
   * @see org.springmodules.xmlrpc.type.XmlRpcTypeHandler#handleType(Object,
   *      XmlRpcTypeHandlerRegistry)
   */
  protected Object handle(Object obj, XmlRpcTypeHandlerRegistry registry) {
    Short shortObj = (Short) obj;
    return new Integer(shortObj.shortValue());
  }

  /**
   * @see org.springmodules.xmlrpc.type.XmlRpcTypeHandler#getSupportedClass()
   */
  public Class getSupportedClass() {
    return Short.class;
  }

}
