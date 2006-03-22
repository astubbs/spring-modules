package org.springmodules.scheduling.flux;

import flux.CacheType;
import flux.Configuration;
import flux.DatabaseType;
import flux.EngineException;
import flux.Factory;
import flux.j2ee.JmsInboundMessageConfiguration;
import flux.logging.Level;
import flux.logging.LoggerType;
import flux.messaging.Publisher;
import flux.runtimeconfiguration.RuntimeConfigurationNode;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * A JavaBean for configuring the Flux job scheduler. Flux is also a 
 * workflow engine and a business process management (BPM) engine.
 *
 * @author Copyright 2000-2006 Flux Corporation. All rights reserved.
 */
public class ConfigurationBean implements Configuration {

  private Configuration c;

  /**
   * Creates a default engine configuration.
   *
   * @throws flux.EngineException If a system error occurs.
   * @see flux.Factory#makeConfiguration
   */
  public ConfigurationBean() throws EngineException {
    c = Factory.makeInstance().makeConfiguration();
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
    c = Factory.makeInstance().makeConfiguration(properties);
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
    c = Factory.makeInstance().makeConfigurationFromProperties(propertiesFile);
  } // constructor

  // note: there is no constructor for a properties input stream, an XML input stream, an XML config file, or a literal XML configuration

  public String getAuditTrailExpiration() {
    return c.getAuditTrailExpiration();
  }

  public String getAuditTrailLogger() {
    return c.getAuditTrailLogger();
  }

  public boolean getBpmBackwardCompatibilityTo70Enabled() {
    return c.getBpmBackwardCompatibilityTo70Enabled();
  }

  public int getCacheSize() {
    return c.getCacheSize();
  }

  public CacheType getCacheType() {
    return c.getCacheType();
  }

  public String getClientLogger() {
    return c.getClientLogger();
  }

  public String getClusterAddress() {
    return c.getClusterAddress();
  }

  public boolean getClusterEnabled() {
    return c.getClusterEnabled();
  }

  public boolean getClusterNetworking() {
    return c.getClusterNetworking();
  }

  public int getClusterPort() {
    return c.getClusterPort();
  }

  public int getConcurrencyLevel() {
    return c.getConcurrencyLevel();
  }

  public boolean getCreateRegistry() {
    return c.getCreateRegistry();
  }

  public String getDashboardFromAddress() {
    return c.getDashboardFromAddress();
  }

  public String getDashboardMailServer() {
    return c.getDashboardMailServer();
  }

  public String getDashboardUrl() {
    return c.getDashboardUrl();
  }

  public Properties getDatabaseProperties() {
    return c.getDatabaseProperties();
  }

  public DatabaseType getDatabaseType() {
    return c.getDatabaseType();
  }

  public String getDataSource() {
    return c.getDataSource();
  }

  public boolean getDataSourceCaching() {
    return c.getDataSourceCaching();
  }

  public String getDataSourcePassword() {
    return c.getDataSourcePassword();
  }

  public String getDataSourceUsername() {
    return c.getDataSourceUsername();
  }

  public boolean getDataSourceUserTransactions() {
    return c.getDataSourceUserTransactions();
  }

  public String getDataSourceUserTransactionClientJndiName() {
    return c.getDataSourceUserTransactionClientJndiName();
  }

  public String getDataSourceUserTransactionServerJndiName() {
    return c.getDataSourceUserTransactionServerJndiName();
  }

  public String getDataSourceUserTransactionTimeout() {
    return c.getDataSourceUserTransactionTimeout();
  }

  public String getDriver() {
    return c.getDriver();
  }

  public String getEngineContentsSummaryFrequency() {
    return c.getEngineContentsSummaryFrequency();
  }

  public String getFailoverTimeWindow() {
    return c.getFailoverTimeWindow();
  }

  public String getHeartbeatFrequency() {
    return c.getHeartbeatFrequency();
  }

  public String getHost() {
    return c.getHost();
  }

  public String getInitialContextFactory() {
    return c.getInitialContextFactory();
  }

  public String getInitialContextPassword() {
    return c.getInitialContextPassword();
  }

  public String getInitialContextUsername() {
    return c.getInitialContextUsername();
  }

  public Level getInternalLoggerLevel() {
    return c.getInternalLoggerLevel();
  }

  public String getInternalLoggerFileDirectory() {
    return c.getInternalLoggerFileDirectory();
  }

  public long getInternalLoggerFileRotationSize() {
    return c.getInternalLoggerFileRotationSize();
  }

  public String getJdbcConnectionRecycleFrequency() {
    return c.getJdbcConnectionRecycleFrequency();
  }

  public String getJdbcPassword() {
    return c.getJdbcPassword();
  }

  public String getJdbcUsername() {
    return c.getJdbcUsername();
  }

  public Set getJmsInboundMessageConfiguration() {
    return c.getJmsInboundMessageConfiguration();
  }

