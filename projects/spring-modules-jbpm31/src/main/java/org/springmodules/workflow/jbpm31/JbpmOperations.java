/**
 * Created on Feb 28, 2006
 *
 * $Id: JbpmOperations.java,v 1.1 2006/03/02 14:56:01 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.workflow.jbpm31;

import java.util.List;

import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;

/**
 * @author Costin Leau
 *
 */
public interface JbpmOperations {

	public ProcessInstance findProcessInstance(final Long processInstanceId);

	public List findProcessInstances();

	public List findPooledTaskInstances(final String actorId);

	public List findPooledTaskInstances(final List actorIds);

	public List findTaskInstances(final String actorId);

	public List findTaskInstances(final String[] actorIds);

	public List findTaskInstances(final List actorIds);

	public List findTaskInstancesByToken(Token token);

	public List findTaskInstancesByToken(final long tokenId);

	public void signal(final ProcessInstance processInstance);

	public Long saveProcessInstance(final ProcessInstance processInstance);

	public void signal(final ProcessInstance processInstance, final String transitionId);

	public void signal(final ProcessInstance processInstance, final Transition transition);

}