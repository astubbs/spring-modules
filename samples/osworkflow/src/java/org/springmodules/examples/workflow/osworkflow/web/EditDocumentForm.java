
package org.springmodules.examples.workflow.osworkflow.web;

import javax.servlet.http.HttpServletRequest;

import org.springmodules.examples.workflow.osworkflow.model.Document;
import org.springmodules.workflow.osworkflow.OsWorkflowTemplate;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author robh
 */
public class EditDocumentForm extends SimpleFormController {

	private OsWorkflowTemplate template;

	public EditDocumentForm() {
		setFormView("editDocument");
	}

	protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {
		if (template.getPropertySet().exists("document")) {
			return template.getPropertySet().getAsActualType("document");
		}
		else {
			return new Document();
		}
	}

	public void setTemplate(OsWorkflowTemplate template) {
		this.template = template;
	}

	protected ModelAndView onSubmit(Object command) throws Exception {
		Document document = (Document) command;
		template.doAction(1, "document", document);
		return new ModelAndView(new RedirectView("/approval/status"));
	}


}
