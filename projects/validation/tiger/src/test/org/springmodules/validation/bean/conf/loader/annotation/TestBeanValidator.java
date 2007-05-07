package org.springmodules.validation.bean.conf.loader.annotation;

import org.springmodules.validation.validator.AbstractTypeSpecificValidator;
import org.springframework.validation.Errors;

/**
 * @author Uri Boness
 */
public class TestBeanValidator extends AbstractTypeSpecificValidator {

    public TestBeanValidator() {
            super(TestBean.class);
        }

        public void validate(Object object, Errors errors) {
            if(((TestBean)object).getName() == null) {
                errors.rejectValue("name", "errorCode");
            }
        }

}
