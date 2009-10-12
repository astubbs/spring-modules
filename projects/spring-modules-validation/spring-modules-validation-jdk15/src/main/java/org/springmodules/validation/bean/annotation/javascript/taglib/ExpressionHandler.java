package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;

import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Expression;

public class ExpressionHandler extends Handler {

    public ExpressionHandler() {
        super(Expression.class);
    }

    @Override
    public String convertToValang(String fieldName, Annotation a, MessageSourceAccessor messages) {
        Expression annotation = (Expression) a;

        String errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
        String applyIfValang = valangToJS(annotation.applyIf());
        String expression = valangToJS(annotation.value());

        return buildBasicRule(fieldName, errMsg, expression, applyIfValang, annotation);
    }

}