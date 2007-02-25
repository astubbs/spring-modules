/**
 * Created on Nov 5, 2005
 *
 * $Id: ConfigurationFactoryBean.java,v 1.1 2007/02/25 18:45:10 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.orm.db4o;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.db4o.Db4o;
import com.db4o.config.Configuration;
import com.db4o.io.IoAdapter;
import com.db4o.reflect.Reflector;

/**
 * Factory Bean used for settings db4o Configuration object. Note that Configuration are
 * singleton (one per VM) and changing the parameters affects all db4o engines started afterwards
 * inside that VM. This class is needed as db4o configuration object doesn't respect
 * JavaBeans conventions and thus Spring can't inject the properties.
 *  
 * @author Costin Leau
 *
 */
public class ConfigurationFactoryBean implements InitializingBean, FactoryBean {

	private Configuration configuration;

	// db4o configuration defaults
	private Integer activationDepth = null;
	private Boolean automaticShutDown = null;
	private Integer blockSize = null;
	private Boolean callbacks = null;
	private Boolean callConstructors = null;
	private Boolean classActivationDepthConfigurable = null;
	private Boolean detectSchemaChanges = null;
	private Integer discardFreeSpace = null;
	private Boolean encrypt = null;
	private Boolean exceptionsOnNotStorable = null;
	private Integer generateUUIDs = null;
	private Integer generateVersionNumbers = null;
	private IoAdapter ioAdapter = null;
	private String markTransient = null;
	private Integer messageLevel = null;
	private Boolean lockDatabaseFile = null;
	private String password = null;
	private Boolean readOnly = null;
	private Reflector reflector = null;
	private Long reserveStorageSpace = null;
	private String blobPath = null;

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Configuration config = Db4o.configure();
		if (activationDepth != null)
			config.activationDepth(activationDepth.intValue());
		if (automaticShutDown != null)
			config.automaticShutDown(automaticShutDown.booleanValue());
		if (blockSize != null)
			config.blockSize(blockSize.intValue());
		if (callbacks != null)
			config.callbacks(callbacks.booleanValue());
		if (callConstructors != null)
			config.callConstructors(callConstructors.booleanValue());
		if (classActivationDepthConfigurable != null)
			config.classActivationDepthConfigurable(classActivationDepthConfigurable.booleanValue());
		if (detectSchemaChanges != null)
			config.detectSchemaChanges(detectSchemaChanges.booleanValue());
		if (discardFreeSpace != null)
			config.discardFreeSpace(discardFreeSpace.intValue());
		if (encrypt != null)
			config.encrypt(encrypt.booleanValue());
		if (exceptionsOnNotStorable != null)
			config.exceptionsOnNotStorable(exceptionsOnNotStorable.booleanValue());
		if (generateUUIDs != null)
			config.generateUUIDs(generateUUIDs.intValue());
		if (generateVersionNumbers != null)
			config.generateVersionNumbers(generateVersionNumbers.intValue());
		if (ioAdapter != null)
			config.io(ioAdapter);
		if (markTransient != null)
			config.markTransient(markTransient);
		if (messageLevel != null)
			config.messageLevel(messageLevel.intValue());
		if (lockDatabaseFile != null)
			config.lockDatabaseFile(lockDatabaseFile.booleanValue());
		if (password != null)
			config.password(password);
		if (readOnly != null)
			config.readOnly(readOnly.booleanValue());
		if (reflector != null)
			config.reflectWith(reflector);
		if (reserveStorageSpace != null)
			config.reserveStorageSpace(reserveStorageSpace.longValue());
		if (blobPath != null)
			config.setBlobPath(blobPath);
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
	 * @param classActivationDepthConfigurable The classActivationDepthConfigurable to set.
	 */
	public void setClassActivationDepthConfigurable(Boolean classActivationDepthConfigurable) {
		this.classActivationDepthConfigurable = classActivationDepthConfigurable;
	}

	/**
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
	 * @param discardFreeSpace The discardFreeSpace to set.
	 */
	public void setDiscardFreeSpace(Integer discardFreeSpace) {
		this.discardFreeSpace = discardFreeSpace;
	}

	/**
	 * @param encrypt The encrypt to set.
	 */
	public void setEncrypt(Boolean encrypt) {
		this.encrypt = encrypt;
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
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
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

}
