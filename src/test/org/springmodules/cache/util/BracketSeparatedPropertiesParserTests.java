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
 * Unit Tests for <code>{@link BracketSeparatedPropertiesParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/09/09 02:19:26 $
 */
public final class BracketSeparatedPropertiesParserTests extends TestCase {

  private Properties properties;

  private String property;

  private String propertyKey;

  private String propertyValue;

  public BracketSeparatedPropertiesParserTests(String name) {
    super(name);
  }

  private void assertAddPropertyThrowsException() {
    try {
      BracketSeparatedPropertiesParser.addProperty(property,
          properties);
      fail();
      
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  private void assertParsePropertiesThrowsException() {
    try {
      BracketSeparatedPropertiesParser.parseProperties(property);
      fail();
      
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  protected void setUp() throws Exception {
    super.setUp();

    properties = new Properties();

    propertyKey = "name";
    propertyValue = "Luke Skywalker";
    property = propertyKey + "=" + propertyValue;
  }

  public void testAddPropertyWithDuplicatedProperties() {
    properties.setProperty(propertyKey, propertyValue);
    assertAddPropertyThrowsException();
  }

  public void testAddPropertyWithInvalidPropertyString() {
    property = "XWing";
    assertAddPropertyThrowsException();
  }

  public void testAddPropertyWithValidPropertyString() {
    properties.setProperty(propertyKey, propertyValue);
    Properties actualProperties = new Properties();

    BracketSeparatedPropertiesParser.addProperty(property,
        actualProperties);
    assertEquals(properties, actualProperties);
  }

  public void testParsePropertiesWithEmptyString() {
    property = "";
    assertParsePropertiesThrowsException();
  }

  public void testParsePropertiesWithStringEqualToNull() {
    property = null;
    assertParsePropertiesThrowsException();
  }

  public void testParsePropertiesWithStringNotEndingWithBrackets() {
    property = "[Anakin";
    assertParsePropertiesThrowsException();
  }

  public void testParsePropertiesWithStringNotStartingAndNotEndingWithBrackets() {
    property = "Anakin";
    assertParsePropertiesThrowsException();
  }

  public void testParsePropertiesWithStringNotStartingWithBracket() {
    property = "Anakin]";
    assertParsePropertiesThrowsException();
  }

  public void testParsePropertiesWithValidPropertiesString() {
    String secondPropertyKey = "role";
    String secondPropertyValue = "Jedi Knight";
    String secondProperty = secondPropertyKey + "=" + secondPropertyValue;

    Properties expectedProperties = new Properties();
    expectedProperties.setProperty(propertyKey, propertyValue);
    expectedProperties.setProperty(secondPropertyKey, secondPropertyValue);

    String cacheProfileProperties = "[" + property + "][" + secondProperty
        + "]";

    Properties actualProperties = BracketSeparatedPropertiesParser
        .parseProperties(cacheProfileProperties);

    assertEquals(expectedProperties, actualProperties);
  }
}