package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;

import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.InTheFuture;
import org.springmodules.validation.bean.conf.loader.annotation.handler.InThePast;

public class InThePastFutureHandler extends Handler {

    public InThePastFutureHandler() {
        super(InThePast.class, InTheFuture.class);
    }

    @Override
    public String convertToValang(String fieldName, Annotation a, MessageSourceAccessor messages) {
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
        sb.append("this.getPropertyValue(");
        sb.append(wrapAndEscapeJsString(fieldName)); // value of name field
        sb.append(") ), new Date() )}");

        return buildBasicRule(fieldName, errMsg, sb.toString(), applyIfValang, a);
    }
}