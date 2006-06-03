package org.springmodules.validation.commons;

/**
 * A bean validator that is aware of the page attribute in the form configuration.
 * This may be useful for partial bean validation (that is, selected properties of the bean) needed
 * for example in web wizard controllers.
 *
 * @author Uri Boness
 */
public class ConfigurablePageBeanValidator extends AbstractPageBeanValidator {

    private String formName;

    /**
     * Default constructor (javabean support)
     */
    public ConfigurablePageBeanValidator() {
    }

    /**
     * Constructs a new DefaultPageBeanValidator with a given page to validate.
     *
     * @param page The page that should be validated by this validator.
     */
    public ConfigurablePageBeanValidator(int page) {
        super(page);
    }

    /**
     * If <code>useFullyQualifiedClassName</code> is false (default value), this function returns a
     * string containing the uncapitalized, short name for the given class
     * (e.g. myBean for the class com.domain.test.MyBean). Otherwise, it  returns the value
     * returned by <code>Class.getName()</code>.
     *
     * @param cls <code>Class</code> of the bean to be validated.
     * @return the bean name.
     */
    protected String getFormName(Class cls) {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

}
