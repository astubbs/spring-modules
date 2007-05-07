package org.springmodules.validation.bean.conf.loader.annotation;

import org.springframework.validation.Errors;
import org.springmodules.validation.bean.conf.loader.annotation.handler.SpringValidator;
import org.springmodules.validation.validator.AbstractTypeSpecificValidator;

/**
 * @author Uri Boness
 */
@SpringValidator(beanName = "testBeanValidator")
public class TestBean {

    private String name;

    public TestBean() {
        this(null);
    }

    public TestBean(String name) {
        this.name =name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
