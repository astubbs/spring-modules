
package org.springmodules.examples.workflow.osworkflow.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springmodules.examples.workflow.osworkflow.service.DocumentApprovalWorkflow;

import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author robh
 */
public class WorkflowController extends MultiActionController {

	private DocumentApprovalWorkflow workflow;

	public void setWorkflow(DocumentApprovalWorkflow workflow) {
		this.workflow = workflow;
	}

	public ModelAndView start(HttpServletRequest request, HttpServletResponse response) {
    workflow.start();
		return new ModelAndView("status");
	}
	public ModelAndView doUpload(HttpServletRequest request, HttpServletResponse response) {
		workflow.uploadDocument();
		return new ModelAndView("status");
	}
	
}
