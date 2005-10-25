/* 
 * Created on Sep 26, 2005
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
 * Copyright @2005 the original author or authors.
 */
package org.springmodules.cache.integration;

/**
 * <p>
 * TODO: Verify spelling of 'contexts'
 * Paths of the Spring application context(s) used in integration tests.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class ApplicationContextPaths {
  
  public static final String JBOSSCACHE = "**/jbosscacheApplicationContext.xml";

  public static final String JCS = "**/jcsApplicationContext.xml";

  public static final String METADATA_ATTRIBUTES = "**/metadataContext.xml";

  public static final String OSCACHE = "**/oscacheApplicationContext.xml";

  public static final String PROXY_FACTORY = "**/proxyFactoryApplicationContext.xml";
}
