package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Range;

public class RangeHandler extends Handler {

    public RangeHandler() {
        super(Range.class);
    }

    @Override
    public String convertToValang(Field field, Annotation a, MessageSourceAccessor messages) {
        Range annotation = (Range) a;

        String name = field.getName();
        String errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
        String applyIfValang = valangToJS(annotation.applyIf());

        StringBuffer sb = new StringBuffer();
        sb.append(" function() {return this.between(this.getPropertyValue(");
        sb.append(wrapAndEscapeJsString(name));// field
        sb.append("), (new Array(");
        sb.append(annotation.min());
        sb.append(',');
        sb.append(annotation.max());
        sb.append(") ))}");

        return buildBasicRule(name, errMsg, sb.toString(), applyIfValang, a);
    }
}