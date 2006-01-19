/* 
 * Created on Aug 22, 2005
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
package org.springmodules.cache.regex;

import junit.framework.TestCase;

/**
 * <p>
 * Test cases for implementations of <code>{@link Regex}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractRegexTestCases extends TestCase {

  public AbstractRegexTestCases() {
    super();
  }

  public AbstractRegexTestCases(String name) {
    super(name);
  }

  public final void testMatchesStringStringWithInvalidRegularExpression() {
    String regularExpression = "[kkk";

    try {
      getRegexToTest(regularExpression);
      fail();

    } catch (PatternInvalidSyntaxException exception) {
      // we are expecting this exception
    }
  }

  public final void testMatchWithMatchingInputAndGrouping() {
    String pattern = "(ab)c.*";
    Regex regex = getRegexToTest(pattern);

    String input = "abc";
    Match match = regex.match(input);

    assertInputMatchesPattern(input, pattern, match);

    String[] expectedGroups = { input, "ab" };
    assertEqualGroups(expectedGroups, match.getGroups());
  }

  /**
   * Tests the method <code>{@link Regex#match(String)}</code> with a matching
   * input sequence and a regular expression that does not specify grouping.
   */
  public final void testMatchWithMatchingInputAndWithoutGrouping() {
    String pattern = "abc.*";
    Regex regex = getRegexToTest(pattern);

    String input = "abc";
    Match match = regex.match(input);

    assertInputMatchesPattern(input, pattern, match);

    String[] expectedGroups = { input };
    assertEqualGroups(expectedGroups, match.getGroups());
  }

  /**
   * Tests the method <code>{@link Regex#match(String)}</code> with a input
   * sequence that does not match the given regular expression.
   */
  public final void testMatchWithNotMatchingInput() {
    String regularExpression = "(ab)cz.*";
    Regex regex = getRegexToTest(regularExpression);

    String input = "xyz";
    Match match = regex.match(input);

    assertFalse("The input '" + input
        + "' should not match the regular expression '" + regularExpression
        + "'", match.isSuccessful());

    assertNull("There should not be any groups", match.getGroups());
  }

  protected final void assertEqualGroups(String[] expected, String[] actual) {
    int groupCount = expected.length;
    assertEquals("<Group count>", groupCount, actual.length);

    for (int i = 0; i < groupCount; i++) {
      assertEquals("<Group " + i + ">", expected[i], actual[i]);
    }
  }

  protected final void assertInputMatchesPattern(String input, String pattern,
      Match match) {
    assertTrue("The input '" + input
        + "' should match the regular expression '" + pattern + "'", match
        .isSuccessful());
  }

  /**
   * Returns an instance of the implementation of <code>{@link Regex}</code>
   * to test.
   * 
   * @param pattern
   *          the regular expression pattern to compile.
   * @return the compiled regular expression to test.
   */
  protected abstract Regex getRegexToTest(String pattern);
}