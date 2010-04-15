package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Expression;

public class ExpressionHandler extends Handler {

    public ExpressionHandler() {
        super(Expression.class);
    }

    @Override
    public String convertToValang(Field field, Annotation a, MessageSourceAccessor messages) {
        Expression annotation = (Expression) a;

        String name = (field == null) ? "" : field.getName();
        String errMsg = messages.getMessage(annotation.errorCode(), annotation.message());
        String applyIfValang = valangToJS(annotation.applyIf());
        String expression = valangToJS(annotation.value());

        return buildBasicRule(name, errMsg, expression, applyIfValang, annotation);
    }

}