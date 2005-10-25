/* 
 * Created on Sep 29, 2005
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
package org.springmodules.cache;

/**
 * <p>
 * Configuration options for flushing a cache.
 * </p>
 * 
 * @author Alex Ruiz
 */
public interface FlushingModel extends CacheModel {

  /**
   * @return <code>true</code> if the cache should be flushed <b>before</b>
   *         the intercepted method is executed. Returns <code>false</code> if
   *         the cache should be flushed <b>after</b> the intercepted method is
   *         executed. This property is used only if caching is performed
   *         through Spring AOP.
   */
  boolean isFlushBeforeMethodExecution();
}
