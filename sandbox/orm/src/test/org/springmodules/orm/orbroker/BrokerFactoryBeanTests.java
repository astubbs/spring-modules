/*
 * Copyright 2002-2006 the original author or authors.
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

import junit.framework.TestCase;
import net.sourceforge.orbroker.Broker;
import net.sourceforge.orbroker.BrokerException;
import net.sourceforge.orbroker.Executable;
import org.easymock.MockControl;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springmodules.orm.orbroker.support.BrokerDaoSupport;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

/**
 * BrokerFactoryBean Tests.
 *
 * @author Omar Irbouh
 * @since 2005.06.03
 */
public class BrokerFactoryBeanTests extends TestCase {

  public void testBrokerFactoryWithNullDataSource() {
    BrokerFactoryBean factory = new BrokerFactoryBean();
    try {
      factory.afterPropertiesSet();
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    } catch (Exception e) {
      fail("Exception not expected " + e);
    }
  }

  public void testBrokerFactoryWithNullConfigLocation() {
    MockControl dsControl = MockControl.createControl(DataSource.class);
    BrokerFactoryBean factory = new BrokerFactoryBean();
    factory.setDataSource((DataSource) dsControl.getMock());
    try {
      factory.afterPropertiesSet();
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    } catch (Exception e) {
      fail("Exception not expected " + e);
    }
  }

  public void testBrokerFactoryWithConfigNotFound() {
    MockControl dsControl = MockControl.createControl(DataSource.class);
    BrokerFactoryBean factory = new BrokerFactoryBean();
    factory.setDataSource((DataSource) dsControl.getMock());
    factory.setConfigLocation(new ClassPathResource("path/not-found.xml"));
    try {
      factory.afterPropertiesSet();
      fail("Should have thrown IOException");
    } catch (IOException e) {
      // expected
    }
  }

  public void testBrokerTemplate() throws SQLException, IOException {
    // prepare mock objects
    MockControl dsControl = MockControl.createControl(DataSource.class);
    DataSource ds = (DataSource) dsControl.getMock();

    MockControl conControl = MockControl.createControl(Connection.class);
    Connection con = (Connection) conControl.getMock();

    //needed for orbroker internal since I can not mock ORBroker classes
    prepareNewBrokerInternals(dsControl, ds);

    ds.getConnection();
    dsControl.setReturnValue(con);

    //needed for orbroker internal since I can not mock ORBroker classes
    prepareObtainExecutableInternals(conControl, con);

    con.close();
    conControl.setVoidCallable(1);

    dsControl.replay();
    conControl.replay();

    // run scenario
    Resource config = new ClassPathResource("org/springmodules/orm/orbroker/broker.xml");
    Broker broker = new Broker(config.getInputStream(), ds);
    BrokerTemplate brokerTemplate = new BrokerTemplate();
    brokerTemplate.setBroker(broker);
    brokerTemplate.afterPropertiesSet();

    Object result = brokerTemplate.execute(new BrokerCallback() {
      public Object doInBroker(Executable executable) throws BrokerException {
        return "done";
      }
    });

    assertEquals("result should be [done]", "done", result);

    dsControl.verify();
    conControl.verify();
  }

  public void testBrokerDaoSupport() throws Exception {
    // prepare mock objects
    MockControl dsControl = MockControl.createControl(DataSource.class);
    DataSource ds = (DataSource) dsControl.getMock();

    //needed for orbroker internal since I can not mock ORBroker classes
    prepareNewBrokerInternals(dsControl, ds);

    dsControl.replay();

    BrokerTemplate template = new BrokerTemplate();
    Broker broker = new Broker(ds);
    template.setBroker(broker);
    assertEquals(broker, template.getBroker());

    BrokerDaoSupport testDao = new BrokerDaoSupport() {
    };
    testDao.setBrokerTemplate(template);
    assertEquals(template, testDao.getBrokerTemplate());

    testDao.afterPropertiesSet();
    dsControl.verify();
  }

  /**
   * This method may be updated when upgrading ORBroker to a newer version.
   * ORBroker does not use interfaces, and its main classes are final with a package scope visibility!!!
   * I found noway to mock ORBroker classes!!! jmock / cglib does not support final classes
   *
   * @param dsControl DataSource mock control
   * @param ds DataSource mock
   * @throws SQLException
   */
  protected void prepareNewBrokerInternals(MockControl dsControl, DataSource ds) throws SQLException {
    MockControl conControl = MockControl.createControl(Connection.class);
    Connection con = (Connection) conControl.getMock();

    MockControl mdControl = MockControl.createControl(DatabaseMetaData.class);
    DatabaseMetaData md = (DatabaseMetaData) mdControl.getMock();

    ds.getConnection();
    dsControl.setReturnValue(con);

    con.getTransactionIsolation();
    conControl.setReturnValue(Connection.TRANSACTION_READ_COMMITTED);
    con.getAutoCommit();
    conControl.setReturnValue(false);

    con.getMetaData();
    conControl.setReturnValue(md);

    md.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    mdControl.setReturnValue(true);

    md.supportsBatchUpdates();
    mdControl.setReturnValue(true);

    con.close();
    conControl.setVoidCallable(1);

    conControl.replay();
    mdControl.replay();
  }

  /**
   * This method may be updated when upgrading ORBroker to a newer version.
   * ORBroker does not use interface, and its main classes are final with a package scope visibility!!!
   * I found noway to mock ORBroker classes!!! jmock / cglib does not support final classes
   *
   * @param conControl Connection mock control
   * @param con Connection mock
   * @throws SQLException
   */
  protected void prepareObtainExecutableInternals(MockControl conControl, Connection con) throws SQLException {
    con.getAutoCommit();
    conControl.setReturnValue(false);

    con.getTransactionIsolation();
    conControl.setReturnValue(Connection.TRANSACTION_READ_COMMITTED);
  }

}