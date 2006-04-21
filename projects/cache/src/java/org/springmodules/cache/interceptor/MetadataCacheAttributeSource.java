/* 
 * Created on Apr 21, 2006
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
 * Copyright @2006 the original author or authors.
 */
package org.springmodules.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springmodules.cache.CacheAttribute;

import org.springframework.aop.support.AopUtils;
import org.springframework.metadata.Attributes;
import org.springframework.util.CollectionUtils;

/**
 * TODO Describe this class
 * 
 * @author Alex Ruiz
 * 
 */
public class MetadataCacheAttributeSource {

  public interface TypeMatcher {
    boolean matches(Object o);
  }

  public static final Object NULL_ATTRIBUTE = new Object();

  private final Map attributeMap;

  private Attributes attributes;

  public MetadataCacheAttributeSource() {
    attributeMap = new HashMap();
  }

  public CacheAttribute get(Method m, Class t, TypeMatcher tm) {
    String key = key(m, t);
    Object cached = attributeMap.get(key);
    if (cached != null) return unmaskNull(cached);
    CacheAttribute attribute = retrieve(m, t, tm);
    attributeMap.put(key, maskNull(attribute));
    return attribute;
  }

  public void setAttributes(Attributes newAttributes) {
    attributes = newAttributes;
  }

  private Collection allAttributes(Method m) {
    return attributes.getAttributes(m);
  }

  private CacheAttribute find(Collection methodAttributes, TypeMatcher tm) {
    if (CollectionUtils.isEmpty(methodAttributes)) return null;
    for (Iterator i = methodAttributes.iterator(); i.hasNext();) {
      Object attribute = i.next();
      if (tm.matches(attribute)) return (CacheAttribute) attribute;
    }
    return null;
  }

  private String key(Method m, Class t) {
    return t.toString() + System.identityHashCode(m);
  }

  private Object maskNull(CacheAttribute c) {
    return c == null ? NULL_ATTRIBUTE : c;
  }

  private CacheAttribute retrieve(Method m, Class t, TypeMatcher tm) {
    Method specificMethod = AopUtils.getMostSpecificMethod(m, t);
    CacheAttribute attribute = find(allAttributes(specificMethod), tm);
    if (attribute != null) return attribute;
    if (specificMethod != m) return find(allAttributes(m), tm);
    return null;
  }

  private CacheAttribute unmaskNull(Object o) {
    return o == NULL_ATTRIBUTE ? null : (CacheAttribute) o;
  }
}
