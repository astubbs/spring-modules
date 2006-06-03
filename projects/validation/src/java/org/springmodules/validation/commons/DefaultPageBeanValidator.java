package org.springmodules.validation.commons;

import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * A bean validator that is aware of the page attribute in the form configuration.
 * This may be useful for partial bean validation (that is, selected properties of the bean) needed
 * for example in web wizard controllers.
 *
 * @author Uri Boness
 */
public class DefaultPageBeanValidator extends AbstractPageBeanValidator {

    /**
     * If <code>true</code> the full class name of each bean will be used as the form name when looking for
     * a <code>Validator</code>. If <code>false</code> the uncapitalized, short name of the class will be used.
     */
    private boolean useFullyQualifiedClassName = false;

    /**
     * Default constructor (javabean support)
     */
    public DefaultPageBeanValidator() {
    }

    /**
     * Constructs a new DefaultPageBeanValidator with a given page to validate.
     *
     * @param page The page that should be validated by this validator.
     */
    public DefaultPageBeanValidator(int page) {
        super(page);
    }

    /**
     * Sets the value of the <code>useFullyQualifiedClassName</code>.
     */
    public void setUseFullyQualifiedClassName(boolean useFullyQualifiedClassName) {
        this.useFullyQualifiedClassName = useFullyQualifiedClassName;
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
        return (this.useFullyQualifiedClassName) ? cls.getName() : StringUtils.uncapitalize(ClassUtils.getShortName(cls));
    }
}
