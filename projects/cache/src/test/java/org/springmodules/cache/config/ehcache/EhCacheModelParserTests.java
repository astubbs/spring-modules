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
package org.springmodules.cache.config.ehcache;

import junit.framework.TestCase;

import org.w3c.dom.Element;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.config.DomElementStub;
import org.springmodules.cache.provider.ehcache.EhCacheCachingModel;
import org.springmodules.cache.provider.ehcache.EhCacheFlushingModel;

/**
 * <p>
 * Unit Tests for <code>{@link EhCacheModelParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class EhCacheModelParserTests extends TestCase {

  private EhCacheModelParser parser;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public EhCacheModelParserTests(String name) {
    super(name);
  }

  public void testDoParseFlushingModel() {
    String cacheNames = "testCache";
    boolean flushBeforeMethodExecution = true;

    Element element = new DomElementStub("flushing");
    element.setAttribute("cacheNames", cacheNames);

    FlushingModel actual = parser.doParseFlushingModel(element,
        flushBeforeMethodExecution);
    EhCacheFlushingModel expected = new EhCacheFlushingModel(cacheNames);
    expected.setFlushBeforeMethodExecution(flushBeforeMethodExecution);

    assertEquals(expected, actual);
  }

  public void testParseCachingModel() {
    String cacheName = "testCache";

    Element element = new DomElementStub("caching");
    element.setAttribute("cacheName", cacheName);

    CachingModel actual = parser.parseCachingModel(element);
    CachingModel expected = new EhCacheCachingModel(cacheName);

    assertEquals(expected, actual);
  }

  protected void setUp() {
    parser = new EhCacheModelParser();
  }

}
