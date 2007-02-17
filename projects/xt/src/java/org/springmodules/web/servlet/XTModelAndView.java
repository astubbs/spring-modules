package org.springmodules.web.servlet;

import java.util.HashMap;
import java.util.Map;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * {@link org.springframework.web.servlet.ModelAndView} implementation to use
 * in conjunction with Spring MVC and the XT Ajax Framework when returning  
 * after form submission processing in Spring Controllers.
 * <br><br>
 * Users are not forced to return an XTModelAndView from their controllers, but 
 * it is strongly recommended, in order to be able to properly access the command object and the model map
 * from {@link org.springmodules.xt.ajax.AjaxSubmitEvent}s.
 * <br><br>
 * It behaves exactly the same as a standard {@link org.springframework.web.servlet.ModelAndView}, so it is
 * absolutely safe to return an XTModelAndView in place of a standard ModelAndView, even if
 * no Ajax behaviour is actually used.
 * 
 * @see http://static.springframework.org/spring/docs/2.0.x/api/org/springframework/web/servlet/mvc/AbstractFormController.html#processFormSubmission(javax.servlet.http.HttpServletRequest,%20javax.servlet.http.HttpServletResponse,%20java.lang.Object,%20org.springframework.validation.BindException)
 *
 * @author Sergio Bossa
 */
public class XTModelAndView extends ModelAndView {
    
    /**
     * Ajax ModelAndView constructor: it creates a ModelAndView with a view name, a model map merging the model of the 
     * {@link org.springframework.validation.BindException} object, and the model of the given
     * additional map.
     * 
     * @param viewName The view to which redirect to.
     * @param errors The {@link org.springframework.validation.BindException}
     * passed to the Spring Controller processing method.
     * @param model An additional model map to merge with the 
     * {@link org.springframework.validation.BindException} model map.
     */
    public XTModelAndView(String viewName, BindException errors, Map model) {
        super(viewName, new HashMap(errors.getModel()));
        this.getModelInternal().putAll(model);
    }
    
    /**
     * Ajax ModelAndView constructor: it creates a ModelAndView with a view name and the model of the 
     * {@link org.springframework.validation.BindException} object.
     *
     * @param viewName The view to which redirect to.
     * @param errors The {@link org.springframework.validation.BindException}
     * passed to the Spring Controller processing method.
     */
    public XTModelAndView(String viewName, BindException errors) {
        super(viewName, new HashMap(errors.getModel()));
    }
}
