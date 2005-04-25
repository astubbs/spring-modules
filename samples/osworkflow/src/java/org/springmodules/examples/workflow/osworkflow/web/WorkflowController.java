
package org.springmodules.examples.workflow.osworkflow.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springmodules.workflow.osworkflow.OsWorkflowTemplate;

/**
 * @author robh
 */
public class WorkflowController implements Controller {

	private OsWorkflowTemplate template;

	public void setTemplate(OsWorkflowTemplate template) {
		this.template = template;
	}

	public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
		
	}

}
