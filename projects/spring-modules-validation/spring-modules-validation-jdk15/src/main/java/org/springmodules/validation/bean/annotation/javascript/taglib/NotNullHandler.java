package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

public class NotNullHandler extends Handler {

    public NotNullHandler() {
        super(NotNull.class);
    }

    @Override
    public String convertToValang(Field field, Annotation a, MessageSourceAccessor messages) {
        NotNull annotation = (NotNull) a;

        String name = field.getName();
        String errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
        String applyIfValang = valangToJS(annotation.applyIf());

        StringBuffer sb = new StringBuffer();
        sb.append(" function() {return ! this.nullFunc((this.getPropertyValue(");
        sb.append(wrapAndEscapeJsString(name));// field
        sb.append(")), (null))}");

        return buildBasicRule(name, errMsg, sb.toString(), applyIfValang, annotation);
    }

}