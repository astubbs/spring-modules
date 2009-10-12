package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;

import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Range;

public class RangeHandler extends Handler {

    public RangeHandler() {
        super(Range.class);
    }

    @Override
    public String convertToValang(String fieldName, Annotation a, MessageSourceAccessor messages) {
        Range annotation = (Range) a;

        String errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
        String applyIfValang = valangToJS(annotation.applyIf());

        StringBuffer sb = new StringBuffer();
        sb.append(" function() {return this.between(this.getPropertyValue(");
        sb.append(wrapAndEscapeJsString(fieldName));// field
        sb.append("), (new Array(");
        sb.append(annotation.min());
        sb.append(',');
        sb.append(annotation.max());
        sb.append(") ))}");

        return buildBasicRule(fieldName, errMsg, sb.toString(), applyIfValang, a);
    }
}