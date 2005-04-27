/* 
 * Created on Jan 7, 2005
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

package org.springmodules.cache.regex;

/**
 * <p>
 * Unchecked exception thrown to indicate a syntax error in a regular expression
 * pattern.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/04/27 01:41:24 $
 */
public class PatternInvalidSyntaxException extends RuntimeException {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3833182532136219191L;

  /**
   * Constructor.
   * 
   * @param message
   *          the detail message of the exception.
   */
  public PatternInvalidSyntaxException(String message) {
    super(message);
  }
}