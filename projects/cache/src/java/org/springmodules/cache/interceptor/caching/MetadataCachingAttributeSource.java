/* 
 * Created on Sep 21, 2004
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

package org.springmodules.cache.interceptor.caching;

import java.lang.reflect.Method;

import org.springmodules.cache.interceptor.MetadataCacheAttributeSource;
import org.springmodules.cache.interceptor.MetadataCacheAttributeSource.TypeMatcher;

import org.springframework.metadata.Attributes;

/**
 * <p>
 * Binds caching metadata atttributes to methods.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class MetadataCachingAttributeSource implements
    CachingAttributeSource {

  static final TypeMatcher CACHING_ATTRIBUTE_MATCHER = new TypeMatcher() {
    public boolean matches(Object o) {
      return o instanceof Cached;
    }
  };

  private final MetadataCacheAttributeSource source;

  public MetadataCachingAttributeSource() {
    source = new MetadataCacheAttributeSource();
  }

  public Cached getCachingAttribute(Method m, Class t) {
    if (CachingUtils.isCacheable(m))
      return (Cached) source.get(m, t, CACHING_ATTRIBUTE_MATCHER);
    return null;
  }

  public void setAttributes(Attributes newAttributes) {
    source.setAttributes(newAttributes);
  }
}