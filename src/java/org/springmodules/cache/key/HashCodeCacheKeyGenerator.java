/* 
 * Created on Oct 7, 2004
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

import org.apache.commons.lang.builder.HashCodeBuilder;

import org.aopalliance.intercept.MethodInvocation;

/**
 * <p>
 * Generates the key for a cache entry using the hashCode of the intercepted
 * method and its arguments.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/08/04 04:40:59 $
 */
public class HashCodeCacheKeyGenerator implements CacheKeyGenerator {

  /**
   * Flag that indicates if this generator should generate the hash code of the
   * arguments passed to the method to apply caching to. If <code>false</code>,
   * this generator uses the default hash code of the arguments.
   */
  private boolean generateArgumentHashCode;

  public HashCodeCacheKeyGenerator() {
    super();
  }

  public HashCodeCacheKeyGenerator(boolean generateArgumentHashCode) {
    this();
    this.setGenerateArgumentHashCode(generateArgumentHashCode);
  }

  /**
   * @see CacheKeyGenerator#generateKey(MethodInvocation)
   */
  public final Serializable generateKey(MethodInvocation methodInvocation) {
    HashCodeCalculator hashCodeCalculator = new HashCodeCalculator();

    Method method = methodInvocation.getMethod();
    int methodHashCode = System.identityHashCode(method);
    hashCodeCalculator.append(methodHashCode);

    Object[] methodArguments = methodInvocation.getArguments();
    if (methodArguments != null) {
      int methodArgumentCount = methodArguments.length;

      for (int i = 0; i < methodArgumentCount; i++) {
        int methodArgumentHashCode = this
            .getMethodArgumentHashCode(methodArguments[i]);

        hashCodeCalculator.append(methodArgumentHashCode);
      }
    }

    long checkSum = hashCodeCalculator.getCheckSum();
    int hashCode = hashCodeCalculator.getHashCode();

    Serializable cacheKey = new HashCodeCacheKey(checkSum, hashCode);
    return cacheKey;
  }

  /**
   * Returns the hash code of the specified method argument.
   * 
   * @param methodArgument
   *          the method argument.
   * @return the hash code of the specified method argument.
   */
  protected final int getMethodArgumentHashCode(Object methodArgument) {
    int methodArgumentHashCode = 0;

    if (methodArgument != null) {
      if (this.generateArgumentHashCode) {
        methodArgumentHashCode = HashCodeBuilder
            .reflectionHashCode(methodArgument);
      } else {
        methodArgumentHashCode = methodArgument.hashCode();
      }
    }
    return methodArgumentHashCode;
  }

  public final void setGenerateArgumentHashCode(boolean generateArgumentHashCode) {
    this.generateArgumentHashCode = generateArgumentHashCode;
  }

}