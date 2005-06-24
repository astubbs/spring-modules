/* 
 * Created on Jun 23, 2005
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
package org.springmodules.remoting.xmlrpc.dom;

import org.springmodules.remoting.xmlrpc.AbstractXmlRpcResponseWriterTests;
import org.springmodules.remoting.xmlrpc.XmlRpcResponseWriter;

/**
 * <p>
 * Unit Tests for <code>{@link DomXmlRpcResponseWriter}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/24 11:59:53 $
 */
public class DomXmlRpcResponseWriterTests extends
    AbstractXmlRpcResponseWriterTests {

  /**
   * Primar object that is under test.
   */
  private DomXmlRpcResponseWriter writer;
  
  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public DomXmlRpcResponseWriterTests(String name) {
    super(name);
  }

  /**
   * @see AbstractXmlRpcResponseWriterTests#getXmlRpcResponseWriterImplementation()
   */
  protected XmlRpcResponseWriter getXmlRpcResponseWriterImplementation() {
    return this.writer;
  }

  /**
   * @see AbstractXmlRpcResponseWriterTests#onSetUp()
   */
  protected void onSetUp() throws Exception {
    super.onSetUp();
    
    this.writer = new DomXmlRpcResponseWriter();
  }

  
}
