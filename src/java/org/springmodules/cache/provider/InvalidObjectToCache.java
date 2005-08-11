/* 
 * Created on Aug 2, 2005
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
package org.springmodules.cache.provider;

/**
 * <p>
 * Exception thrown when an invalid object is going to be stored in a cache.
 * Each cache provider should determine which objects should be considered
 * invalid.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class InvalidObjectToCache extends CacheException {

  private static final long serialVersionUID = -9124262814399202014L;

  public InvalidObjectToCache(String detailMessage) {
    super(detailMessage);
  }

  public InvalidObjectToCache(String detailMessage, Throwable nestedException) {
    super(detailMessage, nestedException);
  }

}
