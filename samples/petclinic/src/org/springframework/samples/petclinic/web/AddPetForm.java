package org.springframework.samples.petclinic.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * JavaBean form controller that is used to add a new <code>Pet</code> to the system.
 *
 * @author Ken Krebs
 */
public class AddPetForm extends AbstractClinicForm {

	public AddPetForm() {
		// need a session to hold the formBackingObject
		setSessionForm(true);
	}

	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();
		refData.put("types", getClinic().getPetTypes());
		return refData;
	}

	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		Owner owner = getClinic().loadOwner(RequestUtils.getRequiredIntParameter(request, "ownerId"));
		Pet pet = new Pet();
		owner.addPet(pet);
		return pet;
	}

	protected void onBind(HttpServletRequest request, Object command) {
		Pet pet = (Pet) command;
		int typeId = Integer.parseInt(request.getParameter("typeId"));
		pet.setType((PetType) EntityUtils.getById(getClinic().getPetTypes(), PetType.class, typeId));
	}

	/** Method inserts a new Pet */
	protected ModelAndView onSubmit(Object command) throws ServletException {
		Pet pet = (Pet) command;
		// delegate the insert to the Business layer
		getClinic().storePet(pet);
		return new ModelAndView(getSuccessView(), "ownerId", pet.getOwner().getId());
	}

	protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return disallowDuplicateFormSubmission(request, response);
	}

}
