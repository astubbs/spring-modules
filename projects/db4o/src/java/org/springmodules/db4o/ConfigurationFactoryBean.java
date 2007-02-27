/**
 * Created on Nov 5, 2005
 *
 * $Id: ConfigurationFactoryBean.java,v 1.1 2007/02/27 16:43:57 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.db4o;

import java.io.PrintStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Constants;

import com.db4o.Db4o;
import com.db4o.config.Alias;
import com.db4o.config.Configuration;
import com.db4o.io.IoAdapter;
import com.db4o.reflect.Reflector;

/**
 * Factory Bean used for settings db4o {@link com.db4o.config.Configuration}
 * object.
 * 
 * <p/> By default, this class will use a 'fresh' configuration. However, it's
 * possible to use this FactoryBean to modify the global Configuration object for the
 * running JVM session or clone the existing one. It is also possible to pass a
 * Configuration object directly to this FactoryBean, which bypasses the
 * creation process.
 * 
 * This class is needed as db4o configuration object doesn't respect JavaBeans
 * conventions and thus Spring can't inject the properties.
 * 
 * @author Costin Leau
 * 
 */
public class ConfigurationFactoryBean implements InitializingBean, FactoryBean {

	private static final Log log = LogFactory.getLog(ConfigurationFactoryBean.class);

	/**
	 * Constant indicating that this factory works on a new/fresh db4o
	 * configuration.
	 */
	public static final int CONFIGURATION_NEW = 2 ^ 0;

	/**
	 * Constant indicating that this factory works on a JVM-cloned db4o
	 * configuration.
	 */
	public static final int CONFIGURATION_CLONED = 2 ^ 1;

	/**
	 * Constant indicating that this factory works on the global JVM db4o
	 * configuration.
	 */
	public static final int CONFIGURATION_GLOBAL = 2 ^ 2;

	private static final String CREATION_MODE_PREFIX = "CONFIGURATION_";

	private static final Constants CONFIGURATION_CREATION_MODE = new Constants(ConfigurationFactoryBean.class);

	/**
	 * Configuration to use.
	 */
	private Configuration configuration;

	/**
	 * Configuration creation mode.
	 */
	private int configurationCreationMode = CONFIGURATION_NEW;

	// db4o configuration defaults
	private Integer activationDepth = null;

	private Alias[] aliases = new Alias[0];

	private Boolean allowVersionUpdates = null;

	private Boolean automaticShutDown = null;

	private Integer blockSize = null;

	private Integer bTreeCacheHeight = null;

	private Integer bTreeNodeSize = null;

	private Boolean callbacks = null;

	private Boolean callConstructors = null;

	private Boolean classActivationDepthConfigurable = null;

	private Boolean detectSchemaChanges = null;

	private Boolean disableCommitRecovery = null;

	private Boolean exceptionsOnNotStorable = null;

	private Boolean flushFileBuffers = null;

	private Integer generateUUIDs = null;

	private Integer generateVersionNumbers = null;

	private IoAdapter ioAdapter = null;

	private Boolean lockDatabaseFile = null;

	private String markTransient = null;

	private Integer messageLevel = null;

	private Boolean optimizeNativeQueries = null;

	private Boolean readOnly = null;

	private Reflector reflector = null;

	private Long reserveStorageSpace = null;

	private String blobPath = null;

	private PrintStream out = null;

	private Boolean testConstructors = null;

	private Boolean unicode = null;

	private Integer updateDepth = null;

	private Integer weakReferenceCollectionInterval = null;

