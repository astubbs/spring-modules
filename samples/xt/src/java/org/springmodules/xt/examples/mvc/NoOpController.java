package org.springmodules.xt.examples.mvc;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springmodules.web.servlet.XTModelAndView;
import org.springmodules.xt.examples.mvc.form.NoOpForm;

/**
 * No-Op controller.
 *
 * @author Sergio Bossa
 */
public class NoOpController extends SimpleFormController {
    
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return new NoOpForm();
    }

    protected ModelAndView onSubmit(Object command, BindException errors) throws Exception {
        return new XTModelAndView("/start.page", errors);
    }
}
