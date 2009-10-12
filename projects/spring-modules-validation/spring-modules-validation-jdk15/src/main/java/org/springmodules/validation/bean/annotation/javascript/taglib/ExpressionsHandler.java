package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;

import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Expressions;

public class ExpressionsHandler extends Handler {

    public ExpressionsHandler() {
        super(Expressions.class);
    }

    @Override
    protected boolean isDelegateAnnotations() {
        return true;
    }

    @Override
    public String convertToValang(String fieldName, Annotation a, MessageSourceAccessor messages) {
        throw new UnsupportedOperationException("this class only does delegate annotation work");
    }

    public Annotation[] getDelegateAnnotations(Annotation a, String fieldName) {
        Expressions annotation = (Expressions) a;
        return annotation.value(); // all sub-annotations

    }

}