package org.springmodules.feedxt.web.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springmodules.feedxt.web.view.SignUpUserView;

/**
 * @author Sergio Bossa
 */
public class UserValidator implements Validator { 
    
    public boolean supports(Class aClass) {
        return SignUpUserView.class.isAssignableFrom(aClass);
    }

    public void validate(Object object, Errors errors) {
        SignUpUserView user = (SignUpUserView) object;
        if (user.getFirstname() == null || user.getFirstname().equals("")) {
            errors.rejectValue("firstname", "user.empty.firstname", "Empty firstname.");
        }
        if (user.getSurname() == null || user.getSurname().equals("")) {
            errors.rejectValue("surname", "user.empty.surname", "Empty surname.");
        }
        if (user.getBirthdate() == null) {
            errors.rejectValue("birthdate", "user.empty.birthdate", "Empty birthdate.");
        }
        if (user.getUsername() == null || user.getUsername().equals("")) {
            errors.rejectValue("username", "user.empty.username", "Empty username.");
        }
        if (user.getPassword() == null || user.getPassword().equals("")) {
            errors.rejectValue("password", "user.empty.password", "Empty password.");
        }
        if (user.getPassword() != null && !user.getPassword().equals("") && user.getPassword().length() < 5) {
            errors.rejectValue("password", "user.short.password", "Short password : it must have at least 5 characters.");
        }
        if (user.getConfirmedPassword() == null || !user.getConfirmedPassword().equals(user.getPassword())) {
            errors.rejectValue("password", "user.wrong.password", "Password doesn't match.");
        }
    }
}
