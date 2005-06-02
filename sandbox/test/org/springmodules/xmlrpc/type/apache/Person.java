/* 
 * Created on Jun 2, 2005
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
package org.springmodules.xmlrpc.type.apache;

import java.io.Serializable;

/**
 * <p>
 * Abstraction of a person.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 23:31:46 $
 */
public class Person implements Serializable {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3256727268767642424L;

  /**
   * The age of this person.
   */
  private int age;

  /**
   * The first name of this person.
   */
  private String firstName;

  /**
   * The last name of this person.
   */
  private String lastName;

  /**
   * Constructor.
   */
  public Person() {
    super();
  }

  /**
   * Getter for field <code>{@link #age}</code>.
   * 
   * @return the field <code>age</code>.
   */
  public final int getAge() {
    return this.age;
  }

  /**
   * Getter for field <code>{@link #firstName}</code>.
   * 
   * @return the field <code>firstName</code>.
   */
  public final String getFirstName() {
    return this.firstName;
  }

  /**
   * Getter for field <code>{@link #lastName}</code>.
   * 
   * @return the field <code>lastName</code>.
   */
  public final String getLastName() {
    return this.lastName;
  }

  /**
   * Setter for the field <code>{@link #age}</code>.
   * 
   * @param age
   *          the new value to set.
   */
  public final void setAge(int age) {
    this.age = age;
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

}
