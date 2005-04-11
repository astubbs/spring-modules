/* 
 * Created on Nov 16, 2004
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
 * Copyright @2004 the original author or authors.
 */

package org.springmodules.cache;

import org.springframework.core.NestedRuntimeException;

/**
 * <p>
 * Run-time exception that wraps any instance of <code>Throwable</code> or any
 * of its subclasses thrown when:
 * <ul>
 * <li>Retrieving an entry from the cache</li>
 * <li>Storing an object to the cache</li>
 * <li>Flushing the cache</li>
 * </ul>
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/11 04:02:24 $
 */
public class CacheWrapperException extends NestedRuntimeException {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3761403127492262197L;

  /**
   * Constructor.
   * 
   * @param detailMessage
   *          the detail message.
   * @param nestedException
   *          the nested exception.
   */
  public CacheWrapperException(String detailMessage, Throwable nestedException) {
    super(detailMessage, nestedException);
  }
}