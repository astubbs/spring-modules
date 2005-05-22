
package org.springmodules.examples.workflow.osworkflow.web;

import org.springmodules.examples.workflow.osworkflow.model.Comment;
import org.springmodules.examples.workflow.osworkflow.service.DocumentApprovalWorkflow;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author robh
 */
public class PassBackForm extends SimpleFormController {

	private DocumentApprovalWorkflow workflow;

	public PassBackForm() {
		setFormView("passBack");
		setCommandClass(Comment.class);
	}

	public void setWorkflow(DocumentApprovalWorkflow workflow) {
		this.workflow = workflow;
	}

	protected ModelAndView onSubmit(Object command) throws Exception {
		this.workflow.passBack((Comment) command);
		return new ModelAndView(new RedirectView("../status"));
	}
}
