package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;

import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

public class NotNullHandler extends Handler {

    public NotNullHandler() {
        super(NotNull.class);
    }

    @Override
    public String convertToValang(String fieldName, Annotation a, MessageSourceAccessor messages) {
        NotNull annotation = (NotNull) a;

        String errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
        String applyIfValang = valangToJS(annotation.applyIf());

        StringBuffer sb = new StringBuffer();
        sb.append(" function() {return ! this.nullFunc((this.getPropertyValue(");
        sb.append(wrapAndEscapeJsString(fieldName));// field
        sb.append(")), (null))}");

        return buildBasicRule(fieldName, errMsg, sb.toString(), applyIfValang, annotation);
    }

}