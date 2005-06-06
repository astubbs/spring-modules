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

/**
 * <p>
 * Exception thrown when the parser of the XML-RPC request/response encounters
 * an internal error.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/06 02:13:57 $
 */
public class XmlRpcParsingException extends XmlRpcException {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3761406417336546864L;

  /**
   * Constructor.
   * 
   * @param msg
   *          the detail message.
   */
  public XmlRpcParsingException(String msg) {
    super(msg);
  }

  /**
   * Constructor.
   * 
   * @param msg
   *          the detail message.
   * @param nestedException
   *          the nested exception.
   */
  public XmlRpcParsingException(String msg, Throwable nestedException) {
    super(msg, nestedException);
  }

}
