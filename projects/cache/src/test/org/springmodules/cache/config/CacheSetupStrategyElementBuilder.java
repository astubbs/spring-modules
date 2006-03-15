/* 
 * Created on Mar 8, 2006
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

import org.w3c.dom.Element;

import org.springframework.util.ObjectUtils;

/**
 * <p>
 * Creates a XML element representing a cache setup strategy.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheSetupStrategyElementBuilder implements XmlElementBuilder {

  static class CacheKeyGeneratorElementBuilder implements XmlElementBuilder {
    public Element toXml() {
      return new DomElementStub("cacheKeyGenerator");
    }
  }

  static class CachingListenerElementBuilder implements XmlElementBuilder {
    public String refId = "";

    public Element toXml() {
      Element element = new DomElementStub("cachingListener");
      element.setAttribute("refId", refId);
      return element;
    }
  }

  static class CachingModelElementBuilder implements XmlElementBuilder {
    public String target = "";

    public Element toXml() {
      Element element = new DomElementStub("caching");
      element.setAttribute("target", target);
      return element;
    }
  }

  static class FlushingModelElementBuilder implements XmlElementBuilder {
    public String target = "";

    public Element toXml() {
      Element element = new DomElementStub("flushing");
      element.setAttribute("target", target);
      return element;
    }
  }

  CacheKeyGeneratorElementBuilder cacheKeyGeneratorElementBuilder;

  String cacheProviderId = "";

  CachingListenerElementBuilder[] cachingListenerElementBuilders;

  CachingModelElementBuilder[] cachingModelElementBuilders;

  FlushingModelElementBuilder[] flushingModelElementBuilders;

  /**
   * @see XmlElementBuilder#toXml()
   */
  public Element toXml() {
    Element root = new DomElementStub("abstract");
    root.setAttribute("providerId", cacheProviderId);

    if (!ObjectUtils.isEmpty(cachingListenerElementBuilders)) {
      Element listeners = new DomElementStub("cachingListeners");
      appendChildren(listeners, cachingListenerElementBuilders);

      root.appendChild(listeners);
    }

    if (cacheKeyGeneratorElementBuilder != null) {
      root.appendChild(cacheKeyGeneratorElementBuilder.toXml());
    }
    appendChildren(root, cachingModelElementBuilders);
    appendChildren(root, flushingModelElementBuilders);

    return root;
  }

  void setDefaultCachingListenerElementBuilders(int count) {
    CachingListenerElementBuilder[] builders = new CachingListenerElementBuilder[count];
    String refIdPrefix = "listener_";

    for (int i = 0; i < count; i++) {
      CachingListenerElementBuilder builder = new CachingListenerElementBuilder();
      builder.refId = refIdPrefix + i;
      builders[i] = builder;
    }

    cachingListenerElementBuilders = builders;
  }

  void setDefaultCachingModelElementBuilders(int count) {
    CachingModelElementBuilder[] builders = new CachingModelElementBuilder[count];
    String targetPrefix = "target_";

    for (int i = 0; i < count; i++) {
      CachingModelElementBuilder builder = new CachingModelElementBuilder();
      builder.target = targetPrefix + i;
      builders[i] = builder;
    }

    cachingModelElementBuilders = builders;
  }

  void setDefaultFlushingModelElementBuilders(int count) {
    FlushingModelElementBuilder[] builders = new FlushingModelElementBuilder[count];
    String targetPrefix = "target_";

    for (int i = 0; i < count; i++) {
      FlushingModelElementBuilder builder = new FlushingModelElementBuilder();
      builder.target = targetPrefix + i;
      builders[i] = builder;
    }

    flushingModelElementBuilders = builders;
  }

  private void appendChildren(Element parent, XmlElementBuilder[] children) {
    if (!ObjectUtils.isEmpty(children)) {
      int size = children.length;
      for (int i = 0; i < size; i++) {
        parent.appendChild(children[i].toXml());
      }
    }
  }
}
