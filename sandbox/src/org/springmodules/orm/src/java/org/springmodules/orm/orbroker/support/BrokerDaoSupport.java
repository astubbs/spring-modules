/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.orm.orbroker.support;

import net.sourceforge.orbroker.Broker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springmodules.orm.orbroker.BrokerTemplate;

/**
 * @author Omar Irbouh
 * @since 2005.06.02
 */
public abstract class BrokerDaoSupport implements InitializingBean {

  protected final Log logger = LogFactory.getLog(getClass());

  protected BrokerTemplate brokerTemplate;

  public final void setBrokerTemplate(BrokerTemplate brokerTemplate) {
    this.brokerTemplate = brokerTemplate;
  }

  public BrokerTemplate getBrokerTemplate() {
    return brokerTemplate;
  }

  public void setBroker(Broker broker) {
    this.brokerTemplate = createBrokerTemplate(broker);
  }

  protected BrokerTemplate createBrokerTemplate(Broker broker) {
    return new BrokerTemplate(broker);
  }

  public void afterPropertiesSet() throws Exception {
    Assert.notNull(brokerTemplate, "brokerTemplate is required");
    initDao();
  }

  /**
   * Subclasses can override this for custom initialization behavior.
   * Gets called after population of this instance's bean properties.
   *
   * @throws Exception if initialization fails
   */
  protected void initDao() throws Exception {
  }

}