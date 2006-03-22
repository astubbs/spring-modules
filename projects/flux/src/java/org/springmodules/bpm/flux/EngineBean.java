package org.springmodules.bpm.flux;

import flux.Configuration;
import flux.EngineException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;

/**
 * A JavaBean for the Flux business process management (BPM) engine. 
 * Flux is also a job scheduler and a workflow engine.
 *
 * @author Copyright 2000-2006 Flux Corporation. All rights reserved.
 */
public class EngineBean extends org.springmodules.scheduling.flux.EngineBean {

  /**
   * Creates an engine with a default in-memory database.
   *
   * @throws flux.EngineException If a system error occurs.
   * @see flux.Factory#makeEngine
   */
  public EngineBean() throws EngineException {
    super();
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
    super(configuration);
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
    super(configuration);
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
    super(configurationPropertiesFile);
  } // constructor

  // note: there is no constructor for a properties input stream, an XML input stream, an XML config file, or a literal XML configuration

  /**
   * Looks up an RMI engine at the specified host and RMI registry port
   * using the default RMI registry bind name. This engine bean acts as
   * a proxy to the remote RMI engine.
   *
   * @param host The host where the remote RMI engine is located.
   * @param port The RMI registry port on the specified host.
   * @throws flux.EngineException If a system error occurs.
   * @throws java.rmi.RemoteException If a networking error occurs.
   * @throws java.rmi.NotBoundException If there is no object in the RMI
   * registry under the default bind name.
   * @see flux.Factory#lookupRmiEngine(String, int)
   */
  public EngineBean(String host, int port) throws EngineException, RemoteException, NotBoundException {
    super(host, port);
  } // constructor

  /**
   * Looks up an RMI engine at the specified host and RMI registry port
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
   * @see flux.Factory#lookupRmiEngine(String, int, String)
   */
  public EngineBean(String host, int port, String bindName) throws EngineException, RemoteException, NotBoundException {
    super(host, port, bindName);
  } // constructor

} // class EngineBean