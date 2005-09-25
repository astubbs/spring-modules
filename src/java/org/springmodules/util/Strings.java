/* 
 * Created on Sep 5, 2005
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
package org.springmodules.util;

/**
 * <p>
 * Utility methods for Strings.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public abstract class Strings {

  public static String quote(String str) {
    return (str != null) ? "'" + str + "'" : str;
  }

  public static Object quoteIfString(Object obj) {
    return (obj instanceof String) ? quote((String) obj) : obj;
  } 
}
