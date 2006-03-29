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
package org.springmodules.cache.config.oscache;

import junit.framework.TestCase;

import org.w3c.dom.Element;

import org.springframework.util.StringUtils;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.config.DomElementStub;
import org.springmodules.cache.provider.oscache.OsCacheCachingModel;
import org.springmodules.cache.provider.oscache.OsCacheFlushingModel;

/**
 * <p>
 * Unit Tests for <code>{@link OsCacheModelParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class OsCacheModelParserTests extends TestCase {

  private OsCacheModelParser parser;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public OsCacheModelParserTests(String name) {
    super(name);
  }

  public void testDoParseFlushingModel() {
    String groups = "pojos,services";
    boolean flushBeforeMethodExecution = true;

    Element element = new DomElementStub("flushing");
    element.setAttribute("groups", groups);

    FlushingModel actual = parser.doParseFlushingModel(element,
        flushBeforeMethodExecution);
    OsCacheFlushingModel expected = new OsCacheFlushingModel(groups);
    expected.setFlushBeforeMethodExecution(flushBeforeMethodExecution);

    assertEquals(expected, actual);
  }

  public void testParseCachingModel() {
    String cronExpression = "* 0 * * *";
    String groups = "pojos,web";
    String refreshPeriod = "2";

    Element element = new DomElementStub("caching");
    element.setAttribute("cronExpression", cronExpression);
    element.setAttribute("groups", groups);
    element.setAttribute("refreshPeriod", refreshPeriod);

    CachingModel actual = parser.parseCachingModel(element);
    CachingModel expected = new OsCacheCachingModel(groups, Integer
        .parseInt(refreshPeriod), cronExpression);

    assertEquals(expected, actual);
  }

  public void testParseCachingModelWithNotNumericRefreshPeriod() {
    String cronExpression = "* 0 * * *";
    String groups = "pojos,web";
    String refreshPeriod = "three";

    Element element = createCachingElement(cronExpression, groups,
        refreshPeriod);

    CachingModel actual = parser.parseCachingModel(element);
    CachingModel expected = new OsCacheCachingModel(groups, cronExpression);

    assertEquals(expected, actual);
  }

  public void testParseCachingModelWithoutRefreshPeriod() {
    String cronExpression = "* 0 * * *";
    String groups = "pojos,web";

    Element element = createCachingElement(cronExpression, groups);

    CachingModel actual = parser.parseCachingModel(element);
    CachingModel expected = new OsCacheCachingModel(groups, cronExpression);

    assertEquals(expected, actual);
  }

  protected void setUp() {
    parser = new OsCacheModelParser();
  }

  private Element createCachingElement(String cronExpression, String groups) {
    return createCachingElement(cronExpression, groups, null);
  }

  private Element createCachingElement(String cronExpression, String groups,
      String refreshPeriod) {
    Element element = new DomElementStub("caching");
    element.setAttribute("cronExpression", cronExpression);
    element.setAttribute("groups", groups);

    if (StringUtils.hasText(refreshPeriod)) {
      element.setAttribute("refreshPeriod", refreshPeriod);
    }

    return element;
  }
}
