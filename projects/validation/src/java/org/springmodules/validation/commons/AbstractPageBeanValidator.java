package org.springmodules.validation.commons;

import org.apache.commons.validator.Validator;

/**
 * @author Uri Boness
 */
public abstract class AbstractPageBeanValidator extends AbstractBeanValidator implements PageAware {

    private final static int DEFAULT_PAGE = -1;

    private int page = DEFAULT_PAGE;

    protected AbstractPageBeanValidator() {
    }

    protected AbstractPageBeanValidator(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    protected void initValidator(Validator validator) {
        validator.setPage(page);
    }

    protected void cleanupValidator(Validator validator) {
        validator.setPage(DEFAULT_PAGE);
    }
}
