/*
 * Created on Oct 18, 2004
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

package org.springmodules.cache.key;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Unit Tests for <code>{@link HashCodeCacheKeyGenerator}</code>.
 * </p>
 *
 * @author Omar Irbouh
 * @author Alex Ruiz
 */
public final class HashCodeCacheKeyGeneratorTests extends
    AbstractCacheKeyGeneratorTests {

  private HashCodeCacheKeyGenerator keyGenerator;

  public HashCodeCacheKeyGeneratorTests(String name) {
    super(name);
  }

  /**
   * Verifies that the method
   * <code>{@link HashCodeCacheKeyGenerator#generateKey(org.aopalliance.intercept.MethodInvocation)}</code>
   * does not try to add the hash codes of the method arguments if there are not
   * any method arguments.
   */
  public void testGenerateKeyWithoutMethodArguments() throws Exception {
    Class targetClass = String.class;
    Method toStringMethod = targetClass.getMethod("toString", new Class[0]);

    // get the expected key.
    HashCodeCalculator hashCodeCalculator = new HashCodeCalculator();
    int methodHashCode = System.identityHashCode(toStringMethod);
    hashCodeCalculator.append(methodHashCode);

    long checkSum = hashCodeCalculator.getCheckSum();
    int hashCode = hashCodeCalculator.getHashCode();

    Serializable expected = new HashCodeCacheKey(checkSum, hashCode);

    // get the actual key.
    Serializable actual = executeGenerateArgumentHashCode(toStringMethod, null);

    assertEquals(expected, actual);
  }

  public void testGenerateKeyWithTwoMapsHavingDifferentEntriesAndGeneratingArgumentHashCode()
      throws Exception {
    keyGenerator.setGenerateArgumentHashCode(true);

    Map foo = new HashMap();
    foo.put("foo", "foo");

    Map bar = new HashMap();
    bar.put("bar", "bar");

    Class targetClass = String.class;
    Method toStringMethod = targetClass.getMethod("toString", new Class[0]);

    Object fooKey = executeGenerateArgumentHashCode(toStringMethod,
        new Object[] { foo });
    Object barKey = executeGenerateArgumentHashCode(toStringMethod,
        new Object[] { bar });

    System.out.println("fooKey: " + fooKey);
    System.out.println("barKey: " + barKey);

    assertFalse("Key <" + fooKey + "> should be different than <" + barKey
        + ">", fooKey.equals(barKey));
  }

	public void testGenerateKeyWithSameArgumentCountAndDifferentValuesWithoutGeneratingArgumentHashCode()
			throws Exception {
		keyGenerator.setGenerateArgumentHashCode(false);

		Class targetClass = String.class;
		Method toStringMethod = targetClass.getMethod("toString", new Class[0]);

		Object[] args1 = new Object[]{"arg1", new Integer(1)};
		Object[] args2 = new Object[]{"arg2", new Integer(2)};

		Object key1 = executeGenerateArgumentHashCode(toStringMethod, args1);
		Object key2 = executeGenerateArgumentHashCode(toStringMethod, args2);

		assertFalse("Key <" + key1 + "> should be different than <" + key2
				+ ">", key1.equals(key2));

		int hashCode1 = ((HashCodeCacheKey) key1).getHashCode();
		int hashCode2 = ((HashCodeCacheKey) key2).getHashCode();

		assertFalse("hasCode <" + hashCode1 + "> should be different than <" + hashCode2
				+ ">", hashCode1 == hashCode2);
	}

	protected void afterSetUp() {
    keyGenerator = new HashCodeCacheKeyGenerator();
  }

  /**
   * @see AbstractCacheKeyGeneratorTests#getCacheKeyGenerator()
   */
  protected CacheKeyGenerator getCacheKeyGenerator() {
    return keyGenerator;
  }

}