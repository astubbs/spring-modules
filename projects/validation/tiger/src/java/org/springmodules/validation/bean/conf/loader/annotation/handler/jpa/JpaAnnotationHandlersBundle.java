package org.springmodules.validation.bean.conf.loader.annotation.handler.jpa;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import org.springmodules.validation.bean.conf.loader.annotation.handler.ValidationAnnotationHandlersBundle;
import org.springmodules.validation.bean.conf.loader.annotation.handler.PropertyValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.loader.annotation.handler.ClassValidationAnnotationHandler;

/**
 * @author Uri Boness
 */
public class JpaAnnotationHandlersBundle implements ValidationAnnotationHandlersBundle {

    public Collection<PropertyValidationAnnotationHandler> getPropertyHandlers() {
        List<PropertyValidationAnnotationHandler> handlers = new ArrayList<PropertyValidationAnnotationHandler>(4);
        handlers.add(new JpaBasicAnnotationHandler());
        handlers.add(new JpaColumnAnnotationHandler());
        handlers.add(new JpaManyToOneAnnotationHandler());
        handlers.add(new JpaOneToOneAnnotationHandler());
        return handlers;
    }

    public Collection<ClassValidationAnnotationHandler> getClassHandlers() {
        return Collections.EMPTY_LIST;
    }
}
