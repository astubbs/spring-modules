/* 
 * Created on Apr 6, 2005
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
package org.springmodules.cache.interceptor.caching;

import java.util.List;

/**
 * <p>
 * Interface that simulates a business service.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:19:01 $
 */
public interface SimulatedService {

  /**
   * Returns the name of a person given the id.
   * 
   * @param id
   *          the id of the person.
   * @return the name of a person given the id.
   */
  String getPersonName(long id);

  /**
   * Returns all the persons in the database.
   * 
   * @return all the persons in the database.
   */
  List getPersons();
}
