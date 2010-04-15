package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Min;

public class MinHandler extends Handler {

    public MinHandler() {
        super(Min.class);
    }

    @Override
    public String convertToValang(Field field, Annotation a, MessageSourceAccessor messages) {
        Min annotation = (Min) a;

        String name = field.getName();
        String errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
        String applyIfValang = valangToJS(annotation.applyIf());

        StringBuffer sb = new StringBuffer();
        sb.append(" function() {return this.moreThanOrEquals((this.getPropertyValue(");
        sb.append(wrapAndEscapeJsString(name));// field
        sb.append(")), (");
        sb.append(annotation.value());
        sb.append("))}");

        return buildBasicRule(name, errMsg, sb.toString(), applyIfValang, annotation);
    }

}