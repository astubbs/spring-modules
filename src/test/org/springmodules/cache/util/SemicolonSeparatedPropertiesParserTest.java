/* 
 * Created on Oct 10, 2005
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

import java.util.Iterator;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link SemicolonSeparatedPropertiesParser}</code>
 * </p>
 * 
 * @author Alex Ruiz
 */
public class SemicolonSeparatedPropertiesParserTest extends TestCase {

  public SemicolonSeparatedPropertiesParserTest(String name) {
    super(name);
  }

  public void testParsePropertiesWithDuplicatedProperties() {
    try {
      SemicolonSeparatedPropertiesParser
          .parseProperties("firstName=Luke;firstName=Han");
      fail();
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception
    }
  }

  public void testParsePropertiesWithOneProperty() {
    String key = "firstName";
    String value = "Anakin";

    Properties expected = new Properties();
    expected.setProperty(key, value);

    Properties actual = SemicolonSeparatedPropertiesParser.parseProperties(key
        + "=" + value);
    assertEquals(expected, actual);
  }

  public void testParsePropertiesWithPropertyNotMatchingPattern() {
    try {
      SemicolonSeparatedPropertiesParser.parseProperties("Hi");
      fail();
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception
    }
  }

  public void testParsePropertiesWithText() {
    Properties expected = new Properties();
    expected.setProperty("firstName", "Han");
    expected.setProperty("lastName", "Solo");

    int propertyCount = expected.size();

    StringBuffer buffer = new StringBuffer();
    int counter = 0;
    for (Iterator i = expected.keySet().iterator(); i.hasNext();) {
      String key = (String) i.next();
      String value = expected.getProperty(key);
      buffer.append(key + "=" + value);

      if (++counter < propertyCount) {
        buffer.append(";");
      }
    }

    String text = buffer.toString();
    assertFalse(text.endsWith(";"));

    Properties actual = SemicolonSeparatedPropertiesParser
        .parseProperties(text);
    assertEquals(expected, actual);
  }

  public void testParsePropertiesWithTextEndingWithSemicolon() {
    Properties expected = new Properties();
    expected.setProperty("firstName", "Luke");
    expected.setProperty("lastName", "Skywalker");
    expected.setProperty("role", "Jedi Knight");

    StringBuffer buffer = new StringBuffer();
    for (Iterator i = expected.keySet().iterator(); i.hasNext();) {
      String key = (String) i.next();
      String value = expected.getProperty(key);
      buffer.append(key + "=" + value + ";");
    }

    Properties actual = SemicolonSeparatedPropertiesParser
        .parseProperties(buffer.toString());
    assertEquals(expected, actual);
  }
  
  public void testParsePropertiesWithTextHavingOnlySemicolon() {
    assertNull(SemicolonSeparatedPropertiesParser.parseProperties(";"));
  }
}
