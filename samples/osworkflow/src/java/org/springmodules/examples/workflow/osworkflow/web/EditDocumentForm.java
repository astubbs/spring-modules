
package org.springmodules.examples.workflow.osworkflow.web;

import javax.servlet.http.HttpServletRequest;

import org.springmodules.examples.workflow.osworkflow.model.Document;
import org.springmodules.examples.workflow.osworkflow.service.DocumentApprovalWorkflow;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author robh
 */
public class EditDocumentForm extends SimpleFormController {

	private DocumentApprovalWorkflow workflow;

	public EditDocumentForm() {
		setFormView("editDocument");
	}

	public void setWorkflow(DocumentApprovalWorkflow workflow) {
		this.workflow = workflow;
	}

	protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {
		Document doc = workflow.getCurrentDocument();

		if (doc == null) {
			return new Document();
		}
		else {
			return doc;
		}
	}

	protected ModelAndView onSubmit(Object command) throws Exception {
		Document document = (Document) command;
		this.workflow.uploadDocument(document);
		return new ModelAndView(new RedirectView("/approval/status"));
	}


}
