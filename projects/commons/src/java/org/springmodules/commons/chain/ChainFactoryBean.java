/*
 * Copyright 2002-2006 the original author or authors.
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
package org.springmodules.commons.chain;

import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.impl.ChainBase;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Simple FactoryBean used for simplifying configuring a catalog inside a Spring
 * XML configuration.
 * 
 * @author Costin Leau
 * 
 */
public class ChainFactoryBean extends AbstractFactoryBean {

	private Chain chain;
	private Command[] commands;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.config.AbstractFactoryBean#createInstance()
	 */
	protected Object createInstance() throws Exception {
		return new ChainBase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return (chain == null ? Chain.class : chain.getClass());
	}

	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();

		if (commands == null)
			throw new IllegalArgumentException("commands parameter is required");

		for (int i = 0; i < commands.length; i++) {
			chain.addCommand(commands[i]);
		}
	}

	public Command[] getCommands() {
		return commands;
	}

	/**
	 * Set the commands that have to be configured for this catalog.
	 * 
	 * @param commands
	 */
	public void setCommands(Command[] commands) {
		this.commands = commands;
	}

}
