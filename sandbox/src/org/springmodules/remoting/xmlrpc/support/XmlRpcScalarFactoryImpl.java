/* 
 * Created on Jun 20, 2005
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
package org.springmodules.remoting.xmlrpc.support;

import org.springmodules.remoting.xmlrpc.XmlRpcElementNames;
import org.springmodules.remoting.xmlrpc.XmlRpcParsingException;

/**
 * <p>
 * Default implementation of <code>{@link XmlRpcScalarFactory}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/21 01:03:32 $
 */
public class XmlRpcScalarFactoryImpl implements XmlRpcScalarFactory {

  /**
   * Constructor.
   */
  public XmlRpcScalarFactoryImpl() {
    super();
  }

  /**
   * @see XmlRpcScalarFactory#createScalarValue(String, String)
   */
  public XmlRpcScalar createScalarValue(String elementName, String value) {
    if (XmlRpcElementNames.BASE_64.equals(elementName)) {
      return new XmlRpcBase64(value);

    } else if (XmlRpcElementNames.BOOLEAN.equals(elementName)) {
      return new XmlRpcBoolean(value);

    } else if (XmlRpcElementNames.DATE_TIME.equals(elementName)) {
      return new XmlRpcDateTime(value);

    } else if (XmlRpcElementNames.DOUBLE.equals(elementName)) {
      return new XmlRpcDouble(value);

    } else if (XmlRpcElementNames.I4.equals(elementName)
        || XmlRpcElementNames.INT.equals(elementName)) {
      return new XmlRpcInteger(value);

    } else if (XmlRpcElementNames.STRING.equals(elementName)) {
      return new XmlRpcString(value);

    }

    throw new XmlRpcParsingException("Unexpected element '" + elementName
        + "'");
  }

}
