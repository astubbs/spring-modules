/* 
 * Created on Jan 19, 2005
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

package org.springmodules.cache.provider;

import java.beans.PropertyEditorSupport;
import java.util.Properties;

import org.springmodules.cache.util.BracketSeparatedPropertiesParser;

/**
 * <p>
 * Creates a new instance of <code>{@link CacheProfile}</code> by parsing a
 * String of the form
 * <code>[propertyName1=propertyValue1][propertyName2=propertyValue2][propertyNameN=propertyValueN]</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/12 08:37:45 $
 */
public abstract class AbstractCacheProfileEditor extends PropertyEditorSupport {

  /**
   * Constructor.
   */
  public AbstractCacheProfileEditor() {
    super();
  }

  /**
   * Creates a new instance of <code>{@link CacheProfile}</code> from the
   * specified String.
   * 
   * @param text
   *          The String to be parsed.
   */
  public final void setAsText(String text) throws IllegalArgumentException {
    Properties properties = BracketSeparatedPropertiesParser
        .parseProperties(text);

    CacheProfile cacheProfile = this.createCacheProfile(properties);
    this.setValue(cacheProfile);
  }

  /**
   * Creates a new instance of <code>{@link CacheProfile}</code> from the
   * specified properties.
   * 
   * @param properties
   *          the specified properties.
   * @return a new cache profile.
   */
  protected abstract CacheProfile createCacheProfile(Properties properties);
}