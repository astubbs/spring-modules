package org.springframework.samples.petclinic.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextException;
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

	public void afterPropertiesSet() throws Exception {
		if (clinic == null)
			throw new ApplicationContextException("Must set clinic bean property on " + getClass());
	}

	// handlers

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
		return new ModelAndView("vetsView", "vets", clinic.getVets());
	}

	/**
	 * Custom handler for owner display
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView ownerHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		Owner owner = clinic.loadOwner(RequestUtils.getIntParameter(request, "ownerId", 0));
		if (owner == null) {
			return new ModelAndView("findOwnersRedirect");
		}
		Map model = new HashMap();
		model.put("owner", owner);
		return new ModelAndView("ownerView", "model", model);
	}

}