package org.springmodules.xt.examples.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springmodules.xt.ajax.web.servlet.AjaxModelAndView;
import org.springmodules.xt.examples.mvc.form.NoOpForm;

/**
 * No-Op controller.
 *
 * @author Sergio Bossa
 */
public class ExceptionController extends SimpleFormController {
    
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return new NoOpForm();
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        if (request.getParameter("source-element").equals("b1")) {
            throw new Exception("Exception occurred in controller!");
        }
        return new AjaxModelAndView("/start.page", errors);
    }
}
