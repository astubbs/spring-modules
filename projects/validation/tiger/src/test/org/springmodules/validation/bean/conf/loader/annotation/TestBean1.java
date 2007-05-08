package org.springmodules.validation.bean.conf.loader.annotation;

import org.springmodules.validation.bean.conf.loader.annotation.handler.ValidatorRef;

/**
 * @author Uri Boness
 */
@ValidatorRef("testBeanValidator")
public class TestBean1 {

    private String name;

    public TestBean1() {
        this(null);
    }

    public TestBean1(String name) {
        this.name =name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
