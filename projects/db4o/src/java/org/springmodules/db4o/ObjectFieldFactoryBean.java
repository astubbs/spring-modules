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

import com.db4o.config.ObjectClass;
import com.db4o.config.ObjectField;

/**
 * {@link org.springframework.beans.factory.FactoryBean} used for constructing
 * an db4o {@link com.db4o.config.ObjectField}.
 * 
 * <p>
 * This <code>FactoryBean</code> will construct a <code>ObjectField</code> for
 * the {@link #setFieldName(String) specified field name} on the 
 * {@link #setObjectClass(ObjectClass) specified ObjectClass}.
 * </p>
 * 
 * <p>
 * By default, this <code>FactoryBean</code> will return new instances of
 * <code>ObjectField</code> (i.e. this <code>FactoryBean</code>  isn't a
 * {@link #isSingleton() singleton}).
 * </p>
 *
 * <p>
 * This class is needed as db4o configuration doesn't respect JavaBeans
 * conventions and thus Spring can't inject the properties.
 * </p>
 *
 * @see org.springmodules.db4o.ObjectClassFactoryBean
 *
 * @since 0.9
 */
public class ObjectFieldFactoryBean implements InitializingBean, FactoryBean {

	/**
	 * The {@link com.db4o.config.ObjectClass} from which to create an
	 * {@link com.db4o.config.ObjectField}.
	 */
	private ObjectClass objectClass;

	/**
	 * The {@link com.db4o.config.ObjectField} that will be created.
	 */
	private ObjectField objectField;

	/**
	 * The name of the field of the {@link com.db4o.config.ObjectField}.
	 */
	private String fieldName;

	/**
	 * Configuration settings to apply to the {@link #ObjectField}
	 */
	private Boolean cascadeOnActivate;
	private Boolean cascadeOnDelete;
	private Boolean cascadeOnUpdate;
	private Boolean indexed;
	private Boolean queryEvaluation;
	private String renameValue;

	/**
	 * {@inheritDoc}
	 *
	 * Create the {@link #objectField}.
	 */
	public void afterPropertiesSet() throws Exception {
		performAssertions();
		createObjectField();
	}

	/**
	 * Perform assertions for this class. Namely that {@link #objectClass} has
	 * been set.
	 */
	private void performAssertions() {
		Assert.notNull(this.objectClass, "Property objectClass cannot be null.");
	}

	/**
	 * Generate the {@link #objectFields} using the specified configuration.
	 */
	private void createObjectField() {
		this.objectField = this.objectClass.objectField(this.fieldName);

		if (this.cascadeOnActivate != null) {
			this.objectField.cascadeOnActivate(this.cascadeOnActivate.booleanValue());
		}
		if (this.cascadeOnDelete != null) {
			this.objectField.cascadeOnDelete(this.cascadeOnDelete.booleanValue());
		}
		if (this.cascadeOnUpdate != null) {
			this.objectField.cascadeOnUpdate(this.cascadeOnUpdate.booleanValue());
		}
		if (this.indexed != null) {
			this.objectField.indexed(this.indexed.booleanValue());
		}
		if (this.queryEvaluation != null) {
			this.objectField.queryEvaluation(this.queryEvaluation.booleanValue());
		}

		// post process the ObjectField
		if (StringUtils.hasText(this.renameValue)) {
			this.objectField.rename(this.renameValue);
		}
	}

	public Object getObject() throws Exception {
		return this.objectField;
	}

	public Class getObjectType() {
		return this.objectField.getClass();
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * This is set to <code>false</code> as multiple instances of an
	 * {@link com.db4o.config.ObjectField} with the same configuration are
	 * equivalent, however they may be configured separately by the receiver.
	 */
	public boolean isSingleton() {
		return false;
	}

	/**
	 * Set the {@link com.db4o.config.ObjectClass} for which this factory
	 * should construct an {@link com.db4o.config.ObjectField}.
	 *
	 * @param objectClass the <code>ObjectClass</code>. May not be <code>null</code>.
	 */
	public void setObjectClass(ObjectClass objectClass) {
		this.objectClass = objectClass;
	}

	/**
	 * Set the name of the field on the {@link #setObjectClass(ObjectClass)}
	 * for which this factory should create a {@link com.db4o.config.ObjectField}.
	 *
	 * @param fieldName the name of the field.  May not be <code>null</code>.
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @see com.db4o.config.ObjectField#cascadeOnActivate(boolean)
	 */
	public void setCascadeOnActivate(Boolean cascadeOnActivate) {
		this.cascadeOnActivate = cascadeOnActivate;
	}

	/**
	 * @see com.db4o.config.ObjectField#cascadeOnDelete(boolean)
	 */
	public void setCascadeOnDelete(Boolean cascadeOnDelete) {
		this.cascadeOnDelete = cascadeOnDelete;
	}

	/**
	 * @see com.db4o.config.ObjectField#cascadeOnUpdate(boolean)
	 */
	public void setCascadeOnUpdate(Boolean cascadeOnUpdate) {
		this.cascadeOnUpdate = cascadeOnUpdate;
	}


	/**
	 * @see com.db4o.config.ObjectField#indexed(boolean)
	 */
	public void setIndexed(Boolean indexed) {
		this.indexed = indexed;
	}

	/**
	 * @see com.db4o.config.ObjectField#queryEvaluation(boolean)
	 */
	public void setQueryEvaluation(Boolean queryEvaluation) {
		this.queryEvaluation = queryEvaluation;
	}

	/**
	 * @see com.db4o.config.ObjectField#rename(String)
	 */
	public void setRenameValue(String renameValue) {
		this.renameValue = renameValue;
	}

}
