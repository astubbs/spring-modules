/* 
 * Created on Nov 19, 2005
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

import java.util.HashMap;
import java.util.Map;

import org.springmodules.util.HashCodeBuilder;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link HashCodeBuilder}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class HashCodeBuilderTests extends TestCase {

  private class Person {

    private String firstName;

    private long id;

    private String lastName;

    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof Person)) {
        return false;
      }

      Person person = (Person) obj;

      if (id != person.id) {
        return false;
      }
      if (firstName != null ? !firstName.equals(person.firstName)
          : person.firstName != null) {
        return false;
      }

      if (lastName != null ? !lastName.equals(person.lastName)
          : person.lastName != null) {
        return false;
      }

      return true;
    }

    public final String getFirstName() {
      return firstName;
    }

    public final long getId() {
      return id;
    }

    public final String getLastName() {
      return lastName;
    }

    public int hashCode() {
      int multiplier = 31;
      int hash = 17;

      int offset = 32;
      hash = multiplier * hash + ((int) (id ^ (id >> offset)));
      hash = multiplier * hash + (firstName != null ? firstName.hashCode() : 0);
      hash = multiplier * hash + (lastName != null ? lastName.hashCode() : 0);

      return hash;
    }

    public final void setFirstName(String newFirstName) {
      firstName = newFirstName;
    }

    public final void setId(long newId) {
      id = newId;
    }

    public final void setLastName(String newLastName) {
      lastName = newLastName;
    }
  }

  public HashCodeBuilderTests(String name) {
    super(name);
  }

  private void assertEqualReflectionHashCode(Object obj1, Object obj2) {
    int hashCode1 = HashCodeBuilder.reflectionHashCode(obj1);
    int hashCode2 = HashCodeBuilder.reflectionHashCode(obj2);

    assertEquals(hashCode1, hashCode2);
  }

  private void assertNotEqualReflectionHashCode(Object obj1, Object obj2) {
    int hashCode1 = HashCodeBuilder.reflectionHashCode(obj1);
    int hashCode2 = HashCodeBuilder.reflectionHashCode(obj2);

    assertTrue(hashCode1 != hashCode2);
  }

  private Person createDefaultPerson() {
    Person person = new Person();
    person.setFirstName("Luke");
    person.setId(865);
    person.setLastName("Skywalker");
    return person;
  }

  public void testHashCode() {
    Person person = createDefaultPerson();
    assertEquals(person.hashCode(), HashCodeBuilder.hashCode(person));
  }

  public void testHashCodeWithNullObject() {
    assertEquals(0, HashCodeBuilder.hashCode(null));
  }

  public void testReflecionHashCodeWithMapsHavingEqualEntries() {
    Map map1 = new HashMap();
    map1.put("jedi", createDefaultPerson());
    map1.put("null", null);

    Map map2 = new HashMap();
    map2.put("jedi", createDefaultPerson());
    map2.put("null", null);

    assertEqualReflectionHashCode(map1, map2);
  }

  public void testReflecionHashCodeWithMapsHavingNotEqualEntries() {
    Map map1 = new HashMap();
    map1.put("jedi", createDefaultPerson());

    Person person2 = new Person();
    person2.setFirstName("Darth");
    person2.setId(5353);
    person2.setLastName("Vader");

    Map map2 = new HashMap();
    map2.put("sith", person2);

    assertNotEqualReflectionHashCode(map1, map2);
  }

  public void testReflectionHashCodeWithEqualObjects() {
    Person person1 = createDefaultPerson();
    Person person2 = createDefaultPerson();

    assertEquals(person1, person2);
    assertEqualReflectionHashCode(person1, person2);
  }

  public void testReflectionHashCodeWithNotEqualObjects() {
    Person person1 = createDefaultPerson();

    Person person2 = new Person();
    person2.setFirstName("Leia");
    person2.setId(98);
    person2.setLastName("Organa");

    assertFalse(person1.equals(person2));
    assertNotEqualReflectionHashCode(person1, person2);
  }

  public void testReflectionHashCodeWithNullObject() {
    assertEquals(0, HashCodeBuilder.reflectionHashCode(null));
  }
}
