/**
 * Created on Feb 18, 2006
 *
 * $Id: OsWorkflowTemplate.java,v 1.1 2006/02/23 13:42:56 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.workflow.osworkflow.v28;

import com.opensymphony.workflow.AbstractWorkflow;
import com.opensymphony.workflow.TypeResolver;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;

/**
 * Support for TypeResolver available in OsWorkflow 2.8.0. SpringTypResolver available inside the official OsWorkflow 2.8 distribution, 
 * allows osworkflow to obtain business logic components (conditions, functions, and so on) from Spring's ApplicationContext. 
 * 
 * @see com.opensymphony.workflow.TypeResolver
 * @see com.opensymphony.workflow.SpringTypeResolver
 * @since 0.3
 * @author Costin Leau
 *
 */
public class OsWorkflowTemplate extends org.springmodules.workflow.osworkflow.OsWorkflowTemplate {

	/**
	 * OsWorkflow Type typeResolver.
	 */
	private TypeResolver typeResolver;

	/**
	 * @return Returns the typeResolver.
	 */
	public TypeResolver getTypeResolver() {
		return typeResolver;
	}

	/**
	 * @param typeResolver The typeResolver to set.
	 */
	public void setTypeResolver(TypeResolver resolver) {
		this.typeResolver = resolver;
	}

	/**
	 * Injects the type resolver (if available) inside the workflow.
	 * 
	 * @see org.springmodules.workflow.osworkflow.OsWorkflowTemplate#createWorkflow(java.lang.String)
	 */
	protected Workflow createWorkflow(String caller) throws WorkflowException {
		Workflow workflow = super.createWorkflow(caller);
		if (typeResolver != null) {
			// inject the type resolver if there is such a case
			if (workflow instanceof AbstractWorkflow) {
				((AbstractWorkflow) workflow).setResolver(typeResolver);
			}
		}
		return workflow;
	}
}
