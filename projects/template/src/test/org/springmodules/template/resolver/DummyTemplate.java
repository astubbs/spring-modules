package org.springmodules.template.resolver;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

import org.springmodules.template.Template;
import org.springmodules.template.TemplateGenerationException;

/**
 * @author Uri Boness
 */
public class DummyTemplate implements Template {

    public void generate(OutputStream out, Map model) throws TemplateGenerationException {
    }

    public void generate(Writer writer, Map model) throws TemplateGenerationException {
    }

    public String generate(Map model) throws TemplateGenerationException {
        return null;
    }
}
