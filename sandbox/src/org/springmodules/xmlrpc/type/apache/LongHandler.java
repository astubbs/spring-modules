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
 * Handles an instance of <code>{@link Long}</code> by converting it to
 * <code>String</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
s * @version $Revision: 1.1 $ $Date: 2005/06/04 01:16:54 $
 */
public final class LongHandler extends AbstractApacheXmlRpcTypeHandler {

  /**
   * Constructor.
   */
  public LongHandler() {
    super();
  }

  /**
   * @see org.springmodules.xmlrpc.type.XmlRpcTypeHandler#handleType(Object,
   *      XmlRpcTypeHandlerRegistry)
   */
  protected Object handle(Object obj, XmlRpcTypeHandlerRegistry registry) {
    return obj.toString();
  }

  /**
   * @see org.springmodules.xmlrpc.type.XmlRpcTypeHandler#getSupportedClass()
   */
  public Class getSupportedClass() {
    return Long.class;
  }

}
