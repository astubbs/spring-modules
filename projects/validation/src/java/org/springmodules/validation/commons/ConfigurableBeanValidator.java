package org.springmodules.validation.commons;

/**
 *
 * @author Rob Harrop
 */
public class ConfigurableBeanValidator extends AbstractBeanValidator {

    private String formName = null;

    protected String getFormName(Class aClass) {
        return this.formName;
    }

    /**
     * @param formName The formName to set.
     */
    public void setFormName(String formName) {
        this.formName = formName;
    }


}
