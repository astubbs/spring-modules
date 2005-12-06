/**
 * Created on Oct 11, 2005
 *
 * $Id: OsWorkflow28Template.java,v 1.1 2005/12/06 10:59:30 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.workflow.osworkflow;

import com.opensymphony.workflow.AbstractWorkflow;
import com.opensymphony.workflow.TypeResolver;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;

/**
 * Extension of the default OsWorkflow28Template to allow OSWorkflow 2.8+ TypeResolvers.
 * In order to compile this class you need OSWorkflow 2.8+ inside the classpath.
 * 
 * @author Costin Leau
 *
 */
public class OsWorkflow28Template extends org.springmodules.workflow.osworkflow.OsWorkflowTemplate {

    private TypeResolver resolver;

    /**
     * @return Returns the resolver.
     */
    public TypeResolver getResolver() {
        return resolver;
    }

    /**
     * @param resolver The resolver to set.
     */
    public void setResolver(TypeResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * By default use SpringTypeResolver.
     * 
     * @see org.springmodules.workflow.osworkflow.OsWorkflow28Template#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (resolver == null)
            throw new IllegalArgumentException("resolver can't be null");
    }

    /**
     * @see org.springmodules.workflow.osworkflow.OsWorkflow28Template#createWorkflow(java.lang.String)
     */
    protected Workflow createWorkflow(String workflowName) throws WorkflowException {
        Workflow wf = super.createWorkflow(workflowName);
        if (wf instanceof AbstractWorkflow)
            ((AbstractWorkflow) wf).setResolver(resolver);
        return wf;
    }
}
