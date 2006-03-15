package org.springmodules.scheduling.flux;

import flux.Configuration;
import flux.Engine;
import flux.EngineException;
import flux.Factory;
import flux.FlowChart;
import flux.FlowChartElementIterator;
import flux.FlowChartIterator;
import flux.ForecastIterator;
import flux.SubState;
import flux.SuperState;
import flux.TransactionalSession;
import flux.Version;
import flux.agent.AgentAdministrator;
import flux.audittrail.AuditTrailIterator;
import flux.bpm.BusinessProcessAdministrator;
import flux.logging.Level;
import flux.logging.LogIterator;
import flux.messaging.MessageAdministrator;
import flux.messaging.PublisherAdministrator;
import flux.runtimeconfiguration.RuntimeConfigurationNode;
import flux.security.SecurityAdministrator;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

/**
 * A JavaBean for the Flux job scheduler, workflow engine, and business process
 * management (BPM) engine.
 *
 * @author Copyright 2000-2006 Flux Corporation. All rights reserved.
 */
public class EngineBean implements Engine {

  private Engine engine;

  /**
   * Creates an engine with a default in-memory database.
   *
   * @throws flux.EngineException If a system error occurs.
   * @see flux.Factory#makeEngine
   */
  public EngineBean() throws EngineException {
    engine = Factory.makeInstance().makeEngine();
  } // constructor

  /**
   * Creates an engine using the supplied configuration.
   *
   * @param configuration A specification of how to make an engine.
   * @throws flux.EngineException If a system error occurs.
   * @throws java.rmi.RemoteException If a networking error occurs.
   * @see flux.Factory#makeEngine(flux.Configuration)
   */
  public EngineBean(Configuration configuration) throws EngineException, RemoteException {
    engine = Factory.makeInstance().makeEngine(configuration);
  } // constructor

  /**
   * Creates an engine using the supplied configuration properties.
   *
   * @param configuration A specification of how to make an engine.
   * @throws flux.EngineException If a system error occurs.
   * @throws java.rmi.RemoteException If a networking error occurs.
   * @see flux.Factory#makeConfiguration(java.util.Properties)
   * @see flux.Factory#makeEngine(flux.Configuration)
   */
  public EngineBean(Properties configuration) throws EngineException, RemoteException {
    Factory factory = Factory.makeInstance();
    Configuration config = factory.makeConfiguration(configuration);
    engine = Factory.makeInstance().makeEngine(config);
  } // constructor

  /**
   * Creates an engine using the supplied configuration properties.
   *
   * @param configurationPropertiesFile A path to a file that contains
   *                                    configuration properties.
   * @throws flux.EngineException If a system error occurs.
   * @throws java.rmi.RemoteException If a networking error occurs.
   * @see flux.Factory#makeConfigurationFromProperties(String)
   * @see flux.Factory#makeEngine(flux.Configuration)
   */
  public EngineBean(String configurationPropertiesFile) throws EngineException, RemoteException {
    Factory factory = Factory.makeInstance();
    Configuration config = factory.makeConfigurationFromProperties(configurationPropertiesFile);
    engine = Factory.makeInstance().makeEngine(config);
  } // constructor

  // note: there is no constructor for a properties input stream, an XML input stream, an XML config file, or a literal XML configuration

  /**
   * @param host
   * @param port
   * @throws flux.EngineException
   * @throws java.rmi.RemoteException
   * @throws java.rmi.NotBoundException
   * @see flux.Factory#lookupRmiEngine(String, int)
   */
  public EngineBean(String host, int port) throws EngineException, RemoteException, NotBoundException {
    engine = Factory.makeInstance().lookupRmiEngine(host, port);
  } // constructor

  /**
   * @param host
   * @param port
   * @param bindName
   * @throws flux.EngineException
   * @throws java.rmi.RemoteException
   * @throws java.rmi.NotBoundException
   * @see flux.Factory#lookupRmiEngine(String, int, String)
   */
  public EngineBean(String host, int port, String bindName) throws EngineException, RemoteException, NotBoundException {
    engine = Factory.makeInstance().lookupRmiEngine(host, port, bindName);
  } // constructor

  // fixme: these contructors don't allow pointing at an XML config file or an XML literal configuration

  public void clearAuditTrail() throws EngineException, RemoteException {
    engine.clearAuditTrail();
  }

  public void clearLogs() throws EngineException, RemoteException {
    engine.clearLogs();
  }

  public void dispose() throws EngineException, RemoteException {
    engine.dispose();
  }

  public AgentAdministrator getAgentAdministrator() throws EngineException, RemoteException {
    return engine.getAgentAdministrator();
  }

  public String getAuditTrailExpiration() throws EngineException, RemoteException {
    return engine.getAuditTrailExpiration();
  }

  public BusinessProcessAdministrator getBusinessProcessAdministrator() throws EngineException, RemoteException {
    return engine.getBusinessProcessAdministrator();
  }

  public int getConcurrencyLevel() throws EngineException, RemoteException {
    return engine.getConcurrencyLevel();
  }

  public Configuration getConfiguration() throws EngineException, RemoteException {
    return engine.getConfiguration();
  }

  public String getLogExpiration() throws EngineException, RemoteException {
    return engine.getLogExpiration();
  }

  public MessageAdministrator getMessageAdministrator() throws EngineException, RemoteException {
    return engine.getMessageAdministrator();
  }

  public PublisherAdministrator getPublisherAdministrator() throws EngineException, RemoteException {
    return engine.getPublisherAdministrator();
  }

  public RuntimeConfigurationNode getRuntimeConfiguration() throws EngineException, RemoteException {
    return engine.getRuntimeConfiguration();
  }

  public SecurityAdministrator getSecurityAdministrator() throws EngineException, RemoteException {
    return engine.getSecurityAdministrator();
  }

