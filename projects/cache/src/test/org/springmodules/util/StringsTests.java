/* 
 * Created on Sep 6, 2005
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

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link Strings}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class StringsTests extends TestCase {

  public StringsTests(String name) {
    super(name);
  }

  public void testRemoveDuplicatesWithArrayEqualToNull() {
    assertNull(Strings.removeDuplicates(null));
  }

  public void testRemoveDuplicatesWithDuplicatedValues() {
    String[] array = { "str1", "str2", "str1" };
    String[] expected = { "str1", "str2" };
    assertTrue(Arrays.equals(expected, Strings.removeDuplicates(array)));
  }

  public void testRemoveDuplicatesWithEmptyArray() {
    String[] expected = new String[0];
    assertTrue(Arrays.equals(expected, Strings.removeDuplicates(new String[0])));
  }

  public void testRemoveDuplicatesWithoutDuplicatedValues() {
    String[] expected = { "str1", "str2", "str3" };
    assertTrue(Arrays.equals(expected, Strings.removeDuplicates(new String[] {
        "str1", "str2", "str3" })));
  }

}
