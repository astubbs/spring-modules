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
package org.springmodules.db4o;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.db4o.Db4o;
import com.db4o.config.Configuration;
import com.db4o.config.ObjectClass;
import com.db4o.config.ObjectTranslator;

/**
 * {@link org.springframework.beans.factory.FactoryBean} used for constructing
 * db4o {@link com.db4o.config.ObjectClass}.
 * 
 * <p>
 * By default, this <code>FactoryBean</code> will return new instances of
 * <code>ObjectClass</code> (i.e. this <code>FactoryBean</code> isn't a
 * {@link #isSingleton() singleton}).
 * </p>
 * 
 * <p>
 * This class is needed as db4o configuration doesn't respect JavaBeans
 * conventions and thus Spring can't inject the properties.
 * </p>
 * 
 * <p>
 * If an explicit {@link com.db4o.config.Configuration} is
 * {@link #setConfiguration provided} then that will be used, otherwise a
 * {@link com.db4o.Db4o#configure() default configuration} will be used.
 * </p>
 * 
 * @see org.springmodules.db4o.ConfigurationFactoryBean
 * 
 * @since 0.9
 * 
 */
public class ObjectClassFactoryBean implements InitializingBean, FactoryBean {

	/**
	 * The {@link com.db4o.config.ObjectClass} that this factory will create.
	 */
	private ObjectClass objectClass;

	/**
	 * The class for which the {@link #objectClass} will be defined.
	 */
	private Class clazz;

	/**
	 * Configuration, which will be used to construct the objectClass.
	 */
	private Configuration configuration;

	/**
	 * Configuration settings to apply to the {@link #objectClass}
	 */
	private Boolean callConstructor;
	private Boolean cascadeOnActivate;
	private Boolean cascadeOnDelete;
	private Boolean cascadeOnUpdate;
	private Boolean enableReplication;
	private Boolean generateUUIDs;
	private Boolean generateVersionNumbers;
	private Boolean indexed;
	private Integer maximumActivationDepth;
	private Integer minimumActivationDepth;
	private Boolean persistStaticFieldValues;
	private String renameValue;
	private Boolean storeTransientFields;
	private ObjectTranslator objectTranslator;
	private Integer updateDepth;

	/**
	 * {@inheritDoc}
	 * 
	 * Create the {@link #objectClass}.
	 */
	public void afterPropertiesSet() throws Exception {
		performAssertions();
		determineConfiguration();
		createObjectClass();
	}

	/**
	 * Perform assertions for this class. Namely that {@link #clazz} has been
	 * set.
	 */
	private void performAssertions() {
		Assert.notNull(this.clazz, "Property clazz cannot be null.");
	}

	/**
	 * Determine the {@link com.db4o.config.Configuration} to use.
	 * 
	 * <p>
	 * If the {@link #configuration} is explicitly set, that will be used,
	 * otherwise a {@link Db4o#configure() default configuration} will be used.
	 */
	private void determineConfiguration() {
		if (this.configuration == null) {
			this.configuration = Db4o.configure();
		}
	}

	/**
	 * Generate the {@link #objectClass} using the specified configuration.
	 */
	private void createObjectClass() {
		this.objectClass = this.configuration.objectClass(this.clazz);
		if (this.callConstructor != null) {
			this.objectClass.callConstructor(this.callConstructor
					.booleanValue());
		}
		if (this.cascadeOnActivate != null) {
			this.objectClass.cascadeOnActivate(this.cascadeOnActivate
					.booleanValue());
		}
		if (this.cascadeOnDelete != null) {
			this.objectClass.cascadeOnDelete(this.cascadeOnDelete
					.booleanValue());
		}
		if (this.cascadeOnUpdate != null) {
			this.objectClass.cascadeOnUpdate(this.cascadeOnUpdate
					.booleanValue());
		}
		if (this.enableReplication != null) {
			this.objectClass.enableReplication(this.enableReplication
					.booleanValue());
		}
		if (this.generateUUIDs != null) {
			this.objectClass.generateUUIDs(this.generateUUIDs.booleanValue());
		}
		if (this.generateVersionNumbers != null) {
			this.objectClass.generateVersionNumbers(this.generateVersionNumbers
					.booleanValue());
		}
		if (this.indexed != null) {
			this.objectClass.indexed(this.indexed.booleanValue());
		}
		if (this.maximumActivationDepth != null) {
			this.objectClass.maximumActivationDepth(this.maximumActivationDepth
					.intValue());
		}
		if (this.minimumActivationDepth != null) {
			this.objectClass.minimumActivationDepth(this.minimumActivationDepth
					.intValue());
		}
		if (Boolean.TRUE.equals(this.persistStaticFieldValues)) {
			this.objectClass.persistStaticFieldValues();
		}
		if (this.storeTransientFields != null) {
			this.objectClass.storeTransientFields(this.storeTransientFields
					.booleanValue());
		}
		if (this.updateDepth != null) {
			this.objectClass.updateDepth(this.updateDepth.intValue());
		}

		// post process the objectClass
		if (StringUtils.hasText(this.renameValue)) {
			this.objectClass.rename(this.renameValue);
		}
		if (this.objectTranslator != null) {
			this.objectClass.translate(objectTranslator);
		}
	}

