/* 
 * Created on Jun 10, 2005
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
 * @version $Revision: 1.1 $ $Date: 2005/06/13 08:54:08 $
 */
public class XmlRpcBase64 {

  /**
   * Decodes a String containing Base64 data into octects.
   * 
   * @param source
   *          String containing Base64 data.
   * @return an array of <code>byte</code>s containing decoded data.
   */
  public static byte[] toByteArray(String source) {
    return Base64.decodeBase64(source.getBytes());
  }

  /**
   * Encodes binary data using the base64 algorithm but does not chunk the
   * output.
   * 
   * @param source
   *          binary data to encode.
   * @return Base64 characters.
   */
  public static String toString(byte[] source) {
    byte[] buffer = Base64.encodeBase64(source);
    return new String(buffer, 0, buffer.length);
  }

  /**
   * Constructor.
   */
  public XmlRpcBase64() {
    super();
  }

}