  public String getFairnessTimeWindow() {
    return c.getFairnessTimeWindow();
  }

  public String getFlowChartLogger() {
    return c.getFlowChartLogger();
  }

  public boolean getListenerClassLoaderEnabled() {
    return c.getListenerClassLoaderEnabled();
  }

  public String getLogExpiration() {
    return c.getLogExpiration();
  }

  public LoggerType getLoggerType() {
    return c.getLoggerType();
  }

  public Set getLoggerTypes() {
    return c.getLoggerTypes();
  }

  public int getMaxConnections() {
    return c.getMaxConnections();
  }

  public String getName() {
    return c.getName();
  }

  public String getOracleLargeObjectAdapter() {
    return c.getOracleLargeObjectAdapter();
  }

  public String getProviderUrl() {
    return c.getProviderUrl();
  }

  public Set getPublisherBootstrapConfiguration() {
    return c.getPublisherBootstrapConfiguration();
  }

  public int getRegistryPort() {
    return c.getRegistryPort();
  }

  public int getRmiPort() {
    return c.getRmiPort();
  }

  public boolean getRmiServer() {
    return c.getRmiServer();
  }

  public RuntimeConfigurationNode getRuntimeConfiguration() {
    return c.getRuntimeConfiguration();
  }

  public String getRuntimeConfigurationFile() {
    return c.getRuntimeConfigurationFile();
  }

  public String getRuntimeConfigurationFileRefreshFrequency() {
    return c.getRuntimeConfigurationFileRefreshFrequency();
  }

  public Map getScriptingLanguages() {
    return c.getScriptingLanguages();
  }

  public String getSecurityConfigurationFile() {
    return c.getSecurityConfigurationFile();
  }

  public String getSecurityConfigurationFileEntry() {
    return c.getSecurityConfigurationFileEntry();
  }

  public boolean getSecurityEnabled() {
    return c.getSecurityEnabled();
  }

  public String getSecurityPolicyFile() {
    return c.getSecurityPolicyFile();
  }

  public boolean getSecurityPolicyOverrideEnabled() {
    return c.getSecurityPolicyOverrideEnabled();
  }

  public String getServerName() {
    return c.getServerName();
  }

  public String getSystemDelay() {
    return c.getSystemDelay();
  }

  public String getSystemLogger() {
    return c.getSystemLogger();
  }

  public String getTablePrefix() {
    return c.getTablePrefix();
  }

  public String getUrl() {
    return c.getUrl();
  }

  public void setAuditTrailExpiration(String s) {
    c.setAuditTrailExpiration(s);
  }

  public void setAuditTrailLogger(String s) {
    c.setAuditTrailLogger(s);
  }

  public void setBpmBackwardCompatibilityTo70Enabled(boolean bpmBackwardCompatibilityTo70Enabled) {
    c.setBpmBackwardCompatibilityTo70Enabled(bpmBackwardCompatibilityTo70Enabled);
  }

  public void setCacheSize(int i) {
    c.setCacheSize(i);
  }

  public void setCacheType(CacheType cacheType) {
    c.setCacheType(cacheType);
  }

  public void setClientLogger(String s) {
    c.setClientLogger(s);
  }

  public void setClusterAddress(String s) {
    c.setClusterAddress(s);
  }

  public void setClusterEnabled(boolean b) {
    c.setClusterEnabled(b);
  }

  public void setClusterNetworking(boolean b) {
    c.setClusterNetworking(b);
  }

  public void setClusterPort(int i) {
    c.setClusterPort(i);
  }

  public void setConcurrencyLevel(int i) {
    c.setConcurrencyLevel(i);
  }

  public void setCreateRegistry(boolean b) {
    c.setCreateRegistry(b);
  }

  public void setDashboardFromAddress(String s) {
    setDashboardFromAddress(s);
  }

  public void setDashboardMailServer(String s) {
    c.setDashboardMailServer(s);
  }

  public void setDashboardUrl(String s) {
    c.setDashboardUrl(s);
  }

  public void setDatabaseProperties(Properties properties) {
    c.setDatabaseProperties(properties);
  }

  public void setDatabaseType(DatabaseType databaseType) {
    c.setDatabaseType(databaseType);
  }

  public void setDataSource(String s) {
    c.setDataSource(s);
  }

  public void setDataSourceCaching(boolean b) {
    c.setDataSourceCaching(b);
  }

  public void setDataSourcePassword(String s) {
    c.setDataSourcePassword(s);
  }

  public void setDataSourceUsername(String s) {
    c.setDataSourceUsername(s);
  }

  public void setDataSourceUserTransactions(boolean b) {
    c.setDataSourceUserTransactions(b);
  }

  public void setDataSourceUserTransactionClientJndiName(String s) {
    c.setDataSourceUserTransactionClientJndiName(s);
  }

  public void setDataSourceUserTransactionServerJndiName(String s) {
    c.setDataSourceUserTransactionServerJndiName(s);
  }

