package org.springframework.samples.petclinic.web;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.samples.petclinic.Owner;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * JavaBean Form controller that is used to search for <code>Owner</code>s by last name.
 *
 * @author Ken Krebs
 */
public class FindOwnersForm	extends AbstractClinicForm {

	private String selectView;

	/** Creates a new instance of FindOwnersForm */
	public FindOwnersForm() {
		// OK to start with a blank command object
		setCommandClass(Owner.class);
	}

	/**
	 * Set the name of the view that should be used for selection display.
	 */
	public void setSelectView(String selectView) {
		this.selectView = selectView;
	}

	protected void initApplicationContext() {
		super.initApplicationContext();
		if (this.selectView == null) {
			throw new IllegalArgumentException("selectView isn't set");
		}
	}

	/**
	 * Method used to search for owners renders View depending on how many are found.
	 */
	protected ModelAndView onSubmit(
			HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		Owner owner = (Owner) command;

		// find owners by last name
		Collection results = getClinic().findOwners(owner.getLastName());

		if (results.size() < 1) {
			// no owners found
			errors.rejectValue("lastName", "notFound", null, "not found");
			return showForm(request, response, errors);
		}

		if (results.size() > 1) {
			// multiple owners found
			return new ModelAndView(this.selectView, "selections", results);
		}

		// 1 owner found
		owner = (Owner) results.iterator().next();
		return new ModelAndView(getSuccessView(), "ownerId", owner.getId());
	}

}
