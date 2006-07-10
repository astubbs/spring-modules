package org.springmodules.workflow.flux;

import flux.EngineException;
import flux.Configuration;

import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.util.Properties;

/**
 * A JavaBean for the XML Flux workflow engine. Flux is also a job 
 * scheduler and a business process management (BPM) engine.
 *
 * @author Copyright 2000-2006 Flux Corporation. All rights reserved.
 */
public class XmlEngineBean extends org.springmodules.scheduling.flux.XmlEngineBean{
  /**
   * Creates an XML engine with a default in-memory database.
   *
   * @throws flux.EngineException If a system error occurs.
   * @see flux.xml.XmlFactory#makeXmlEngine
   */
  public XmlEngineBean() throws EngineException {
    super();
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
    super(configuration);
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
    super(configuration);
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
    super(configurationPropertiesFile);
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
    super(host, port);
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
    super(host, port, bindName);
  } // constructor
}
