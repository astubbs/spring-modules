/* 
 * Created on Jan 21, 2006
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
package org.springmodules.cache.config;

import java.util.List;

import org.w3c.dom.Element;

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.xml.DomUtils;

import org.springmodules.cache.interceptor.caching.CachingListener;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CachingListenerParser {

  private static class XmlElement {
    static final String CACHING_LISTENER = "cachingListener";
  }

  private static class XmlAttribute {
    static final String REF = "ref";
  }

  public List parseCachingListeners(Element element,
      BeanDefinitionRegistry registry) {

    ManagedList listeners = new ManagedList();

    List listenerElements = DomUtils.getChildElementsByTagName(element,
        XmlElement.CACHING_LISTENER, true);
    if (listenerElements != null && !listenerElements.isEmpty()) {
      int listenerCount = listenerElements.size();
      for (int i = 0; i < listenerCount; i++) {
        Element listenerElement = (Element) listenerElements.get(i);
        String ref = listenerElement.getAttribute(XmlAttribute.REF);
        RootBeanDefinition listenerBeanDefinition = (RootBeanDefinition) registry
            .getBeanDefinition(ref);

        if (CachingListener.class.isAssignableFrom(listenerBeanDefinition
            .getBeanClass())) {
          listeners.add(new RuntimeBeanReference(ref));
        }
      }
    }

    return listeners;
  }

}
