package org.springframework.samples.petclinic.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Owner;
import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * <code>MultiActionController</code> that handles all non-form URL's.
 *
 * @author Ken Krebs
 */
public class ClinicController extends MultiActionController implements InitializingBean {

	private Clinic clinic;


	public void setClinic(Clinic clinic) {
		this.clinic = clinic;
	}

	public void afterPropertiesSet() {
		if (this.clinic == null) {
			throw new IllegalArgumentException("Must set clinic bean property on " + getClass());
		}
	}


	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView welcomeHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		return new ModelAndView("welcomeView");
	}

	/**
	 * Custom handler for vets display
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView vetsHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		return new ModelAndView("vetsView", "vets", this.clinic.getVets());
	}

	/**
	 * Custom handler for owner display
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView ownerHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		Owner owner = this.clinic.loadOwner(RequestUtils.getIntParameter(request, "ownerId", 0));
		if (owner == null) {
			return new ModelAndView("findOwnersRedirect");
		}
		return new ModelAndView("ownerView", "owner", owner);
	}

}
