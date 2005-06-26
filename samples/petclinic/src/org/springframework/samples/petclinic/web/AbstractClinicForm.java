package org.springframework.samples.petclinic.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * JavaBean abstract base class for petclinic-aware form controllers.
 * Provides convenience methods for subclasses.
 *
 * @author Ken Krebs
 */
public abstract class AbstractClinicForm extends SimpleFormController {

	private Clinic clinic;

	public void setClinic(Clinic clinic) {
		this.clinic = clinic;
	}

	protected Clinic getClinic() {
		return clinic;
	}

	public void afterPropertiesSet() {
		if (this.clinic == null) {
			throw new IllegalArgumentException("'clinic' is required");
		}
	}

	/**
	 * Set up a custom property editor for the application's date format.
	 */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	/**
	 * Method disallows duplicate form submission.
	 * Typically used to prevent duplicate insertion of entities
	 * into the datastore. Shows a new form with an error message.
	 */
	protected ModelAndView disallowDuplicateFormSubmission(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		BindException errors = getErrorsForNewForm(request);
		errors.reject("duplicateFormSubmission", "Duplicate form submission");
		return showForm(request, response, errors);
	}

}
