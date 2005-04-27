/* 
 * Created on Sep 24, 2004
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
 * Copyright @2004 the original author or authors.
 */

package org.springmodules.cache;

import junit.framework.TestCase;

/**
 * <p>
 * Template for Test Cases for JavaBeans that override the methods
 * <code>equals(Object)</code>,<code>hashCode()</code> and
 * <code>toString()</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:42:12 $
 */
public abstract class AbstractJavaBeanTests extends TestCase {

  /**
   * Constructor.
   */
  public AbstractJavaBeanTests() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public AbstractJavaBeanTests(String name) {
    super(name);
  }

  /**
   * Returns an array of objects where each element is equal to the primary
   * object.
   * 
   * @return an array of objects that shoule be equal to the primary object.
   */
  protected abstract Object[] getEqualObjects();

  /**
   * Returns the expected return value of the method <code>hashCode()</code>
   * of the primary object.
   * 
   * @return the expected return value of the method <code>hashCode()</code>
   *         of the primary object.
   */
  protected abstract int getExpectedHashCode();

  /**
   * Returns the expected return value of the method <code>toString()</code>
   * of the primary object.
   * 
   * @return the expected return value of the method <code>toString()</code>
   *         of the primary object.
   */
  protected abstract String getExpectedToString();

  /**
   * Returns an array of objects where each element is not equal to the primary
   * object.
   * 
   * @return an array of objects that should not be equal to the primary object.
   */
  protected abstract Object[] getNotEqualObjects();

  /**
   * Returns the instance of the class to test.
   * 
   * @return the instance of the class to test.
   */
  protected abstract Object getPrimaryObject();

  /**
   * <p>
   * Verifies that the method <code>Object.equals(Object)</code> returns
   * <code>true</code> when the given argument is equal to the primary object.
   * </p>
   * <p>
   * Example:
   * 
   * <pre>
   * // Primary object.
   * String javaCreator = &quot;James Gosling&quot;;
   * 
   * String javaGuy = &quot;James Gosling&quot;;
   *   boolean equals = javaCreator.equals(javaGuy);
   *  
   * </pre>
   * 
   * </p>
   */
  public final void testEqualsWithEqualObjects() {
    Object primaryObject = this.getPrimaryObject();

    Object[] equalObjects = this.getEqualObjects();
    int equalObjectCount = equalObjects.length;

    for (int i = 0; i < equalObjectCount; i++) {
      Object equalObject = equalObjects[i];

      if (!primaryObject.equals(equalObject)) {
        fail("The object '" + equalObject + "' should be equal to '"
            + primaryObject + "'");
      } // end if
    } // end for
  }

  /**
   * <p>
   * Verifies that the method <code>Object.equals(Object)</code> returns
   * <code>false</code> when the given argument is not equal to the primary
   * object.
   * </p>
   * <p>
   * Example:
   * 
   * <pre>
   * // Primary object.
   * String javaCreator = &quot;James Gosling&quot;;
   * 
   * String hibernateCreator = &quot;Gavin King&quot;;
   *   boolean equals = javaCreator.equals(hibernateCreator);
   *  
   * </pre>
   * 
   * </p>
   */
  public final void testEqualsWithNotEqualObjects() {
    Object primaryObject = this.getPrimaryObject();

    Object[] notEqualObjects = this.getNotEqualObjects();
    int notEqualObjectCount = notEqualObjects.length;

    for (int i = 0; i < notEqualObjectCount; i++) {
      Object notEqualObject = notEqualObjects[i];

      if (primaryObject.equals(notEqualObject)) {
        fail("The object '" + notEqualObject + "' should not be equal to '"
            + primaryObject + "'");
      } // end if
    } // end for
  }

  /**
   * <p>
   * Verifies that the method <code>Object.equals(Object)</code> returns
   * <code>false</code> when the given argument is <code>null</code>.
   * </p>
   * <p>
   * Example:
   * 
   * <pre>
   * // Primary object.
   * String javaCreator = &quot;James Gosling&quot;;
   * 
   * boolean equals = javaCreator.equals(null);
   *  
   * </pre>
   * 
   * </p>
   */
  public final void testEqualsWithNull() {
    Object primaryObject = this.getPrimaryObject();

    if (primaryObject.equals(null)) {
      fail("The object '" + primaryObject + "' should not be equal to null");
    }
  }

  /**
   * <p>
   * Verifies that the method <code>Object.equals(Object)</code> returns
   * <code>false</code> when the given argument is an object that is not in
   * the same class hierarchy as the primary object.
   * </p>
   * <p>
   * Example:
   * 
   * <pre>
   * // Primary object.
   * String javaCreator = &quot;James Gosling&quot;;
   * 
   * Integer netbeansVersion = new Integer(4);
   *   boolean equals = javaCreator.equals(netbeansVersion);
   *  
   * </pre>
   * 
   * </p>
   */
  public final void testEqualsWithObjectNotInSameClassHierarchy() {
    Object primaryObject = this.getPrimaryObject();

    Object objectNotInSameClassHierarchy = new Object();

    if (primaryObject.equals(objectNotInSameClassHierarchy)) {
      fail("The object '" + primaryObject + "' should not be equal to '"
          + objectNotInSameClassHierarchy + "'");
    }
  }

  /**
   * <p>
   * Verifies that the method <code>Object.equals(Object)</code> returns
   * <code>true</code> when the given argument is the primary object.
   * </p>
   * <p>
   * Example:
   * 
   * <pre>
   * // Primary object.
   * String javaCreator = &quot;James Gosling&quot;;
   * 
   * boolean equals = javaCreator.equals(javaCreator);
   *  
   * </pre>
   * 
   * </p>
   */
  public final void testEqualsWithSame() {
    Object primaryObject = this.getPrimaryObject();

    if (!primaryObject.equals(primaryObject)) {
      fail("The object '" + primaryObject + "' should be equal to itself");
    }
  }

  /**
   * Verifies that the method <code>Object.hashCode()</code> calculates a hash
   * code value for the primary object.
   */
  public final void testHashCode() {
    Object primaryObject = this.getPrimaryObject();

    int expectedHashCode = this.getExpectedHashCode();
    int actualHashCode = primaryObject.hashCode();

    assertEquals("<hashCode>", expectedHashCode, actualHashCode);
  }

  /**
   * Verifies that the method <code>Object.toString()</code> creates a string
   * representation of the primary object.
   */
  public final void testToString() {
    Object primaryObject = this.getPrimaryObject();

    String expectedToString = this.getExpectedToString();
    String actualToString = primaryObject.toString();

    assertEquals("<toString>", expectedToString, actualToString);
  }
}