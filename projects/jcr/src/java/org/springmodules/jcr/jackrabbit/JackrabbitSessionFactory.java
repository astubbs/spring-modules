/*
 * Copyright 2002-2007 the original author or authors.
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
package org.springmodules.jcr.jackrabbit;

import javax.jcr.Workspace;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.api.JackrabbitNodeTypeManager;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springmodules.jcr.JcrSessionFactory;

/**
 * Jackrabbit specific {@link JcrSessionFactory} which allows registration of
 * node types in <a
 * href="http://jackrabbit.apache.org/doc/nodetype/cnd.html">CND</a> format.
 * 
 * @author Costin Leau
 * 
 */
public class JackrabbitSessionFactory extends JcrSessionFactory {

	private static final Log log = LogFactory.getLog(JackrabbitSessionFactory.class);

	/**
	 * Node definitions in CND format.
	 */
	private Resource[] nodeDefinitions;

	private String contentType = JackrabbitNodeTypeManager.TEXT_XML;

	/*
	 * (non-Javadoc)
	 * @see org.springmodules.jcr.JcrSessionFactory#registerNodeTypes()
	 */
	protected void registerNodeTypes() throws Exception {
		if (!ObjectUtils.isEmpty(nodeDefinitions)) {
			Workspace ws = getSession().getWorkspace();

			// Get the NodeTypeManager from the Workspace.
			// Note that it must be cast from the generic JCR NodeTypeManager to
			// the
			// Jackrabbit-specific implementation.
			JackrabbitNodeTypeManager nodeTypeManager = (JackrabbitNodeTypeManager) ws.getNodeTypeManager();

			boolean debug = log.isDebugEnabled();
			for (int i = 0; i < nodeDefinitions.length; i++) {
				Resource resource = nodeDefinitions[i];
				if (debug)
					log.debug("adding node type definitions from " + resource.getDescription());

				nodeTypeManager.registerNodeTypes(resource.getInputStream(), contentType);
			}
		}
	}

	/**
	 * @param nodeDefinitions The nodeDefinitions to set.
	 */
	public void setNodeDefinitions(Resource[] nodeDefinitions) {
		this.nodeDefinitions = nodeDefinitions;
	}

	/**
	 * Indicate the node definition content type (by default,
	 * JackrabbitNodeTypeManager#TEXT_XML).
	 * 
	 * @see JackrabbitNodeTypeManager#TEXT_X_JCR_CND
	 * @see JackrabbitNodeTypeManager#TEXT_XML
	 * 
	 * @param contentType The contentType to set.
	 */
	public void setContentType(String contentType) {
		Assert.hasText(contentType, "contentType is required");
		this.contentType = contentType;
	}

}
