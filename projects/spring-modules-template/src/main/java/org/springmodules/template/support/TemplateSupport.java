package org.springmodules.template.support;

import org.springmodules.template.TemplateResolver;
import org.springmodules.template.Template;
import org.springframework.util.Assert;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Uri Boness
 */
public class TemplateSupport implements InitializingBean {

    private String templateName;

    private TemplateResolver templateResolver;

    private Template template;

    public TemplateSupport() {
        this(null);
    }

    public TemplateSupport(Template template) {
        this.template = template;
    }

    public TemplateSupport(TemplateResolver templateResolver, String templateName) {
        this(templateResolver.resolve(templateName));
    }

    public final void afterPropertiesSet() throws Exception {
        if (template == null) {
            Assert.notNull(templateResolver, "Property 'templateResolver' is required");
            Assert.notNull(templateName, "Property 'templateName' is required");
            template = templateResolver.resolve(templateName);
        }
    }

    /**
     * Override this method to perform special initialization tasks. Does nothing by default.
     *
     * @throws Exception when initialization fails.
     */
    protected void init() throws Exception {
    }

    //============================================== Setter/Getter =====================================================

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setTemplateResolver(TemplateResolver templateResolver) {
        this.templateResolver = templateResolver;
    }
}
