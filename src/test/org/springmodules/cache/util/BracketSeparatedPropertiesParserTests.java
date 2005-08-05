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
 * @version $Revision: 1.3 $ $Date: 2005/08/05 02:45:20 $
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
      BracketSeparatedPropertiesParser.addProperty(this.property,
          this.properties);
      fail("Expecting <" + IllegalArgumentException.class.getName() + ">");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  private void assertEqualProperties(Properties expected, Properties actual) {
    assertEquals("<Properties>", expected, actual);
  }

  private void assertParsePropertiesThrowsException() {
    Class expectedException = IllegalArgumentException.class;

    try {
      BracketSeparatedPropertiesParser.parseProperties(this.property);
      fail("Expecting <" + expectedException.getName() + ">");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.properties = new Properties();

    this.propertyKey = "name";
    this.propertyValue = "Luke Skywalker";
    this.property = this.propertyKey + "=" + this.propertyValue;
  }

  public void testAddPropertyWithDuplicatedProperties() {
    this.properties.setProperty(this.propertyKey, this.propertyValue);
    this.assertAddPropertyThrowsException();
  }

  public void testAddPropertyWithInvalidPropertyString() {
    this.property = "XWing";
    this.assertAddPropertyThrowsException();
  }

  public void testAddPropertyWithValidPropertyString() {
    this.properties.setProperty(this.propertyKey, this.propertyValue);
    Properties actualProperties = new Properties();

    BracketSeparatedPropertiesParser.addProperty(this.property,
        actualProperties);
    this.assertEqualProperties(this.properties, actualProperties);
  }

  public void testParsePropertiesWithEmptyString() {
    this.property = "";
    this.assertParsePropertiesThrowsException();
  }

  public void testParsePropertiesWithStringEqualToNull() {
    this.property = null;
    this.assertParsePropertiesThrowsException();
  }

  public void testParsePropertiesWithStringNotEndingWithBrackets() {
    this.property = "[Anakin";
    this.assertParsePropertiesThrowsException();
  }

  public void testParsePropertiesWithStringNotStartingAndNotEndingWithBrackets() {
    this.property = "Anakin";
    this.assertParsePropertiesThrowsException();
  }

  public void testParsePropertiesWithStringNotStartingWithBracket() {
    this.property = "Anakin]";
    this.assertParsePropertiesThrowsException();
  }

  public void testParsePropertiesWithValidPropertiesString() {
    String secondPropertyKey = "role";
    String secondPropertyValue = "Jedi Knight";
    String secondProperty = secondPropertyKey + "=" + secondPropertyValue;

    Properties expectedProperties = new Properties();
    expectedProperties.setProperty(this.propertyKey, this.propertyValue);
    expectedProperties.setProperty(secondPropertyKey, secondPropertyValue);

    String cacheProfileProperties = "[" + this.property + "][" + secondProperty
        + "]";

    Properties actualProperties = BracketSeparatedPropertiesParser
        .parseProperties(cacheProfileProperties);

    this.assertEqualProperties(expectedProperties, actualProperties);
  }
}