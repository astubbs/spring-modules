/* 
 * Created on Jun 13, 2005
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
package org.springmodules.remoting.xmlrpc.util;

import org.apache.commons.codec.binary.Base64;

/**
 * <p>
 * Provides Base64 encoding and decoding as defined by RFC 2045.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/14 00:47:16 $
 */
public class Base64Handler implements ScalarHandler {

  /**
   * Constructor.
   */
  public Base64Handler() {
    super();
  }

  /**
   * @see ScalarHandler#format(Object)
   */
  public String format(Object source) {
    byte[] byteArraySource = (byte[]) source;
    byte[] buffer = Base64.encodeBase64(byteArraySource);
    return new String(buffer, 0, buffer.length);
  }

  /**
   * @see ScalarHandler#parse(String)
   */
  public Object parse(String source) {
    return Base64.decodeBase64(source.getBytes());
  }

}
