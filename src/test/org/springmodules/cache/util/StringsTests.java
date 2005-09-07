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
package org.springmodules.cache.util;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link Strings}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class StringsTests extends TestCase {

  public StringsTests(String name) {
    super(name);
  }

  public void testQuote() {
    String str = "Leia";
    assertEquals("'" + str + "'", Strings.quote(str));
  }

  public void testQuoteIfStringWithNotStringArg() {
    Integer number = new Integer(3);
    assertEquals(number, Strings.quoteIfString(number));
  }

  public void testQuoteIfStringWithStringArg() {
    String str = "Luke";
    assertEquals("'" + str + "'", Strings.quoteIfString(str));
  }

  public void testQuoteIfStringWithStringEqualToNull() {
    assertNull(Strings.quoteIfString(null));
  }

  public void testQuoteWithStringEqualToNull() {
    assertNull(Strings.quote(null));
  }
}
