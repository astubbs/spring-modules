package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.InTheFuture;
import org.springmodules.validation.bean.conf.loader.annotation.handler.InThePast;

public class InThePastFutureHandler extends Handler {

    public InThePastFutureHandler() {
        super(InThePast.class, InTheFuture.class);
    }

    @Override
    public String convertToValang(Field field, Annotation a, MessageSourceAccessor messages) {
        String name = field.getName();
        String errMsg = "";
        String pastFutureMethod = "";
        String applyIfValang = null;

        if (a instanceof InThePast) {
            InThePast annotation = (InThePast) a;
            errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
            pastFutureMethod = "lessThan";
            applyIfValang = valangToJS(annotation.applyIf());
        } else if (a instanceof InTheFuture) {
            InTheFuture annotation = (InTheFuture) a;
            errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
            pastFutureMethod = "moreThan";
            applyIfValang = valangToJS(annotation.applyIf());
        }

        StringBuffer sb = new StringBuffer();
        sb.append(" function() {return this.");
        sb.append(pastFutureMethod);
        sb.append("( (");
        sb.append("this.parseDate(this.getPropertyValue(");
        sb.append(wrapAndEscapeJsString(name)); // value of name field
        sb.append("), ");
        sb.append(wrapAndEscapeJsString(name)); // name
        sb.append(") ), ( (new Date()).getTime() ) )}");

        return buildBasicRule(name, errMsg, sb.toString(), applyIfValang, a);
    }
}