  public void setDataSourceUserTransactionTimeout(String s) {
    c.setDataSourceUserTransactionTimeout(s);
  }

  public void setDriver(String s) {
    c.setDriver(s);
  }

  public void setEngineContentsSummaryFrequency(String s) {
    c.setEngineContentsSummaryFrequency(s);
  }

  public void setFailoverTimeWindow(String s) {
    c.setFailoverTimeWindow(s);
  }

  public void setHeartbeatFrequency(String s) {
    c.setHeartbeatFrequency(s);
  }

  public void setHost(String s) {
    c.setHost(s);
  }

  public void setInitialContextFactory(String s) {
    c.setInitialContextFactory(s);
  }

  public void setInitialContextPassword(String s) {
    c.setInitialContextPassword(s);
  }

  public void setInitialContextUsername(String s) {
    c.setInitialContextUsername(s);
  }

  public void setInternalLoggerLevel(Level level) {
    c.setInternalLoggerLevel(level);
  }

  public void setInternalLoggerFileDirectory(String s) {
    c.setInternalLoggerFileDirectory(s);
  }

  public void setInternalLoggerFileRotationSize(long l) {
    c.setInternalLoggerFileRotationSize(l);
  }

  public void setJdbcConnectionRecycleFrequency(String s) {
    c.setJdbcConnectionRecycleFrequency(s);
  }

  public void setJdbcPassword(String s) {
    c.setJdbcPassword(s);
  }

  public void setJdbcUsername(String s) {
    c.setJdbcUsername(s);
  }

  public void setJmsInboundMessageConfiguration(Set set) {
    c.setJmsInboundMessageConfiguration(set);
  }

  public void setFairnessTimeWindow(String s) {
    c.setFairnessTimeWindow(s);
  }

  public void setFlowChartLogger(String s) {
    c.setFlowChartLogger(s);
  }

  public void setListenerClassLoaderEnabled(boolean b) {
    c.setListenerClassLoaderEnabled(b);
  }

  public void setLogExpiration(String s) {
    c.setLogExpiration(s);
  }

  public void setLoggerType(LoggerType loggerType) {
    c.setLoggerType(loggerType);
  }

  public void setLoggerTypes(Set loggerTypes) throws IllegalArgumentException {
    c.setLoggerTypes(loggerTypes);
  }

  public void setMaxConnections(int i) {
    c.setMaxConnections(i);
  }

  public void setName(String s) {
    c.setName(s);
  }

  public void setOracleLargeObjectAdapter(String s) {
    c.setOracleLargeObjectAdapter(s);
  }

  public void setProviderUrl(String s) {
    c.setProviderUrl(s);
  }

  public void setPublisherBootstrapConfiguration(Set set) {
    c.setPublisherBootstrapConfiguration(set);
  }

  public void setRegistryPort(int i) {
    c.setRegistryPort(i);
  }

  public void setRmiPort(int i) {
    c.setRmiPort(i);
  }

  public void setRmiServer(boolean b) {
    c.setRmiServer(b);
  }

  public void setRuntimeConfiguration(RuntimeConfigurationNode runtimeConfigurationNode) {
    c.setRuntimeConfiguration(runtimeConfigurationNode);
  }

  public void setRuntimeConfigurationFile(String s) {
    c.setRuntimeConfigurationFile(s);
  }

  public void setRuntimeConfigurationFileRefreshFrequency(String s) {
    c.setRuntimeConfigurationFileRefreshFrequency(s);
  }

  public void setScriptingLanguages(Map map) {
    c.setScriptingLanguages(map);
  }

  public void setSecurityConfigurationFile(String s) {
    c.setSecurityConfigurationFile(s);
  }

  public void setSecurityConfigurationFileEntry(String s) {
    c.setSecurityConfigurationFileEntry(s);
  }

  public void setSecurityEnabled(boolean b) {
    c.setSecurityEnabled(b);
  }

  public void setSecurityPolicyFile(String s) {
    c.setSecurityPolicyFile(s);
  }

  public void setSecurityPolicyOverrideEnabled(boolean b) {
    c.setSecurityPolicyOverrideEnabled(b);
  }

  public void setServerName(String s) {
    c.setServerName(s);
  }

  public void setSystemDelay(String s) {
    c.setSystemDelay(s);
  }

  public void setSystemLogger(String s) {
    c.setSystemLogger(s);
  }

  public void setTablePrefix(String s) {
    c.setTablePrefix(s);
  }

  public void setUrl(String s) {
    c.setUrl(s);
  }

  public Object clone() throws CloneNotSupportedException {
    return c.clone();
  }

  public JmsInboundMessageConfiguration makeJmsInboundMessageConfiguration() {
    return c.makeJmsInboundMessageConfiguration();
  }

  public Publisher makePublisher() {
    return c.makePublisher();
  }

  public void verify() throws EngineException {
    c.verify();
  }

} // class ConfigurationBean
