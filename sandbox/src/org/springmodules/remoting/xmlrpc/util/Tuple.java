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

/**
 * <p>
 * A tuple is a pair of objects accessed by first and second, respectively.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/17 09:57:45 $
 */
public class Tuple {

  /**
   * The first value of this tuple.
   */
  public final Object first;

  /**
   * The second value of this tuple.
   */
  public final Object second;

  /**
   * Constructor.
   * 
   * @param first
   *          the new first value of this tuple.
   * @param second
   *          the new second value of this tuple.
   */
  public Tuple(Object first, Object second) {
    super();
    this.first = first;
    this.second = second;
  }

}
