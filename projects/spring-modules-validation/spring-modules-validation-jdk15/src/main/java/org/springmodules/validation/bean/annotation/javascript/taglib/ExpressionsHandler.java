package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Expression;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Expressions;

public class ExpressionsHandler extends Handler {

    public ExpressionsHandler() {
        super(Expressions.class);
    }

    @Override
    public String convertToValang(Field field, Annotation a, MessageSourceAccessor messages) {
        Expressions annotation = (Expressions) a;
        Expression[] expressionSet = annotation.value();
        ExpressionHandler eh = new ExpressionHandler();
        StringBuilder sb = new StringBuilder();
        String prefix = ""; // first iteration

        for (Expression expression : expressionSet) {
            sb.append(prefix);
            sb.append(eh.convertToValang(field, expression, messages));
            prefix = ",\n"; // 2+ iterations

        }
        return sb.toString();
    }

}