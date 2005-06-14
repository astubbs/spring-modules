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

import org.springmodules.remoting.xmlrpc.XmlRpcParsingException;

/**
 * <p>
 * Parses/formats <code>Double</code> values.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/14 00:47:17 $
 */
public class DoubleHandler extends AbstractNumberHandler {

  /**
   * Constructor.
   */
  public DoubleHandler() {
    super();
  }

  /**
   * @see ScalarHandler#parse(String)
   */
  public Object parse(String source) {
    try {
      return new Double(source);

    } catch (NumberFormatException exception) {
      throw new XmlRpcParsingException(
          "'" + source + "' is not a double value", exception);
    }
  }

}
