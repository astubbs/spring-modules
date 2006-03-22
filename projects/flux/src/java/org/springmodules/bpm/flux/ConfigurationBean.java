package org.springmodules.bpm.flux;

import flux.EngineException;
import java.util.Properties;

/**
 * A JavaBean for configuring the Flux business process management 
 * (BPM) engine. Flux is also a job scheduler and a workflow engine.
 *
 * @author Copyright 2000-2006 Flux Corporation. All rights reserved.
 */
public class ConfigurationBean extends org.springmodules.scheduling.flux.ConfigurationBean {

  /**
   * Creates a default engine configuration.
   *
   * @throws flux.EngineException If a system error occurs.
   * @see flux.Factory#makeConfiguration
   */
  public ConfigurationBean() throws EngineException {
    super();
  } // constructor

  /**
   * Creates an engine configuration using the supplied configuration
   * properties.
   *
   * @param properties An object that contains configuration properties.
   * @throws flux.EngineException If a system error occurs.
   * @see flux.Factory#makeConfiguration(java.util.Properties)
   */
  public ConfigurationBean(Properties properties) throws EngineException {
    super(properties);
  } // constructor

  /**
   * Creates an engine configuration using the supplied configuration properties
   * file.
   *
   * @param propertiesFile A path to a file that contains configuration
   *                       properties.
   * @throws flux.EngineException If a system error occurs.
   * @see flux.Factory#makeConfigurationFromProperties(String)
   */
  public ConfigurationBean(String propertiesFile) throws EngineException {
    super(propertiesFile);
  } // constructor

  // note: there is no constructor for a properties input stream, an XML input stream, an XML config file, or a literal XML configuration

} // class ConfigurationBean