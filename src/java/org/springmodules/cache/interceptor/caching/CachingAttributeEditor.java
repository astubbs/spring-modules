/* 
 * Created on Jan 17, 2005
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

package org.springmodules.cache.interceptor.caching;

import java.beans.PropertyEditorSupport;
import java.util.Properties;

import org.springmodules.cache.util.BracketSeparatedPropertiesParser;

/**
 * <p>
 * Creates a new instance of <code>{@link Cached}</code> by parsing a String
 * of the form <code>[cacheProfileId=<i>value</i>]</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/11 04:02:15 $
 */
public class CachingAttributeEditor extends PropertyEditorSupport {

  /**
   * Constructor.
   */
  public CachingAttributeEditor() {
    super();
  }

  /**
   * Creates a new instance of <code>{@link Cached}</code> from the specified
   * <code>String</code>.
   * 
   * @param text
   *          the <code>String</code> to be parsed.
   * 
   * @see BracketSeparatedPropertiesParser#parseProperties(String)
   */
  public void setAsText(String text) {
    Properties properties = BracketSeparatedPropertiesParser
      .parseProperties(text);

    String cacheProfileId = properties.getProperty("cacheProfileId");
    Cached cached = new Cached(cacheProfileId);
    super.setValue(cached);
  }
}