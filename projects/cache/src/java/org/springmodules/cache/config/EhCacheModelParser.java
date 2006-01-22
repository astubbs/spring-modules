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

import org.w3c.dom.Element;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.provider.ehcache.EhCacheCachingModel;
import org.springmodules.cache.provider.ehcache.EhCacheFlushingModel;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class EhCacheModelParser extends AbstractCachingModelParser {

  private static class XmlAttribute {

    static final String CACHE_NAME = "cacheName";

    static final String CACHE_NAMES = "cacheNames";
  
  }

  /**
   * Constructor.
   */
  public EhCacheModelParser() {
    super();
  }

  /**
   * @see AbstractCachingModelParser#parseCachingModel(Element)
   */
  protected CachingModel parseCachingModel(Element element) {
    String cacheName = element.getAttribute(XmlAttribute.CACHE_NAME);
    EhCacheCachingModel model = new EhCacheCachingModel(cacheName);
    return model;
  }

  /**
   * @see AbstractCachingModelParser#parseCachingModel(Element)
   */
  protected FlushingModel parseFlushingModel(Element element) {
    String csvCacheNames = element.getAttribute(XmlAttribute.CACHE_NAMES);
    EhCacheFlushingModel model = new EhCacheFlushingModel(csvCacheNames);
    return model;
  }
}
