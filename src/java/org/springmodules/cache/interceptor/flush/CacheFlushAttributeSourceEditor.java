/* 
 * Created on March 2, 2005
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
package org.springmodules.cache.interceptor.flush;

import java.beans.PropertyEditorSupport;
import java.util.Iterator;
import java.util.Properties;

import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.util.StringUtils;

/**
 * <p>
 * Property editor that creates a new instance of
 * <code>{@link MethodMapCacheFlushAttributeSource}</code> by parsing a given
 * <code>String</code>. The cache-flush attribute <code>String</code> must
 * be parseable by the <code>{@link CacheFlushAttributeEditor}</code> in this
 * package.
 * </p>
 * <p>
 * Strings are in property syntax, with the form: <br>
 * <code>FQCN.methodName=&lt;cache-flush attribute string&gt;</code>
 * </p>
 * <p>
 * For example: <br>
 * <code>com.mycompany.mycode.MyClass.myMethod=[cacheProfileIds=myCache]</code>
 * </p>
 * <p>
 * <b>NOTE: </b> The specified class must be the one where the methods are
 * defined; in case of implementing an interface, the interface class name.
 * </p>
 * <p>
 * Note: Will register all overloaded methods for a given name. Does not support
 * explicit registration of certain overloaded methods. Supports "xxx*"
 * mappings, e.g. "notify*" for "notify" and "notifyAll".
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/25 06:53:18 $
 */
public class CacheFlushAttributeSourceEditor extends PropertyEditorSupport {

  /**
   * Creates a new instance of
   * <code>{@link MethodMapCacheFlushAttributeSource}</code> from the
   * specified <code>String</code>.
   * 
   * @param text
   *          the <code>String</code> to be parsed.
   */
  public void setAsText(String text) {
    if (StringUtils.hasText(text)) {
      MethodMapCacheFlushAttributeSource cacheFlushAttributeSource = new MethodMapCacheFlushAttributeSource();

      PropertiesEditor propertiesEditor = new PropertiesEditor();
      propertiesEditor.setAsText(text);
      Properties properties = (Properties) propertiesEditor.getValue();

      // Now we have properties, process each one individually.
      CacheFlushAttributeEditor cacheFlushAttributeEditor = new CacheFlushAttributeEditor();

      Iterator keySetIterator = properties.keySet().iterator();
      while (keySetIterator.hasNext()) {
        String key = (String) keySetIterator.next();
        String value = properties.getProperty(key);

        // Convert value to a cache-flush attribute.
        cacheFlushAttributeEditor.setAsText(value);
        FlushCache cacheFlushAttribute = (FlushCache) cacheFlushAttributeEditor
            .getValue();

        // Register name and attribute.
        cacheFlushAttributeSource.addCacheFlushAttribute(key,
            cacheFlushAttribute);
      }

      super.setValue(cacheFlushAttributeSource);
    }
  }
}