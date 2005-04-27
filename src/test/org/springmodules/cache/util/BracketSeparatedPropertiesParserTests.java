/* 
 * Created on Jan 18, 2005
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

import java.util.Properties;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Test for <code>{@link BracketSeparatedPropertiesParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:42:21 $
 */
public final class BracketSeparatedPropertiesParserTests extends TestCase {

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public BracketSeparatedPropertiesParserTests(String name) {
    super(name);
  }

  /**
   * Verifies that the method
   * <code>{@link BracketSeparatedPropertiesParser#addProperty(String, Properties)}</code>
   * throws an <code>IllegalArgumentException</code> if we are trying to add
   * an existing property to the set of properties.
   */
  public void testAddPropertyWithDuplicatedProperties() {
    Properties properties = new Properties();
    properties.setProperty("firstName", "James Gosling");

    String property = "firstName=James Gosling";

    try {
      BracketSeparatedPropertiesParser.addProperty(property, properties);
      fail("A 'IllegalArgumentException' should have been thrown");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link BracketSeparatedPropertiesParser#addProperty(String, Properties)}</code>
   * throws an <code>IllegalArgumentException</code> if the specified String
   * does not match the pattern 'name=value'.
   */
  public void testAddPropertyWithInvalidPropertyString() {
    Properties actualProperties = new Properties();
    String property = "Smile :)";

    try {
      BracketSeparatedPropertiesParser.addProperty(property, actualProperties);
      fail("A 'IllegalArgumentException' should have been thrown");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link BracketSeparatedPropertiesParser#addProperty(String, Properties)}</code>
   * adds a new property to the <code>java.util.Properties</code> if the
   * specified String matches the pattern 'name=value'.
   */
  public void testAddPropertyWithValidPropertyString() {

    Properties expectedProperties = new Properties();
    expectedProperties.setProperty("firstName", "James Gosling");

    Properties actualProperties = new Properties();
    String property = "firstName=James Gosling";

    BracketSeparatedPropertiesParser.addProperty(property, actualProperties);

    assertEquals("<Properties>", expectedProperties, actualProperties);
  }

  /**
   * Verifies that the method
   * <code>{@link BracketSeparatedPropertiesParser#parseProperties(String)}</code>
   * throws an <code>IllegalArgumentException</code> if the specified String
   * is empty.
   */
  public void testParsePropertiesWithEmptyString() {
    try {
      BracketSeparatedPropertiesParser.parseProperties("");
      fail("A 'IllegalArgumentException' should have been thrown");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link BracketSeparatedPropertiesParser#parseProperties(String)}</code>
   * throws an <code>IllegalArgumentException</code> if the specified String
   * is <code>null</code>.
   */
  public void testParsePropertiesWithStringEqualToNull() {
    try {
      BracketSeparatedPropertiesParser.parseProperties(null);
      fail("A 'IllegalArgumentException' should have been thrown");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link BracketSeparatedPropertiesParser#parseProperties(String)}</code>
   * throws an <code>IllegalArgumentException</code> if the specified String
   * does not end with a bracket.
   */
  public void testParsePropertiesWithStringNotEndingWithBrackets() {
    try {
      BracketSeparatedPropertiesParser.parseProperties("[A String!");
      fail("A 'IllegalArgumentException' should have been thrown");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link BracketSeparatedPropertiesParser#parseProperties(String)}</code>
   * throws an <code>IllegalArgumentException</code> if the specified String
   * does not start and end with a pair of brackets.
   */
  public void testParsePropertiesWithStringNotStartingAndNotEndingWithBrackets() {
    try {
      BracketSeparatedPropertiesParser.parseProperties("A String!");
      fail("A 'IllegalArgumentException' should have been thrown");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Tests
   * <code>{@link BracketSeparatedPropertiesParser#parseProperties(String)}</code>.
   * Verifies that an <code>IllegalArgumentException</code> is thrown if the
   * specified String does not start with a bracket.
   */
  public void testParsePropertiesWithStringNotStartingWithBracket() {

    IllegalArgumentException catched = null;

    try {
      BracketSeparatedPropertiesParser.parseProperties("A String!]");
    } catch (IllegalArgumentException exception) {
      catched = exception;
    }

    assertNotNull("An IllegalArgumentException should have been catched",
        catched);
  }

  /**
   * Tests
   * <code>{@link BracketSeparatedPropertiesParser#parseProperties(String)}</code>.
   */
  public void testParsePropertiesWithValidPropertiesString() {

    Properties expectedProperties = new Properties();
    expectedProperties.setProperty("cacheName", "myCache");
    expectedProperties.setProperty("cacheGroup", "main");

    String cacheProfileProperties = "[cacheName=myCache][cacheGroup=main]";

    Properties actualProperties = BracketSeparatedPropertiesParser
        .parseProperties(cacheProfileProperties);

    assertEquals("<Properties>", expectedProperties, actualProperties);
  }
}