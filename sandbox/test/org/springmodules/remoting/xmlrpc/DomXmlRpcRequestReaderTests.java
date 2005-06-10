/* 
 * Created on Jun 6, 2005
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
 * Unit Tests for <code>{@link DomXmlRpcRequestReader}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/10 01:49:29 $
 */
public class DomXmlRpcRequestReaderTests extends
    AbstractXmlRpcRequestReaderTests {

  /**
   * Primary object that is under test.
   */
  private DomXmlRpcRequestReader requestReader;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public DomXmlRpcRequestReaderTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void onSetUp() throws Exception {
    super.onSetUp();

    this.requestReader = new DomXmlRpcRequestReader();
  }

  /**
   * @see AbstractXmlRpcRequestReaderTests#getXmlRpcRequestReader()
   */
  protected XmlRpcRequestReader getXmlRpcRequestReader() {
    return this.requestReader;
  }
}
