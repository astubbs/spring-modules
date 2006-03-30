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

package org.springmodules.orm.orbroker;

import net.sourceforge.orbroker.Broker;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Omar Irbouh
 * @since 2005.06.02
 */
public class BrokerFactoryBean implements FactoryBean, InitializingBean {

  private DataSource dataSource;

  private Resource configLocation;

  private Properties textReplacements = null;

  private Broker broker;

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void setConfigLocation(Resource configLocation) {
    this.configLocation = configLocation;
  }

  public void setTextReplacements(Properties textReplacements) {
    this.textReplacements = textReplacements;
  }

  public void afterPropertiesSet() throws IOException {
    // some assertions
    Assert.notNull(dataSource, "dataSource can not be null");
    Assert.notNull(configLocation, "configLocation can not be null");

    // create and initialize the broker
    this.broker = new Broker(configLocation.getInputStream(), dataSource);

    // register text replacements
    if (this.textReplacements != null && !this.textReplacements.isEmpty())
      this.broker.setTextReplacements(this.textReplacements);
  }

  public Object getObject() throws Exception {
    return this.broker;
  }

  public Class getObjectType() {
    return (this.broker != null ? this.broker.getClass() : Broker.class);
  }

  public boolean isSingleton() {
    return true;
  }

}