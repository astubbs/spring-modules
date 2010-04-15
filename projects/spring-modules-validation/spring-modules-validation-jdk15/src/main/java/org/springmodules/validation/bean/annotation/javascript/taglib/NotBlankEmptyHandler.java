package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotEmpty;

public class NotBlankEmptyHandler extends Handler {

    private final static Log logger = LogFactory.getLog(NotBlankEmptyHandler.class);

    public NotBlankEmptyHandler() {
        super(NotBlank.class, NotEmpty.class);
    }

    @Override
    public String convertToValang(Field field, Annotation a, MessageSourceAccessor messages) {
        String name = field.getName();
        String errMsg = "";
        String applyIfValang = null;

        if (a instanceof NotBlank) {
            NotBlank annotation = (NotBlank) a;
            errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
            applyIfValang = valangToJS(annotation.applyIf());
        } else if (a instanceof NotEmpty) {
            NotEmpty annotation = (NotEmpty) a;
            errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
            applyIfValang = valangToJS(annotation.applyIf());
        }

        StringBuffer sb = new StringBuffer();
        sb.append(" function() {return this.moreThan((this.lengthOf(this.getPropertyValue(");
        sb.append(wrapAndEscapeJsString(name));// field
        sb.append("))), (");
        sb.append('0');
        sb.append("))}");

        return buildBasicRule(name, errMsg, sb.toString(), applyIfValang, a);
    }

}