	public Object getObject() throws Exception {
		return this.objectClass;
	}

	public Class getObjectType() {
		return this.clazz;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * This is set to <code>false</code> as multiple instances of an
	 * {@link com.db4o.config.ObjectClass} with the same configuration are
	 * equivalent, however they may be configured separately by the receiver.
	 */
	public boolean isSingleton() {
		return false;
	}

	/**
	 * Set the {@link Class class} for which this factory should construct an
	 * {@link com.db4o.config.ObjectClass}.
	 * 
	 * @param clazz
	 *            the clazz. May not be <code>null</code>.
	 */
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	/**
	 * Set the {@link com.db4o.config.Configuration configuration} to be used to
	 * {@link com.db4o.config.Configuration#objectClass(Object) construct} the
	 * {@link com.db4o.config.ObjectClass}.
	 * 
	 * <p>
	 * If this is never explicitly called, the
	 * {@link com.db4o.Db4o#configure() default configuration} will be used.
	 * 
	 * @param configuration
	 *            the <code>configuration</code> to use.
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * @see com.db4o.config.ObjectClass#callConstructor(boolean)
	 */
	public void setCallConstructor(Boolean callConstructor) {
		this.callConstructor = callConstructor;
	}

	/**
	 * @see com.db4o.config.ObjectClass#cascadeOnActivate(boolean)
	 */
	public void setCascadeOnActivate(Boolean cascadeOnActivate) {
		this.cascadeOnActivate = cascadeOnActivate;
	}

	/**
	 * @see com.db4o.config.ObjectClass#cascadeOnDelete(boolean)
	 */
	public void setCascadeOnDelete(Boolean cascadeOnDelete) {
		this.cascadeOnDelete = cascadeOnDelete;
	}

	/**
	 * @see com.db4o.config.ObjectClass#cascadeOnUpdate(boolean)
	 */
	public void setCascadeOnUpdate(Boolean cascadeOnUpdate) {
		this.cascadeOnUpdate = cascadeOnUpdate;
	}

	/**
	 * @see com.db4o.config.ObjectClass#enableReplication(boolean)
	 */
	public void setEnableReplication(Boolean enableReplication) {
		this.enableReplication = enableReplication;
	}

	/**
	 * @see com.db4o.config.ObjectClass#generateUUIDs(boolean)
	 */
	public void setGenerateUUIDs(Boolean generateUUIDs) {
		this.generateUUIDs = generateUUIDs;
	}

	/**
	 * @see com.db4o.config.ObjectClass#generateVersionNumbers(boolean)
	 */
	public void setGenerateVersionNumbers(Boolean generateVersionNumbers) {
		this.generateVersionNumbers = generateVersionNumbers;
	}

	/**
	 * @see com.db4o.config.ObjectClass#indexed(boolean)
	 */
	public void setIndexed(Boolean indexed) {
		this.indexed = indexed;
	}

	/**
	 * @see com.db4o.config.ObjectClass#maximumActivationDepth(int)
	 */
	public void setMaximumActivationDepth(Integer maximumActivationDepth) {
		this.maximumActivationDepth = maximumActivationDepth;
	}

	/**
	 * @see com.db4o.config.ObjectClass#minimumActivationDepth(int)
	 */
	public void setMinimumActivationDepth(Integer minimumActivationDepth) {
		this.minimumActivationDepth = minimumActivationDepth;
	}

	/**
	 * @see com.db4o.config.ObjectClass#persistStaticFieldValues()
	 */
	public void setPersistStaticFieldValues(Boolean persistStaticFieldValues) {
		this.persistStaticFieldValues = persistStaticFieldValues;
	}

	/**
	 * @see com.db4o.config.ObjectClass#rename(String)
	 */
	public void setRenameValue(String renameValue) {
		this.renameValue = renameValue;
	}

	/**
	 * @see com.db4o.config.ObjectClass#storeTransientFields(boolean)
	 */
	public void setStoreTransientFields(Boolean storeTransientFields) {
		this.storeTransientFields = storeTransientFields;
	}

	/**
	 * @see com.db4o.config.ObjectClass#translate(ObjectTranslator)
	 */
	public void setObjectTranslator(ObjectTranslator objectTranslator) {
		this.objectTranslator = objectTranslator;
	}

	/**
	 * @see com.db4o.config.ObjectClass#updateDepth(int)
	 */
	public void setUpdateDepth(Integer updateDepth) {
		this.updateDepth = updateDepth;
	}

}
