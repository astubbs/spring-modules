package org.springmodules.validation.bean.conf.loader.annotation;

import org.springmodules.validation.bean.conf.loader.annotation.handler.ValidatorRef;
import org.springmodules.validation.bean.conf.loader.annotation.handler.ConditionRef;

/**
 * @author Uri Boness
 */
public class TestBean2 {

    @ConditionRef("testBeanCondition")
    private String name;

    public TestBean2() {
        this(null);
    }

    public TestBean2(String name) {
        this.name =name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
