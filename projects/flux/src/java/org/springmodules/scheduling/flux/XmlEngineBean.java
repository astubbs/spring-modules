package org.springmodules.scheduling.flux;

import flux.xml.XmlEngine;
import flux.xml.XmlFactory;
import flux.Engine;
import flux.EngineException;
import flux.Configuration;
import flux.Version;
import flux.TransactionalSession;
import flux.SuperState;
import flux.SubState;
import flux.Factory;
import flux.security.SecurityAdministrator;
import flux.runtimeconfiguration.RuntimeConfigurationNode;
import flux.messaging.MessageAdministrator;
import flux.messaging.PublisherAdministrator;
import flux.bpm.BusinessProcessAdministrator;
import flux.agent.AgentAdministrator;

import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * A JavaBean for the XML Flux job scheduler. Flux is also a workflow
 * engine and a business process management (BPM) engine.
 *
 * @author Copyright 2000-2006 Flux Corporation. All rights reserved.
 */
public class XmlEngineBean implements XmlEngine {


  private XmlEngine xmlEngine;

  /**
   * Creates an XML engine with a default in-memory database.
   *
   * @throws flux.EngineException If a system error occurs.
   * @see flux.xml.XmlFactory#makeXmlEngine
   */
  public XmlEngineBean() throws EngineException {
    xmlEngine = XmlFactory.makeInstance().makeXmlEngine();
  } // constructor

  /**
   * Creates an XML engine using the supplied configuration.
   *
   * @param configuration A specification of how to make an XML engine.
   * @throws flux.EngineException If a system error occurs.
   * @throws java.rmi.RemoteException If a networking error occurs.
   * @see flux.xml.XmlFactory#makeXmlEngine(flux.Configuration)
   */
  public XmlEngineBean(Configuration configuration) throws EngineException, RemoteException {
    xmlEngine = XmlFactory.makeInstance().makeXmlEngine(configuration);
  } // constructor

  /**
   * Creates an XML engine using the supplied configuration properties.
   *
   * @param configuration A specification of how to make an XML engine.
   * @throws flux.EngineException If a system error occurs.
   * @throws java.rmi.RemoteException If a networking error occurs.
   * @see flux.Factory#makeConfiguration(java.util.Properties)
   * @see flux.xml.XmlFactory#makeXmlEngine(flux.Configuration)
   */
  public XmlEngineBean(Properties configuration) throws EngineException, RemoteException {
    Factory factory = Factory.makeInstance();
    Configuration config = factory.makeConfiguration(configuration);
    xmlEngine = XmlFactory.makeInstance().makeXmlEngine(config);
  } // constructor

  /**
   * Creates an XML engine using the supplied configuration properties.
   *
   * @param configurationPropertiesFile A path to a file that contains
   *                                    configuration properties.
   * @throws flux.EngineException If a system error occurs.
   * @throws java.rmi.RemoteException If a networking error occurs.
   * @see flux.Factory#makeConfigurationFromProperties(String)
   * @see flux.xml.XmlFactory#makeXmlEngine(flux.Configuration)
   */
  public XmlEngineBean(String configurationPropertiesFile) throws EngineException, RemoteException {
    Factory factory = Factory.makeInstance();
    Configuration config = factory.makeConfigurationFromProperties(configurationPropertiesFile);
    xmlEngine = XmlFactory.makeInstance().makeXmlEngine(config);
  } // constructor

  // note: there is no constructor for a properties input stream, an XML input stream, an XML config file, or a literal XML configuration

  /**
   * Looks up an RMI XML engine at the specified host and RMI registry port
   * using the default RMI registry bind name. This engine bean acts as
   * a proxy to the remote RMI engine.
   *
   * @param host The host where the remote RMI engine is located.
   * @param port The RMI registry port on the specified host.
   * @throws flux.EngineException If a system error occurs.
   * @throws java.rmi.RemoteException If a networking error occurs.
   * @throws java.rmi.NotBoundException If there is no object in the RMI
   * registry under the default bind name.
   */
  public XmlEngineBean(String host, int port) throws EngineException, RemoteException, NotBoundException {
    xmlEngine = new XmlEngineBean(host, port, "Flux");
  } // constructor

  /**
   * Looks up an RMI XML engine at the specified host and RMI registry port
   * using the specified RMI registry bind name. This engine bean acts as
   * a proxy to the remote RMI engine.
   *
   * @param host The host where the remote RMI engine is located.
   * @param port The RMI registry port on the specified host.
   * @param bindName The name under which the remote engine is registered
   * in the RMI registry.
   * @throws flux.EngineException If a system error occurs.
   * @throws java.rmi.RemoteException If a networking error occurs.
   * @throws java.rmi.NotBoundException If there is no object in the RMI
   * registry under the specified bind name.
   */
  public XmlEngineBean(String host, int port, String bindName) throws EngineException, RemoteException, NotBoundException {
    Configuration config = Factory.makeInstance().makeConfiguration();
    config.setRegistryPort(port);
    config.setHost(host);
    config.setServerName(bindName);
    xmlEngine = XmlFactory.makeInstance().lookupRmiXmlEngine(config);
  } // constructor

  public Engine getEngine() {
    return xmlEngine.getEngine();
  }

  public void dispose() throws EngineException, RemoteException {
    xmlEngine.dispose();
  }

  public AgentAdministrator getAgentAdministrator() throws EngineException, RemoteException {
    return xmlEngine.getAgentAdministrator();
  }