  public Version getVersion() throws RemoteException, EngineException {
    return engine.getVersion();
  }

  public boolean isBpmModuleEnabled() throws RemoteException {
    return engine.isBpmModuleEnabled();
  }

  public boolean isDisposed() throws EngineException, RemoteException {
    return engine.isDisposed();
  }

  public boolean isRunning() throws EngineException, RemoteException {
    return engine.isRunning();
  }

  public boolean join(String namespace, String timeoutTimeExpression) throws EngineException, RemoteException {
    return engine.join(namespace, timeoutTimeExpression);
  }

  public void ping() throws EngineException, RemoteException {
    engine.ping();
  }

  public void setAuditTrailExpiration(String s) throws EngineException, RemoteException {
    engine.setAuditTrailExpiration(s);
  }

  public void setConcurrencyLevel(int i) throws EngineException, RemoteException {
    engine.setConcurrencyLevel(i);
  }

  public void setLogExpiration(String s) throws EngineException, RemoteException {
    engine.setLogExpiration(s);
  }

  public void setRuntimeConfiguration(RuntimeConfigurationNode runtimeConfigurationNode) throws EngineException, RemoteException {
    engine.setRuntimeConfiguration(runtimeConfigurationNode);
  }

  public void start() throws EngineException, RemoteException {
    engine.start();
  }

  public void stop() throws EngineException, RemoteException {
    engine.stop();
  }

  public boolean isSecured() throws RemoteException {
    return engine.isSecured();
  }

  public TransactionalSession makeJ2seSession() throws EngineException, RemoteException {
    return engine.makeJ2seSession();
  }

  public long clear() throws EngineException, RemoteException {
    return engine.clear();
  }

  public long clearSignal(String s, String s1) throws EngineException, RemoteException {
    return engine.clearSignal(s, s1);
  }

  public long clearSignals(String s) throws EngineException, RemoteException {
    return engine.clearSignals(s);
  }

  public long expedite(String s) throws EngineException, RemoteException {
    return engine.expedite(s);
  }

  public long interrupt(String s) throws EngineException, RemoteException {
    return engine.interrupt(s);
  }

  public long pause(String s) throws EngineException, RemoteException {
    return engine.pause(s);
  }

  public long raiseSignal(String s, String s1) throws EngineException, RemoteException {
    return engine.raiseSignal(s, s1);
  }

  public long recover(String s) throws EngineException, RemoteException {
    return engine.recover(s);
  }

  public long remove(String s) throws EngineException, RemoteException {
    return engine.remove(s);
  }

  public long rename(String s, String s1) throws EngineException, RemoteException {
    return engine.rename(s, s1);
  }

  public long resume(String s) throws EngineException, RemoteException {
    return engine.resume(s);
  }

  public long size() throws EngineException, RemoteException {
    return engine.size();
  }

  public long size(String s) throws EngineException, RemoteException {
    return engine.size(s);
  }

  public long sizeByState(String s, SuperState superState, SubState subState) throws EngineException, RemoteException {
    return engine.sizeByState(s, superState, subState);
  }

  public ForecastIterator forecast(String s, Date date, Date date1) throws EngineException, RemoteException {
    return engine.forecast(s, date, date1);
  }

  public FlowChartIterator get() throws EngineException, RemoteException {
    return engine.get();
  }

  public FlowChartIterator getByState(String s, SuperState superState, SubState subState) throws EngineException, RemoteException {
    return engine.getByState(s, superState, subState);
  }

  public FlowChartIterator getFlowCharts(String s) throws EngineException, RemoteException {
    return engine.getFlowCharts(s);
  }

  public FlowChartElementIterator getFlowChartElements(String s) throws EngineException, RemoteException {
    return engine.getFlowChartElements(s);
  }

  public FlowChartElementIterator getFlowChartElements(String s, SuperState superState, SubState subState) throws EngineException, RemoteException {
    return engine.getFlowChartElements(s, superState, subState);
  }

  public AuditTrailIterator scanAuditTrail(String s, Date date, Date date1, Set set, String s1) throws EngineException, RemoteException {
    return engine.scanAuditTrail(s, date, date1, set, s1);
  }

  public AuditTrailIterator scanAuditTrailByGroup(String s, Date date, Date date1, Set set, String s1, String s2) throws EngineException, RemoteException {
    return engine.scanAuditTrailByGroup(s, date, date1, set, s1, s2);
  }

  public AuditTrailIterator scanAuditTrailByUser(String s, Date date, Date date1, Set set, String s1, String s2) throws EngineException, RemoteException {
    return engine.scanAuditTrailByUser(s, date, date1, set, s1, s2);
  }

  public LogIterator scanLogs(String s, Date date, Date date1, Level level, String s1, String s2, String s3) throws EngineException, RemoteException {
    return engine.scanLogs(s, date, date1, level, s1, s2, s3);
  }

  public LogIterator scanLogsByGroup(String s, Date date, Date date1, Level level, String s1, String s2, String s3, String s4) throws EngineException, RemoteException {
    return engine.scanLogsByGroup(s, date, date1, level, s1, s2, s3, s4);
  }

  public LogIterator scanLogsByUser(String s, Date date, Date date1, Level level, String s1, String s2, String s3, String s4) throws EngineException, RemoteException {
    return engine.scanLogsByUser(s, date, date1, level, s1, s2, s3, s4);
  }

  public FlowChart get(String s) throws EngineException, RemoteException {
    return engine.get(s);
  }

  public FlowChart get(String s, String s1) throws EngineException, RemoteException {
    return engine.get(s, s1);
  }

  public String put(FlowChart flowChart) throws EngineException, RemoteException {
    return engine.put(flowChart);
  }

} // class EngineBean
