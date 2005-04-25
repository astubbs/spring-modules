
package org.springmodules.examples.workflow.osworkflow.web;

import org.springmodules.workflow.osworkflow.OsWorkflowTemplate;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author robh
 */
public class PassBackForm extends SimpleFormController {

	private OsWorkflowTemplate template;

	public PassBackForm() {
		setFormView("passBack");
		setCommandClass(Object.class);
	}

	public void setTemplate(OsWorkflowTemplate template) {
		this.template = template;
	}

	protected ModelAndView onSubmit(Object command) throws Exception {
		template.doAction(3);
		return new ModelAndView(new RedirectView("/approval/status"));
	}
}
