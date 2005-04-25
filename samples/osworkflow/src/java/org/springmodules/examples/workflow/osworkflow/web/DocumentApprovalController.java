
package org.springmodules.examples.workflow.osworkflow.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springmodules.workflow.osworkflow.OsWorkflowTemplate;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * @author robh
 */
public class DocumentApprovalController extends MultiActionController {

	private OsWorkflowTemplate template;

	public void setTemplate(OsWorkflowTemplate template) {
		this.template = template;
	}

	public ModelAndView start(HttpServletRequest request, HttpServletResponse response) {
		template.initialize();
		return status(request, response);
	}

	public ModelAndView status(HttpServletRequest request, HttpServletResponse response) {
		Map model = new HashMap();
		model.put("instanceId", new Long(this.template.getContextManager().getInstanceId()));
		model.put("currentSteps", this.template.getCurrentStepDescriptors());
		model.put("historyStep", this.template.getHistoryStepDescriptors());
		model.put("availableActions", this.template.getAvailableActionDescriptors());
		return new ModelAndView("status", model);
	}
}
