package org.springmodules.feedxt.web.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springmodules.feedxt.web.controller.LogInController.LoginForm;

/**
 * @author Sergio Bossa
 */
public class LoginFormValidator implements Validator {
    
    public boolean supports(Class aClass) {
        return aClass.equals(LoginForm.class);
    }
    
    public void validate(Object object, Errors errors) {
        LoginForm form = (LoginForm) object;
        String username = form.getUsername();
        String password = form.getPassword();
        if (username == null || password == null) {
            errors.reject("wrong.login", "Wrong username or password.");
        }
    }
}
