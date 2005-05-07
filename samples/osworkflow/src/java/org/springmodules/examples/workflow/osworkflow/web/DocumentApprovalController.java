
package org.springmodules.examples.workflow.osworkflow.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.opensymphony.workflow.query.Expression;
import com.opensymphony.workflow.query.FieldExpression;
import com.opensymphony.workflow.query.NestedExpression;
import com.opensymphony.workflow.query.WorkflowExpressionQuery;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.module.propertyset.PropertySet;
import org.springmodules.workflow.osworkflow.OsWorkflowTemplate;

import org.springmodules.workflow.osworkflow.OsWorkflowContextHolder;
import org.springmodules.examples.workflow.osworkflow.service.DocumentApprovalWorkflow;

import org.springmodules.examples.workflow.osworkflow.model.Document;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;

import net.sf.acegisecurity.context.ContextHolder;

/**
 * @author robh
 */
public class DocumentApprovalController extends MultiActionController {

	private DocumentApprovalWorkflow workflow;

	public void setWorkflow(DocumentApprovalWorkflow workflow) {
		this.workflow = workflow;
	}

	public ModelAndView start(HttpServletRequest request, HttpServletResponse response) {
		workflow.startNewWorkflow();
		return status(request, response);
	}

	public ModelAndView status(HttpServletRequest request, HttpServletResponse response) {

		Map model = new HashMap();

		model.put("document", workflow.getCurrentDocument());
		model.put("comments", workflow.getCurrentComments());
		model.put("currentSteps", workflow.getCurrentStepDescriptors());
		model.put("historySteps", workflow.getHistoryStepDescriptors());
		model.put("availableActions", workflow.getAvailableActionDescriptors());
		model.put("state", workflow.getCurrentState());

		return new ModelAndView("status", model);
	}

	public ModelAndView underway(HttpServletRequest request, HttpServletResponse response) {

		List queryResults = this.workflow.getUnderwayWorkflows();

		return new ModelAndView("underway", "ids", queryResults);
	}

	public ModelAndView approve(HttpServletRequest request, HttpServletResponse response) {
		Document document = this.workflow.getCurrentDocument();
		this.workflow.approveCurrentDocument();

		return new ModelAndView("approved", "document", document);
	}

	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.invalidate();
		ContextHolder.setContext(null);
		return new ModelAndView(new RedirectView("/index.jsp", true));
	}
}
