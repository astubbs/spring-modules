package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.MinLength;
import org.springmodules.validation.bean.conf.loader.annotation.handler.MinSize;

public class MinLengthSizeHandler extends Handler {

    public MinLengthSizeHandler() {
        super(MinLength.class, MinSize.class);
    }

    @Override
    public String convertToValang(Field field, Annotation a, MessageSourceAccessor messages) {

        String name = field.getName();
        String errMsg = "";
        int value = 0;
        String applyIfValang = null;

        if (a instanceof MinLength) {
            MinLength annotation = (MinLength) a;
            errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
            value = annotation.value();
            applyIfValang = valangToJS(annotation.applyIf());
        } else if (a instanceof MinSize) {
            MinSize annotation = (MinSize) a;
            errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
            value = annotation.value();
            applyIfValang = valangToJS(annotation.applyIf());
        }

        StringBuffer sb = new StringBuffer();
        sb.append(" function() {return this.moreThanOrEquals((this.lengthOf(this.getPropertyValue(");
        sb.append(wrapAndEscapeJsString(name));// field
        sb.append("))), (");
        sb.append(value);
        sb.append("))}");

        return buildBasicRule(name, errMsg, sb.toString(), applyIfValang, a);
    }

}