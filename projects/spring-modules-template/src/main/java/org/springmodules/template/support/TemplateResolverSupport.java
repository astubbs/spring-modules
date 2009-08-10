package org.springmodules.template.support;

import org.springmodules.template.TemplateResolver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author Uri Boness
 */
public class TemplateResolverSupport implements InitializingBean {

    private TemplateResolver templateResolver;

    public TemplateResolverSupport() {
        this(null);
    }

    public TemplateResolverSupport(TemplateResolver templateResolver) {
        this.templateResolver = templateResolver;
    }

    //============================================== Setter/Getter =====================================================

    public void setTemplateResolver(TemplateResolver templateResolver) {
        this.templateResolver = templateResolver;
    }

    public TemplateResolver getTemplateResolver() {
        return templateResolver;
    }

    public final void afterPropertiesSet() throws Exception {
        Assert.notNull(templateResolver, "Property 'templateResolver' is required");
        init();
    }

    /**
     * Override to perform special initialization tasks. Does nothing by default.
     *
     * @throws Exception when initialization fails.
     */
    protected void init() throws Exception {
    }
}
