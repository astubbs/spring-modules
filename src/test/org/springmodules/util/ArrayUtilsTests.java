/* 
 * Created on Aug 4, 2005
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

import org.springmodules.util.ArrayUtils;
import org.springmodules.util.Strings;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link ArrayUtils}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class ArrayUtilsTests extends TestCase {

  public ArrayUtilsTests(String name) {
    super(name);
  }

  public void testToStringWithNotEmptyStringArray() {
    String[] array = { "Luke", "Han", "Leia" };
    StringBuffer buffer = new StringBuffer();

    int elementCount = array.length;
    for (int i = 0; i < elementCount; i++) {
      if (i == 0)
        buffer.append('{');
      else
        buffer.append(", ");

      buffer.append(Strings.quote(array[i]));
    }

    buffer.append("}");
    
    assertEquals(buffer.toString(), ArrayUtils.toString(array));
  }

  public void testToStringWithEmptyStringArray() {
    assertEquals("{}", ArrayUtils.toString(new String[0]));
  }

  public void testToStringWithStringArrayEqualToNull() {
    assertEquals("null", ArrayUtils.toString((String[]) null));
  }
}
