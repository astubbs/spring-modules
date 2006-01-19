/* 
 * Created on Aug 11, 2005
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
package org.springmodules.cache.serializable;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springmodules.util.Objects;

/**
 * <p>
 * Abstraction of a pet.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class Puppy {

  private String name;

  public Puppy() {
    super();
  }

  public Puppy(String name) {
    super();
    setName(name);
  }

  /**
   * @see Object#equals(Object)
   */
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (!(obj instanceof Puppy)) {
      return false;
    }

    Puppy puppy = (Puppy) obj;
    if (!ObjectUtils.nullSafeEquals(name, puppy.name)) {
      return false;
    }

    return true;
  }

  public final String getName() {
    return name;
  }

  /**
   * @see Object#hashCode()
   */
  public int hashCode() {
    int multiplier = 31;
    int hash = 17;

    hash = multiplier * hash + Objects.nullSafeHashCode(name);

    return hash;
  }

  public final void setName(String newName) {
    name = newName;
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    StringBuffer buffer = Objects.identityToString(this);
    buffer.append("[name=" + StringUtils.quote(name) + "]");
    return buffer.toString();
  }

}
