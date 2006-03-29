/* 
 * Created on Mar 28, 2006
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
package org.springmodules.cache.config.jcs;

import junit.framework.TestCase;

import org.w3c.dom.Element;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.config.DomElementStub;
import org.springmodules.cache.provider.jcs.JcsCachingModel;
import org.springmodules.cache.provider.jcs.JcsFlushingModel;
import org.springmodules.cache.provider.jcs.JcsFlushingModel.CacheStruct;

/**
 * <p>
 * Unit Tests for <code>{@link JcsModelParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class JcsModelParserTests extends TestCase {

  private JcsModelParser parser;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public JcsModelParserTests(String name) {
    super(name);
  }

  public void testDoParseFlushingModelWithMoreThanOneCacheSubelement() {
    boolean flushBeforeMethodExecution = true;

    Element element = new DomElementStub("flushing");
    element.appendChild(createCacheElement("cacheOne", "pojos,daos"));
    element.appendChild(createCacheElement("cacheTwo", "services,web"));

    CacheStruct[] cacheStructs = { new CacheStruct("cacheOne", "pojos,daos"),
        new CacheStruct("cacheTwo", "services,web") };

    JcsFlushingModel expected = new JcsFlushingModel();
    expected.setCacheStructs(cacheStructs);
    expected.setFlushBeforeMethodExecution(true);

    FlushingModel actual = parser.doParseFlushingModel(element,
        flushBeforeMethodExecution);

    assertEquals(expected, actual);
  }

  public void testDoParseFlushingModelWithOneCacheSubelement() {
    String cacheName = "testCache";
    String groups = "group1,group2";
    boolean flushBeforeMethodExecution = true;

    Element element = new DomElementStub("flushing");
    element.appendChild(createCacheElement(cacheName, groups));

    FlushingModel actual = parser.doParseFlushingModel(element,
        flushBeforeMethodExecution);
    JcsFlushingModel expected = new JcsFlushingModel();
    expected.setCacheStruct(new CacheStruct(cacheName, groups));
    expected.setFlushBeforeMethodExecution(flushBeforeMethodExecution);

    assertEquals(expected, actual);
  }

  public void testDoParseFlushingModelWithoutCacheSubelements() {
    boolean flushBeforeMethodExecution = true;

    Element element = new DomElementStub("flushing");

    FlushingModel actual = parser.doParseFlushingModel(element,
        flushBeforeMethodExecution);
    JcsFlushingModel expected = new JcsFlushingModel();
    expected.setFlushBeforeMethodExecution(flushBeforeMethodExecution);

    assertEquals(expected, actual);
  }

  public void testParseCachingModel() {
    String cacheName = "testCache";

    Element element = new DomElementStub("caching");
    element.setAttribute("cacheName", cacheName);

    CachingModel actual = parser.parseCachingModel(element);
    CachingModel expected = new JcsCachingModel(cacheName);

    assertEquals(expected, actual);
  }

  protected void setUp() {
    parser = new JcsModelParser();
  }

  private Element createCacheElement(String cacheName, String groups) {
    Element cacheElement = new DomElementStub("cache");
    cacheElement.setAttribute("name", cacheName);
    cacheElement.setAttribute("groups", groups);

    return cacheElement;
  }

}
