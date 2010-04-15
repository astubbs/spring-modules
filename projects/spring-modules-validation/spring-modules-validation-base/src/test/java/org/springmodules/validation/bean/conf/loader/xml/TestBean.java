package org.springmodules.validation.bean.conf.loader.xml;

/**
 * @author Uri Boness
 */
public class TestBean {

    private String name;

    public TestBean() {
        this(null);
    }

    public TestBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
