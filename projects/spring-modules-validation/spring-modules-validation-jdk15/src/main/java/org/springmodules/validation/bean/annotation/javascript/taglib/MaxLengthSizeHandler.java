package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.MaxLength;
import org.springmodules.validation.bean.conf.loader.annotation.handler.MaxSize;

public class MaxLengthSizeHandler extends Handler {

    public MaxLengthSizeHandler() {
        super(MaxLength.class, MaxSize.class);
    }

    @Override
    public String convertToValang(Field field, Annotation a, MessageSourceAccessor messages) {

        String name = field.getName();
        String errMsg = "";
        int value = 0;
        String applyIfValang = null;

        if (a instanceof MaxLength) {
            MaxLength annotation = (MaxLength) a;
            errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
            value = annotation.value();
            applyIfValang = valangToJS(annotation.applyIf());
        } else if (a instanceof MaxSize) {
            MaxSize annotation = (MaxSize) a;
            errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
            value = annotation.value();
            applyIfValang = valangToJS(annotation.applyIf());
        }

        StringBuffer sb = new StringBuffer();
        sb.append(" function() {return this.lessThanOrEquals((this.lengthOf(this.getPropertyValue(");
        sb.append(wrapAndEscapeJsString(name));// field
        sb.append("))), (");
        sb.append(value);
        sb.append("))}");

        return buildBasicRule(name, errMsg, sb.toString(), applyIfValang, a);
    }

}