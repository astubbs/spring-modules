package org.springmodules.template.resolver;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.LocalizedResourceHelper;

/**
 * @author Uri Boness
 */
public class LocalizedTemplateResolver extends BasicTemplateResolver {

    private LocalizedResourceHelper helper;

    private String prefix;
    private String extension;

    public LocalizedTemplateResolver() {
        this.helper = new LocalizedResourceHelper(getResourceLoader());
    }

    protected Resource loadTemplateResource(String name, String encoding) {
        Locale locale = LocaleContextHolder.getLocale();
        return helper.findLocalizedResource(prefix + name, extension, locale);
    }

    //============================================== Setter/Getter =====================================================

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
