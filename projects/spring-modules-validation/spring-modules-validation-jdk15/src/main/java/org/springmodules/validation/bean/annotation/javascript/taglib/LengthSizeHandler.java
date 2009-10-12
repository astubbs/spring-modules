package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;

import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Length;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Size;

public class LengthSizeHandler extends Handler {

    public LengthSizeHandler() {
        super(Length.class, Size.class);
    }

    @Override
    public String convertToValang(String field, Annotation a, MessageSourceAccessor messages) {
        String errMsg = "";
        int min = 0;
        int max = 0;
        String applyIfValang = null;

        if (a instanceof Length) {
            Length annotation = (Length) a;
            errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
            min = annotation.min();
            max = annotation.max();
            applyIfValang = valangToJS(annotation.applyIf());
        } else if (a instanceof Size) {
            Size annotation = (Size) a;
            errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
            min = annotation.min();
            max = annotation.max();
            applyIfValang = valangToJS(annotation.applyIf());
        }

        StringBuffer sb = new StringBuffer();
        sb.append(" function() {return this.between(this.lengthOf(this.getPropertyValue(");
        sb.append(wrapAndEscapeJsString(field));// field
        sb.append(")), (new Array(");
        sb.append(min);
        sb.append(',');
        sb.append(max);
        sb.append(") ))}");

        return buildBasicRule(field, errMsg, sb.toString(), applyIfValang, a);
    }
}