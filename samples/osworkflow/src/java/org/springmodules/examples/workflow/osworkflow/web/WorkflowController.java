
package org.springmodules.examples.workflow.osworkflow.web;

import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springmodules.examples.workflow.osworkflow.service.DocumentApprovalWorkflow;
import org.springmodules.workflow.osworkflow.WorkflowContext;
import org.springmodules.workflow.osworkflow.OsWorkflowTemplate;

import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author robh
 */
public class WorkflowController implements Controller {

	private OsWorkflowTemplate template;

	public void setTemplate(OsWorkflowTemplate template) {
		this.template = template;
	}

	public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
		
	}

}
