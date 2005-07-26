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

/**
 * <p>
 * Default implementation of <code>{@link Person}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class PersonImpl implements Person {

  /**
   * Version of this class.
   */
  private static final long serialVersionUID = 5359948798614362318L;

  /**
   * First name of this person.
   */
  private String firstName;

  /**
   * Last name of this person.
   */
  private String lastName;

  /**
   * Constructor.
   */
  public PersonImpl() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param firstName
   *          the first name of this person.
   * @param lastName
   *          the last name of this person.
   */
  public PersonImpl(String firstName, String lastName) {
    super();
    this.firstName = firstName;
    this.lastName = lastName;
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

  /**
   * Setter for the field <code>{@link #firstName}</code>.
   * 
   * @param firstName
   *          the new value to set.
   */
  public final void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Setter for the field <code>{@link #lastName}</code>.
   * 
   * @param lastName
   *          the new value to set.
   */
  public final void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(this.getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("firstName='" + this.firstName + "', ");
    buffer.append("lastName='" + this.lastName + "']");

    return buffer.toString();
  }
}
