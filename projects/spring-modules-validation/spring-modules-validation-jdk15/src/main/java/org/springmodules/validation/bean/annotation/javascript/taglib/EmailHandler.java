package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;

import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Email;

public class EmailHandler extends Handler {

    public EmailHandler() {
        super(Email.class);
    }

    @Override
    public String convertToValang(String fieldName, Annotation a, MessageSourceAccessor messages) {
        Email annotation = (Email) a;

        String errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
        String applyIfValang = valangToJS(annotation.applyIf());

        StringBuffer sb = new StringBuffer();
        sb.append(" function() {return this.equals((this.EmailFunction(this.getPropertyValue(");
        sb.append(wrapAndEscapeJsString(fieldName)); // name
        sb.append("))), (true))}");

        return buildBasicRule(fieldName, errMsg, sb.toString(), applyIfValang, annotation);
    }

}