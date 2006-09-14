/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Owner;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * <code>MultiActionController</code> that handles all non-form URL's.
 *
 * @author Ken Krebs
 * @author Rob Harrop
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
	 * Custom handler for the welcome view.
	 * <p>Note that this handler returns an empty ModelAndView object.
	 * It relies on the RequestToViewNameTranslator to come up with
	 * a view name based on the request URL: "/welcome.html" -> "welcome",
	 * plus configured "View" suffix -> "welcomeView".
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView welcomeHandler(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView();
	}

	/**
	 * Custom handler for vets display.
	 * <p>Note that this handler returns a plain ModelMap object instead of
	 * a ModelAndView, also leveraging convention-based model attribute names.
	 * It relies on the RequestToViewNameTranslator to come up with
	 * a view name based on the request URL: "/vets.html" -> "vets",
	 * plus configured "View" suffix -> "vetsView".
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public Map vetsHandler(HttpServletRequest request, HttpServletResponse response) {
		return new ModelMap(this.clinic.getVets());
	}

	/**
	 * Custom handler for owner display.
	 * <p>Note that this handler usually returns a ModelAndView object
	 * without view, also leveraging convention-based model attribute names.
	 * It relies on the RequestToViewNameTranslator to come up with
	 * a view name based on the request URL: "/owner.html" -> "owner",
	 * plus configured "View" suffix -> "ownerView".
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView ownerHandler(HttpServletRequest request, HttpServletResponse response) {
		Owner owner = this.clinic.loadOwner(ServletRequestUtils.getIntParameter(request, "ownerId", 0));
		if (owner == null) {
			return new ModelAndView("findOwnersRedirect");
		}
		return new ModelAndView().addObject(owner);
	}

}
