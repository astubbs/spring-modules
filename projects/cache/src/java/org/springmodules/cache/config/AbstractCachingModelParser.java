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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import org.springframework.util.CollectionUtils;
import org.springframework.util.xml.DomUtils;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FlushingModel;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractCachingModelParser implements CacheModelParser {

  private static class XmlAttribute {

    static final String TARGET = "target";
  }

  private static class XmlElement {

    static final String CACHING = "caching";

    static final String FLUSHING = "flushing";

  }

  /**
   * Constructor.
   */
  public AbstractCachingModelParser() {
    super();
  }

  /**
   * @see CacheModelParser#parseCachingModels(Element)
   */
  public final Map parseCachingModels(Element element) {
    Map models = new HashMap();

    List modelElements = DomUtils.getChildElementsByTagName(element,
        XmlElement.CACHING, true);
    if (!CollectionUtils.isEmpty(modelElements)) {
      int modelElementCount = modelElements.size();

      for (int i = 0; i < modelElementCount; i++) {
        Element modelElement = (Element) modelElements.get(i);
        String key = modelElement.getAttribute(XmlAttribute.TARGET);

        CachingModel model = parseCachingModel(modelElement);
        models.put(key, model);
      }
    }

    return models;
  }

  /**
   * @see CacheModelParser#parseFlushingModels(Element)
   */
  public Map parseFlushingModels(Element element) {
    Map models = new HashMap();

    List modelElements = DomUtils.getChildElementsByTagName(element,
        XmlElement.FLUSHING, true);
    if (!CollectionUtils.isEmpty(modelElements)) {
      int modelElementCount = modelElements.size();

      for (int i = 0; i < modelElementCount; i++) {
        Element modelElement = (Element) modelElements.get(i);
        String key = modelElement.getAttribute(XmlAttribute.TARGET);

        FlushingModel model = parseFlushingModel(modelElement);
        models.put(key, model);
      }
    }

    return models;
  }

  protected abstract CachingModel parseCachingModel(Element element);

  protected abstract FlushingModel parseFlushingModel(Element element);
}
