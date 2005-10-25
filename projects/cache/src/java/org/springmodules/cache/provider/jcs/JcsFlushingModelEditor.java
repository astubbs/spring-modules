/* 
 * Created on Sep 30, 2005
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
package org.springmodules.cache.provider.jcs;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.util.StringUtils;
import org.springmodules.cache.provider.jcs.JcsFlushingModel.CacheStruct;
import org.springmodules.cache.util.SemicolonSeparatedPropertiesParser;

/**
 * <p>
 * <code>PropertyEditor</code> that creates instances of
 * <code>{@link JcsFlushingModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class JcsFlushingModelEditor extends PropertyEditorSupport {

  private static final String CACHE_STRUCT_DELIMETER = "|";

  public JcsFlushingModelEditor() {
    super();
  }

  /**
   * @see PropertyEditorSupport#setAsText(String)
   */
  public void setAsText(String text) throws IllegalArgumentException {
    JcsFlushingModel model = new JcsFlushingModel();

    if (!StringUtils.hasText(text)) {
      setValue(model);
      return;
    }

    if (text.endsWith(CACHE_STRUCT_DELIMETER)) {
      text = text.substring(0, text.length() - CACHE_STRUCT_DELIMETER.length());

      if (!StringUtils.hasText(text)) {
        setValue(model);
        return;
      }
    }

    List cacheStructList = new ArrayList();
    String[] structs = StringUtils.delimitedListToStringArray(text,
        CACHE_STRUCT_DELIMETER);
    int structCount = structs.length;
    for (int i = 0; i < structCount; i++) {
      Properties properties = SemicolonSeparatedPropertiesParser
          .parseProperties(structs[i]);
      CacheStruct struct = new CacheStruct();
      struct.setCacheName(properties.getProperty("cacheName"));
      struct.setGroups(properties.getProperty("groups"));
      cacheStructList.add(struct);
    }

    if (!cacheStructList.isEmpty()) {
      CacheStruct[] cacheStructs = (CacheStruct[]) cacheStructList
          .toArray(new CacheStruct[cacheStructList.size()]);
      model.setCacheStructs(cacheStructs);
    }

    setValue(model);
  }
}
