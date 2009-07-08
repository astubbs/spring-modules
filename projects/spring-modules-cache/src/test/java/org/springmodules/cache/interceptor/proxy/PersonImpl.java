/* 
 * Created on Jul 17, 2005
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
package org.springmodules.cache.interceptor.proxy;

import org.springmodules.util.Objects;

/**
 * <p>
 * Default implementation of <code>{@link Person}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class PersonImpl implements Person {

  private static final long serialVersionUID = 5359948798614362318L;

  /**
   * First name of this person.
   */
  private String firstName;

  /**
   * Last name of this person.
   */
  private String lastName;

  public PersonImpl() {
    super();
  }

  public PersonImpl(String firstName, String lastName) {
    super();
    setFirstName(firstName);
    setLastName(lastName);
  }

  /**
   * @see Person#getFirstName()
   */
  public final String getFirstName() {
    return this.firstName;
  }

  /**
   * @see Person#getLastName()
   */
  public final String getLastName() {
    return this.lastName;
  }

  public final void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public final void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    StringBuffer buffer = Objects.identityToString(this);
    buffer.append("[firstName='" + this.firstName + "', ");
    buffer.append("lastName='" + this.lastName + "']");

    return buffer.toString();
  }
}
