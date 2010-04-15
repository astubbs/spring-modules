package org.springmodules.validation.bean.conf.loader.annotation;

import org.springmodules.validation.bean.conf.loader.annotation.handler.ValidatorRef;
import org.springmodules.validation.bean.conf.loader.annotation.handler.ConditionRef;

/**
 * @author Uri Boness
 */
public class TeztBean2 {

    @ConditionRef("testBeanCondition")
    private String name;

    public TeztBean2() {
        this(null);
    }

    public TeztBean2(String name) {
        this.name =name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
