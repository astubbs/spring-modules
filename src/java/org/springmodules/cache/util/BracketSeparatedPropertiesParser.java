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

import org.apache.commons.lang.StringUtils;
import org.springmodules.cache.regex.Match;
import org.springmodules.cache.regex.Perl5Regex;
import org.springmodules.cache.regex.Regex;

/**
 * <p>
 * Takes a String of form <code>[key1=value1][key2=value2][keyN=valueN]</code>,
 * parses it and creates a <code>java.util.Properties</code> where each value
 * between brakets is an entry of such set of properties.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/04/27 01:42:12 $
 */
public final class BracketSeparatedPropertiesParser {

  /**
   * Regular expression pattern used to parse a String of form "key=value".
   */
  protected static final String REGEX_PATTERN = "([a-zA-Z0-9_]+)=([a-zA-Z0-9_ ,]+)";

  /**
   * Compiled representation of <code>{@link #REGEX_PATTERN}</code>.
   */
  protected static final Regex REGEX = new Perl5Regex(
      "([a-zA-Z0-9_]+)=([a-zA-Z0-9_ ,]+)");

  /**
   * Takes a String of form "key=value", splits it into two Strings ("key" and
   * "value") and adds them to the set of parsed properties as a new entry.
   * 
   * @param unparsedProperty
   *          the String of form "name=value".
   * @param parsedProperties
   *          the set of parsed properties.
   * 
   * @throws IllegalArgumentException
   *           if the specified property does not match the regular expression
   *           pattern "([a-zA-Z0-9_]+)=([a-zA-Z0-9_ ,]+)".
   * @throws IllegalArgumentException
   *           if the set of properties already contains the property specified
   *           by the given String.
   * 
   * @see #REGEX_PATTERN
   */
  protected static void addProperty(String unparsedProperty,
      Properties parsedProperties) {

    Match match = REGEX.match(unparsedProperty);
    boolean matches = match.isSuccessful();

    if (!matches) {
      String message = "The String \"" + unparsedProperty
          + "\" should match the regular expression pattern \"" + REGEX_PATTERN
          + "\"";

      throw new IllegalArgumentException(message);
    }

    String[] groups = match.getGroups();
    String key = groups[1].trim();
    String value = groups[2].trim();

    if (parsedProperties.containsKey(key)) {
      String message = "The property \"" + key
          + "\" is specified more than once";

      throw new IllegalArgumentException(message);
    }

    parsedProperties.setProperty(key, value);
  }

  /**
   * Creates a <code>java.util.Properties</code> from the specified String.
   * 
   * @param propertiesAsText
   *          the String to parse.
   * 
   * @return a new instance of <code>java.util.Properties</code>.
   * @throws IllegalArgumentException
   *           if the specified String is empty.
   * @throws IllegalArgumentException
   *           if the specified String does not start with '[' and does not end
   *           with ']'.
   * @see #addProperty(String, Properties)
   */
  public static Properties parseProperties(String propertiesAsText) {

    if (StringUtils.isEmpty(propertiesAsText)) {
      throw new IllegalArgumentException(
          "The String to parse should not be empty");
    }

    if (!propertiesAsText.startsWith("[") || !propertiesAsText.endsWith("]")) {
      throw new IllegalArgumentException("The String '" + propertiesAsText
          + "' should start with '[' and end with ']");
    }

    Properties properties = new Properties();

    // eliminate the first '[' and the last ']' from the String.
    String propertiesToSplit = propertiesAsText.substring(1, propertiesAsText
        .length() - 1);

    // split the properties in Strings of the form 'name=value'.
    String delimeter = "][";
    int delimeterLength = delimeter.length();
    int currentPosition = 0;
    int delimeterPosition = 0;

    while ((delimeterPosition = propertiesToSplit.indexOf(delimeter,
        currentPosition)) != -1) {

      String property = propertiesToSplit.substring(currentPosition,
          delimeterPosition);

      addProperty(property, properties);
      currentPosition = delimeterPosition + delimeterLength;
    }

    if (currentPosition <= propertiesToSplit.length()) {
      // add rest of String
      String property = propertiesToSplit.substring(currentPosition);
      addProperty(property, properties);
    }

    return properties;
  }

}