	private Boolean weakReferences = null;

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {

		if (configuration == null) {
			log.debug("no configuration given; creating one...");
			configuration = createConfiguration(configurationCreationMode);
		}

		if (activationDepth != null)
			configuration.activationDepth(activationDepth.intValue());
		if (allowVersionUpdates != null)
			configuration.allowVersionUpdates(allowVersionUpdates.booleanValue());
		if (automaticShutDown != null)
			configuration.automaticShutDown(automaticShutDown.booleanValue());
		if (blockSize != null)
			configuration.blockSize(blockSize.intValue());
		if (bTreeCacheHeight != null)
			configuration.bTreeCacheHeight(bTreeCacheHeight.intValue());
		if (bTreeNodeSize != null)
			configuration.bTreeNodeSize(bTreeNodeSize.intValue());
		if (callbacks != null)
			configuration.callbacks(callbacks.booleanValue());
		if (callConstructors != null)
			configuration.callConstructors(callConstructors.booleanValue());
		if (classActivationDepthConfigurable != null)
			configuration.classActivationDepthConfigurable(classActivationDepthConfigurable.booleanValue());
		if (detectSchemaChanges != null)
			configuration.detectSchemaChanges(detectSchemaChanges.booleanValue());
		if (disableCommitRecovery != null)
			if (disableCommitRecovery.booleanValue())
				configuration.disableCommitRecovery();
		if (exceptionsOnNotStorable != null)
			configuration.exceptionsOnNotStorable(exceptionsOnNotStorable.booleanValue());
		if (flushFileBuffers != null)
			configuration.flushFileBuffers(flushFileBuffers.booleanValue());
		if (generateUUIDs != null)
			configuration.generateUUIDs(generateUUIDs.intValue());
		if (generateVersionNumbers != null)
			configuration.generateVersionNumbers(generateVersionNumbers.intValue());
		if (ioAdapter != null)
			configuration.io(ioAdapter);
		if (markTransient != null)
			configuration.markTransient(markTransient);
		if (messageLevel != null)
			configuration.messageLevel(messageLevel.intValue());

		if (optimizeNativeQueries != null)
			configuration.optimizeNativeQueries(optimizeNativeQueries.booleanValue());

		if (lockDatabaseFile != null)
			configuration.lockDatabaseFile(lockDatabaseFile.booleanValue());
		if (readOnly != null)
			configuration.readOnly(readOnly.booleanValue());
		if (reflector != null)
			configuration.reflectWith(reflector);
		if (reserveStorageSpace != null)
			configuration.reserveStorageSpace(reserveStorageSpace.longValue());
		if (blobPath != null)
			configuration.setBlobPath(blobPath);

		if (out != null)
			configuration.setOut(out);
		if (testConstructors != null)
			configuration.testConstructors(testConstructors.booleanValue());
		if (unicode != null)
			configuration.unicode(unicode.booleanValue());
		if (updateDepth != null)
			configuration.updateDepth(updateDepth.intValue());
		if (weakReferenceCollectionInterval != null)
			configuration.weakReferenceCollectionInterval(weakReferenceCollectionInterval.intValue());
		if (weakReferences != null)
			configuration.weakReferences(weakReferences.booleanValue());

		for (int i = 0; aliases != null && i < aliases.length; i++) {
			configuration.addAlias(aliases[i]);
		}
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return configuration;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return (configuration == null ? Configuration.class : configuration.getClass());
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Creates a db4o configuration based on the configuration mode.
	 * 
	 * @param configuration mode
	 * 
	 * @return a db4o configuration object
	 */
	protected Configuration createConfiguration(int creationMode) {
		switch (creationMode) {
		case CONFIGURATION_NEW:
			log.info("using a new db4o configuration");
			return Db4o.newConfiguration();
		case CONFIGURATION_CLONED:
			log.info("using a jvm-cloned db4o configuration");
			return Db4o.cloneConfiguration();
		case CONFIGURATION_GLOBAL:
			log.info("using the global jvm db4o configuration");
			return Db4o.configure();
		default:
			// just in case
			throw new IllegalArgumentException("unknown creation mode:" + creationMode);
		}

	}

	/**
	 * @param activationDepth The activationDepth to set.
	 */
	public void setActivationDepth(Integer activationDepth) {
		this.activationDepth = activationDepth;
	}

	/**
	 * @param automaticShutDown The automaticShutDown to set.
	 */
	public void setAutomaticShutDown(Boolean automaticShutDown) {
		this.automaticShutDown = automaticShutDown;
	}

	/**
	 * @param blobPath The blobPath to set.
	 */
	public void setBlobPath(String blobPath) {
		this.blobPath = blobPath;
	}

	/**
	 * @param blockSize The blockSize to set.
	 */
	public void setBlockSize(Integer blockSize) {
		this.blockSize = blockSize;
	}

	/**
	 * @param callbacks The callbacks to set.
	 */
	public void setCallbacks(Boolean callbacks) {
		this.callbacks = callbacks;
	}

	/**
	 * @param callConstructors The callConstructors to set.
	 */
	public void setCallConstructors(Boolean callConstructors) {
		this.callConstructors = callConstructors;
	}

	/**
	 * @param classActivationDepthConfigurable The
	 * classActivationDepthConfigurable to set.
	 */
	public void setClassActivationDepthConfigurable(Boolean classActivationDepthConfigurable) {
		this.classActivationDepthConfigurable = classActivationDepthConfigurable;
	}

	/**
	 * Set the db4o configuration to use for this FactoryBean.
	 * 
	 * @param configuration The configuration to set.
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * @param detectSchemaChanges The detectSchemaChanges to set.
	 */
	public void setDetectSchemaChanges(Boolean detectSchemaChanges) {
		this.detectSchemaChanges = detectSchemaChanges;
	}

	/**
	 * @param exceptionsOnNotStorable The exceptionsOnNotStorable to set.
	 */
	public void setExceptionsOnNotStorable(Boolean exceptionsOnNotStorable) {
		this.exceptionsOnNotStorable = exceptionsOnNotStorable;
	}

	/**
	 * @param generateUUIDs The generateUUIDs to set.
	 */
	public void setGenerateUUIDs(Integer generateUUIDs) {
		this.generateUUIDs = generateUUIDs;
	}

	/**
	 * @param generateVersionNumbers The generateVersionNumbers to set.
	 */
	public void setGenerateVersionNumbers(Integer generateVersionNumbers) {
		this.generateVersionNumbers = generateVersionNumbers;
	}

	/**
	 * @param ioAdapter The ioAdapter to set.
	 */
	public void setIoAdapter(IoAdapter ioAdapter) {
		this.ioAdapter = ioAdapter;
	}

	/**
	 * @param lockDatabaseFile The lockDatabaseFile to set.
	 */
	public void setLockDatabaseFile(Boolean lockDatabaseFile) {
		this.lockDatabaseFile = lockDatabaseFile;
	}

	/**
	 * @param markTransient The markTransient to set.
	 */
	public void setMarkTransient(String markTransient) {
		this.markTransient = markTransient;
	}

	/**
	 * @param messageLevel The messageLevel to set.
	 */
	public void setMessageLevel(Integer messageLevel) {
		this.messageLevel = messageLevel;
	}

	/**
	 * @param readOnly The readOnly to set.
	 */
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * @param reflector The reflector to set.
	 */
	public void setReflector(Reflector reflector) {
		this.reflector = reflector;
	}

	/**
	 * @param reserveStorageSpace The reserveStorageSpace to set.
	 */
	public void setReserveStorageSpace(Long reserveStorageSpace) {
		this.reserveStorageSpace = reserveStorageSpace;
	}

	/**
	 * @param aliases The aliases to set.
	 */
	public void setAliases(Alias[] aliases) {
		this.aliases = aliases;
	}

	/**
	 * @param allowVersionUpdates The allowVersionUpdates to set.
	 */
	public void setAllowVersionUpdates(Boolean allowVersionUpdates) {
		this.allowVersionUpdates = allowVersionUpdates;
	}

	/**
	 * @param treeCacheHeight The bTreeCacheHeight to set.
	 */
	public void setBTreeCacheHeight(Integer treeCacheHeight) {
		bTreeCacheHeight = treeCacheHeight;
	}

	/**
	 * @param treeNodeSize The bTreeNodeSize to set.
	 */
	public void setBTreeNodeSize(Integer treeNodeSize) {
		bTreeNodeSize = treeNodeSize;
	}

	/**
	 * @param disableCommitRecovery The disableCommitRecovery to set.
	 */
	public void setDisableCommitRecovery(Boolean disableCommitRecovery) {
		this.disableCommitRecovery = disableCommitRecovery;
	}

	/**
	 * @param flushFileBuffers The flushFileBuffers to set.
	 */
	public void setFlushFileBuffers(Boolean flushFileBuffers) {
		this.flushFileBuffers = flushFileBuffers;
	}

	/**
	 * @param optimizeNativeQueries The optimizeNativeQueries to set.
	 */
	public void setOptimizeNativeQueries(Boolean optimizeNativeQueries) {
		this.optimizeNativeQueries = optimizeNativeQueries;
	}

	/**
	 * @param out The out to set.
	 */
	public void setOut(PrintStream out) {
		this.out = out;
	}

	/**
	 * @param testConstructors The testConstructors to set.
	 */
	public void setTestConstructors(Boolean testConstructors) {
		this.testConstructors = testConstructors;
	}

	/**
	 * @param unicode The unicode to set.
	 */
	public void setUnicode(Boolean unicode) {
		this.unicode = unicode;
	}

	/**
	 * @param updateDepth The updateDepth to set.
	 */
	public void setUpdateDepth(Integer updateDepth) {
		this.updateDepth = updateDepth;
	}

	/**
	 * @param weakReferenceCollectionInterval The
	 * weakReferenceCollectionInterval to set.
	 */
	public void setWeakReferenceCollectionInterval(Integer weakReferenceCollectionInterval) {
		this.weakReferenceCollectionInterval = weakReferenceCollectionInterval;
	}

	/**
	 * @param weakReferences The weakReferences to set.
	 */
	public void setWeakReferences(Boolean weakReferences) {
		this.weakReferences = weakReferences;
	}

	/**
	 * Set the configuration creation mode. This factoryBean can work on a newly
	 * created configuration, a JVM cloned one or directly on the global JVM
	 * configuration.
	 * 
	 * @see #CONFIGURATION_NEW
	 * @see #CONFIGURATION_CLONED
	 * @see #CONFIGURATION_GLOBAL
	 * @see #setConfigurationCreationModeNumber(int)
	 * 
	 * @param mode the configuration mode as a string
	 */
	public void setConfigurationCreationMode(String mode) {
		if (mode != null) {
			mode = mode.toUpperCase();
			if (!mode.startsWith(CREATION_MODE_PREFIX))
				mode = CREATION_MODE_PREFIX + mode;
			this.configurationCreationMode = CONFIGURATION_CREATION_MODE.asNumber(mode).intValue();
		}

	}

	/**
	 * Set the configuration creation mode.
	 * 
	 * @param mode the configuration mode as an int
	 */
	public void setConfigurationCreationModeNumber(int mode) {
		if (!CONFIGURATION_CREATION_MODE.getValues(CREATION_MODE_PREFIX).contains(new Integer(mode)))
			throw new IllegalArgumentException("invalid configurationCreationModeNumber");
		this.configurationCreationMode = mode;
	}

}
