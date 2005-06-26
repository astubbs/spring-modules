package org.springframework.samples.petclinic.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.samples.petclinic.Owner;
import org.springframework.web.servlet.ModelAndView;

/**
 * JavaBean form controller that is used to add a new <code>Owner</code> to the system.
 *
 * @author Ken Krebs
 */
public class AddOwnerForm extends AbstractClinicForm {

	public AddOwnerForm() {
		// OK to start with a blank command object
		setCommandClass(Owner.class);
		// activate session form mode to allow for detection of duplicate submissions
		setSessionForm(true);
	}

	/** Method inserts a new <code>Owner</code>. */
	protected ModelAndView onSubmit(Object command) throws ServletException {
		Owner owner = (Owner) command;
		// delegate the insert to the Business layer
		getClinic().storeOwner(owner);
		return new ModelAndView(getSuccessView(), "ownerId", owner.getId());
	}

	protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return disallowDuplicateFormSubmission(request, response);
	}

}
