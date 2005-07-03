/* 
 * Created on Jun 20, 2005
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
package org.springmodules.remoting.xmlrpc.support;

import java.io.Serializable;

/**
 * <p>
 * Abstraction of a person.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/07/03 14:11:39 $
 */
public class Person implements Serializable {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3256718502789133874L;

  /**
   * The id of this person.
   */
  private Integer id;

  /**
   * The name of this person.
   */
  private String name;

  /**
   * Constructor.
   */
  public Person() {
    super();
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * 
   * @param obj
   *          the reference object with which to compare
   * @return <code>true</code> if this object is the same as the obj argument;
   *         <code>false</code> otherwise.
   * 
   * @see Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Person)) {
      return false;
    }

    final Person person = (Person) obj;

    if (this.id != null ? !this.id.equals(person.id) : person.id != null) {
      return false;
    }
    if (this.name != null ? !this.name.equals(person.name)
        : person.name != null) {
      return false;
    }

    return true;
  }

  /**
   * Getter for field <code>{@link #id}</code>.
   * 
   * @return the field <code>id</code>.
   */
  public final Integer getId() {
    return this.id;
  }

  /**
   * Getter for field <code>{@link #name}</code>.
   * 
   * @return the field <code>name</code>.
   */
  public final String getName() {
    return this.name;
  }

  /**
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   * 
   * @return a hash code value for this object.
   * 
   * @see Object#hashCode()
   */
  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + (this.id != null ? this.id.hashCode() : 0);
    hash = multiplier * hash + (this.name != null ? this.name.hashCode() : 0);
    return hash;
  }

  /**
   * Setter for the field <code>{@link #id}</code>.
   * 
   * @param id
   *          the new value to set.
   */
  public final void setId(Integer id) {
    this.id = id;
  }

  /**
   * Setter for the field <code>{@link #name}</code>.
   * 
   * @param name
   *          the new value to set.
   */
  public final void setName(String name) {
    this.name = name;
  }

  /**
   * Returns a string representation of the object. In general, the
   * <code>toString</code> method returns a string that "textually represents"
   * this object.
   * 
   * @return a string representation of the object.
   * 
   * @see Object#toString()
   */
  public String toString() {
    return "Person: id=" + this.id + ", name='" + this.name + "'";
  }
}
