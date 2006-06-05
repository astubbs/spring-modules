/*
 * Copyright 2006 GigaSpaces Technologies. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED INCLUDING BUT NOT LIMITED TO WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT. GIGASPACES WILL NOT BE LIABLE FOR ANY DAMAGE OR
 * LOSS IN CONNECTION WITH THE SOFTWARE.
 */
package org.springmodules.javaspaces.gigaspaces;

import org.springframework.dao.support.DaoSupport;


import com.j_spaces.core.IJSpace;

/**
 * <p>
 * Support class, intended for extension by application developer
 * for writing Data Access Objects which perform domain-level
 * operations on the supplied space.
 * In order to operate, the Dao object should be injected either with a pre instantiated
 * GigaSpacesTemplate, or with an IJSpace and an IPojoToEntryConverter
 * implementation. Extending classes will typically use the
 * getGigaSpaceTemplate() method for performing space operations,
 * but direct access to the space is also possible.
 * </p>
 *
 * @author Zvika Markfeld
 * @version 5.0
 * Copyright:    Copyright 2006 GigaSpaces Technologies. All rights reserved.
 * Company:      Gigaspaces Technologies
 */
public class GigaSpacesDaoSupport extends DaoSupport {
	private GigaSpacesTemplate template;
	private IJSpace space;

	/**
	 * Gets the associated GigaSpacesTemplate. Extending classes will
	 * typically use this method for performing space operations
	 */
	public GigaSpacesTemplate getGigaSpaceTemplate() {
		return template;
	}

	/**
	 * Sets the associated GigaSpacesTemplate
	 * @param template
	 */
	public void setGigaSpaceTemplate(GigaSpacesTemplate template) {
		this.template = template;
	}

	/**
	 * Gets the associated Space object
	 * @return
	 */
	public IJSpace getSpace() {
		return (template != null) ? (IJSpace)template.getSpace() : space;
	}

	/**
	 * Sets the associated Space object
	 * @param space
	 */
	public void setSpace(IJSpace space) {
		this.space = space;
	}

	/**
	 * Verifies that either template or space were passed in, but not both.
	 * if space was passed in.
	 *
	 */
	protected void checkDaoConfig() throws IllegalArgumentException {
		if(template != null && space != null) {
			throw new IllegalArgumentException("either template or space properties must be specified, but not both");
		}
		else if(template == null && space == null) {
			throw new IllegalArgumentException("either template or space properties must be specified");
		}
	}

	/**
	 * Creates a template based on the space.
	 */
	protected void initDao() throws Exception {
		if(template == null) {
			template = new GigaSpacesTemplate(space);
		}
	}
}
