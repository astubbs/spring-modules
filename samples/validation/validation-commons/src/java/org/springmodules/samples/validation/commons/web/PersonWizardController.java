package org.springmodules.samples.validation.commons.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;
import org.springmodules.validation.commons.PageAware;

/**
 * @author Uri Boness
 */
public class PersonWizardController extends AbstractWizardFormController {

    public PersonWizardController() {
        setCommandClass(PersonData.class);
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return showForm(request, response, errors);
    }

    protected void validatePage(Object command, Errors errors, int page) {
        Validator[] validators = getValidators();
        for (int i=0; i<validators.length; i++) {
            Validator validator = validators[i];
            if (validator instanceof PageAware) {
                if (((PageAware)validator).getPage() == page) {
                    validator.validate(command, errors);
                }
            }
        }
    }
}
