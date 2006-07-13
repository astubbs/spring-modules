/*
* Copyright 2006 GigaSpaces, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.springmodules.cache.config.gigaspaces;

import junit.framework.TestCase;

import org.springmodules.cache.provider.gigaspaces.GigaSpacesFacade;

/**
 * <p>
 * Unit Tests for <code>{@link GigaSpacesFacadeParser}</code>.
 * </p>
 *
 * @author Alex Ruiz
 */
public class GigaSpacesFacadeParserTests extends TestCase {

  private GigaSpacesFacadeParser parser;

  public void testGetCacheProviderFacadeClass() {
    assertEquals("<Cache Provider Facade class>", GigaSpacesFacade.class, parser
        .getCacheProviderFacadeClass());
  }

  protected void setUp() {
    parser = new GigaSpacesFacadeParser();
  }
}
