package org.springframework.samples.petclinic.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.samples.petclinic.Owner;
import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * JavaBean Form controller that is used to edit an existing <code>Owner</code>.
 *
 * @author Ken Krebs
 */
public class EditOwnerForm extends AbstractClinicForm {

	public EditOwnerForm() {
		// need a session to hold the formBackingObject
		setSessionForm(true);
		// initialize the form from the formBackingObject
		setBindOnNewForm(true);
	}

	/** Method forms a copy of an existing Owner for editing */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		// get the Owner referred to by id in the request
		return getClinic().loadOwner(RequestUtils.getRequiredIntParameter(request, "ownerId"));
	}

	/** Method updates an existing Owner. */
	protected ModelAndView onSubmit(Object command) throws ServletException {
		Owner owner = (Owner) command;
		// delegate the update to the Business layer
		getClinic().storeOwner(owner);

		return new ModelAndView(getSuccessView(), "ownerId", owner.getId());
	}

}
