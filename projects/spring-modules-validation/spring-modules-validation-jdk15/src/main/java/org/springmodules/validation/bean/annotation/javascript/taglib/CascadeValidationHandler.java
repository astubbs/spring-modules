package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.CascadeValidation;

public class CascadeValidationHandler extends Handler {

    public CascadeValidationHandler() {
        //super(CascadeValidation.class);
    }

    @Override
    protected boolean isDelegateAnnotations() {
        return true;
    }

    @Override
    public String convertToValang(String fieldName, Annotation a, MessageSourceAccessor messages) {
        throw new UnsupportedOperationException("this class only does delegate annotation work");
    }

    public Annotation[] getDelegateAnnotations(Object commandObj, String fieldName, Annotation a) {
        CascadeValidation annotation = (CascadeValidation) a;

        Class clazz = commandObj.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        //TODO ?
        return null;// annotation.value(); // all sub-annotations

    }

}