package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;

import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

public class RegExpHandler extends Handler {

    public RegExpHandler() {
        super(RegExp.class);
    }

    @Override
    public String convertToValang(String fieldName, Annotation a, MessageSourceAccessor messages) {
        RegExp annotation = (RegExp) a;

        String errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
        String applyIfValang = valangToJS(annotation.applyIf());
        StringBuffer sb = new StringBuffer();

        sb.append(" function() {return this.equals((this.RegExFunction(");
        sb.append(wrapAndEscapeJsString(annotation.value())); // regex
        sb.append(",this.getPropertyValue(");
        sb.append(wrapAndEscapeJsString(fieldName)); // value name
        sb.append("))), (true))}");

        return buildBasicRule(fieldName, errMsg, sb.toString(), applyIfValang, a);
    }
}