  public String getAuditTrailExpiration() throws EngineException, RemoteException {
    return xmlEngine.getAuditTrailExpiration();
  }

  public BusinessProcessAdministrator getBusinessProcessAdministrator() throws EngineException, RemoteException {
    return xmlEngine.getBusinessProcessAdministrator();
  }

  public int getConcurrencyLevel() throws EngineException, RemoteException {
    return xmlEngine.getConcurrencyLevel();
  }

  public Configuration getConfiguration() throws EngineException, RemoteException {
    return xmlEngine.getConfiguration();
  }

  public String getLogExpiration() throws EngineException, RemoteException {
    return xmlEngine.getLogExpiration();
  }

  public MessageAdministrator getMessageAdministrator() throws EngineException, RemoteException {
    return xmlEngine.getMessageAdministrator();
  }

  public PublisherAdministrator getPublisherAdministrator() throws EngineException, RemoteException {
    return xmlEngine.getPublisherAdministrator();
  }

  public RuntimeConfigurationNode getRuntimeConfiguration() throws EngineException, RemoteException {
    return xmlEngine.getRuntimeConfiguration();
  }

  public SecurityAdministrator getSecurityAdministrator() throws EngineException, RemoteException {
    return xmlEngine.getSecurityAdministrator();
  }

  public Version getVersion() throws RemoteException, EngineException {
    return xmlEngine.getVersion();
  }

  public boolean isBpmModuleEnabled() throws RemoteException {
    return xmlEngine.isBpmModuleEnabled();
  }

  public boolean isDisposed() throws EngineException, RemoteException {
    return xmlEngine.isDisposed();
  }

  public boolean isRunning() throws EngineException, RemoteException {
    return xmlEngine.isRunning();
  }

  public boolean join(String string, String string1) throws EngineException, RemoteException {
    return xmlEngine.join(string, string1);
  }

  public void ping() throws EngineException, RemoteException {
    xmlEngine.ping();
  }

  public void setAuditTrailExpiration(String string) throws EngineException, RemoteException {
    xmlEngine.setAuditTrailExpiration(string);
  }

  public void setConcurrencyLevel(int i) throws EngineException, RemoteException {
    xmlEngine.setConcurrencyLevel(i);
  }

  public void setLogExpiration(String string) throws EngineException, RemoteException {
    xmlEngine.setLogExpiration(string);
  }

  public void setRuntimeConfiguration(RuntimeConfigurationNode runtimeConfigurationNode) throws EngineException, RemoteException {
    xmlEngine.setRuntimeConfiguration(runtimeConfigurationNode);
  }

  public void start() throws EngineException, RemoteException {
    xmlEngine.start();
  }

  public void stop() throws EngineException, RemoteException {
    xmlEngine.stop();
  }

  public boolean isSecured() throws RemoteException {
    return xmlEngine.isSecured();
  }

  public TransactionalSession makeJ2seSession() throws EngineException, RemoteException {
    return xmlEngine.makeJ2seSession();
  }

  public void get(OutputStream outputStream) throws EngineException, RemoteException {
    xmlEngine.get(outputStream);
  }

  public void getByState(String string, SuperState superState, SubState subState, OutputStream outputStream) throws EngineException, RemoteException {
    xmlEngine.getByState(string, superState, subState, outputStream);
  }

  public void getFlowCharts(String string, OutputStream outputStream) throws EngineException, RemoteException {
    xmlEngine.getFlowCharts(string, outputStream);
  }

  public List put(InputStream inputStream) throws EngineException, RemoteException {
    return xmlEngine.put(inputStream);
  }

  public long clear() throws EngineException, RemoteException {
    return xmlEngine.clear();
  }

  public void clearAuditTrail() throws EngineException, RemoteException {
    xmlEngine.clearAuditTrail();
  }

  public void clearLogs() throws EngineException, RemoteException {
    xmlEngine.clearLogs();
  }

  public long clearSignal(String string, String string1) throws EngineException, RemoteException {
    return xmlEngine.clearSignal(string, string1);
  }

  public long clearSignals(String string) throws EngineException, RemoteException {
    return xmlEngine.clearSignals(string);
  }

  public long expedite(String string) throws EngineException, RemoteException {
    return xmlEngine.expedite(string);
  }

  public long interrupt(String string) throws EngineException, RemoteException {
    return xmlEngine.interrupt(string);
  }

  public long pause(String string) throws EngineException, RemoteException {
    return xmlEngine.pause(string);
  }

  public long raiseSignal(String string, String string1) throws EngineException, RemoteException {
    return xmlEngine.raiseSignal(string, string1);
  }

  public long recover(String string) throws EngineException, RemoteException {
    return xmlEngine.recover(string);
  }

  public long remove(String string) throws EngineException, RemoteException {
    return xmlEngine.remove(string);
  }

  public long rename(String string, String string1) throws EngineException, RemoteException {
    return xmlEngine.rename(string, string1);
  }

  public long resume(String string) throws EngineException, RemoteException {
    return xmlEngine.resume(string);
  }

  public long size() throws EngineException, RemoteException {
    return xmlEngine.size();
  }

  public long size(String string) throws EngineException, RemoteException {
    return xmlEngine.size(string);
  }

  public long sizeByState(String string, SuperState superState, SubState subState) throws EngineException, RemoteException {
    return xmlEngine.sizeByState(string, superState,subState);
  }
}
