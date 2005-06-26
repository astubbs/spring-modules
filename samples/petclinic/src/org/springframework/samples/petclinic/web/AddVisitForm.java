package org.springframework.samples.petclinic.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.Visit;
import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * JavaBean form controller that is used to add a new <code>Visit</code> to the system.
 *
 * @author Ken Krebs
 */
public class AddVisitForm extends AbstractClinicForm {

	public AddVisitForm() {
		// need a session to hold the formBackingObject
		setSessionForm(true);
	}

	/** Method creates a new <code>Visit</code> with the correct <code>Pet</code> info */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		Pet pet = getClinic().loadPet(RequestUtils.getRequiredIntParameter(request, "petId"));
		Visit visit = new Visit();
		pet.addVisit(visit);
		return visit;
	}

	/** Method inserts a new <code>Visit</code>. */
	protected ModelAndView onSubmit(Object command) throws ServletException {
		Visit visit = (Visit) command;
		// delegate the insert to the Business layer
		getClinic().storeVisit(visit);
		return new ModelAndView(getSuccessView(), "ownerId", visit.getPet().getOwner().getId());
	}

	protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return disallowDuplicateFormSubmission(request, response);
	}

}
