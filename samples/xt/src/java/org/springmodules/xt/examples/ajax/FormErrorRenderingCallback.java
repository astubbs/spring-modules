package org.springmodules.xt.examples.ajax;

import java.util.Locale;
import org.springmodules.xt.ajax.component.Component;
import org.springmodules.xt.ajax.component.TaggedText;
import org.springmodules.xt.ajax.taconite.support.ErrorRenderingCallback;
import org.springframework.context.MessageSource;
import org.springframework.validation.ObjectError;

/**
 * Callback for rendering errors on forms.
 *
 * @author Sergio Bossa
 */
public class FormErrorRenderingCallback implements ErrorRenderingCallback { 
    
    public Component getRenderingComponent(ObjectError error, MessageSource messageSource, Locale locale) {
        TaggedText text = new TaggedText(messageSource.getMessage(error.getCode(), null, error.getDefaultMessage(), locale), TaggedText.Tag.SPAN);
        text.addAttribute("style","color : red;");
        return text;
    }
}
