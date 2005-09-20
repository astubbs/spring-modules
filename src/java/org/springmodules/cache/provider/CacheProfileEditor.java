/* 
 * Created on Sep 9, 2005
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

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
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
 * @version $Revision$ $Date$
 */
public class CacheProfileEditor extends PropertyEditorSupport {

  /**
   * The class of the <code>{@link CacheProfile}</code> to create.
   */
  private Class cacheProfileClass;

  /**
   * <code>PropertyEditor</code>s for the properties of the cache profile to
   * create. Each <code>PropertyEditor</code> is stored using the name of the
   * property (a String) as key.
   */
  private Map cacheProfilePropertyEditors;

  public CacheProfileEditor() {
    super();
  }

  public final Class getCacheProfileClass() {
    return cacheProfileClass;
  }

  public final Map getCacheProfilePropertyEditors() {
    return cacheProfilePropertyEditors;
  }

  private PropertyEditor getPropertyEditor(String propertyName) {
    return cacheProfilePropertyEditors == null ? null
        : (PropertyEditor) cacheProfilePropertyEditors.get(propertyName);
  }

  /**
   * @throws IllegalStateException
   *           if the class of the cache profile to create has not been set.
   * @see PropertyEditor#setAsText(String)
   * @see org.springframework.beans.PropertyAccessor#setPropertyValue(String,
   *      Object)
   */
  public final void setAsText(String text) {
    if (cacheProfileClass == null) {
      throw new IllegalStateException("cacheProfileClass should not be null");
    }

    Properties properties = BracketSeparatedPropertiesParser
        .parseProperties(text);

    BeanWrapper beanWrapper = new BeanWrapperImpl(cacheProfileClass);

    if (properties != null) {
      for (Iterator i = properties.keySet().iterator(); i.hasNext();) {
        String propertyName = (String) i.next();
        String textProperty = properties.getProperty(propertyName);

        Object propertyValue = null;

        PropertyEditor propertyEditor = getPropertyEditor(propertyName);
        if (propertyEditor != null) {
          propertyEditor.setAsText(textProperty);
          propertyValue = propertyEditor.getValue();
        } else {
          propertyValue = textProperty;
        }
        beanWrapper.setPropertyValue(propertyName, propertyValue);
      }
    }

    setValue(beanWrapper.getWrappedInstance());
  }

  public final void setCacheProfileClass(Class newCacheProfileClass) {
    cacheProfileClass = newCacheProfileClass;
  }

  public final void setCacheProfilePropertyEditors(
      Map newCacheProfilePropertyEditors) {
    cacheProfilePropertyEditors = newCacheProfilePropertyEditors;
  }
}
