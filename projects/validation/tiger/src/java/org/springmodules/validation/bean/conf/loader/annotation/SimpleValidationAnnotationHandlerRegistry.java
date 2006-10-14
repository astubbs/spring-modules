package org.springmodules.validation.bean.conf.loader.annotation;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.validation.bean.conf.loader.annotation.handler.ClassValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.loader.annotation.handler.MethodValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.loader.annotation.handler.PropertyValidationAnnotationHandler;
import org.springmodules.validation.util.cel.ConditionExpressionBased;
import org.springmodules.validation.util.cel.ConditionExpressionParser;
import org.springmodules.validation.util.cel.valang.ValangConditionExpressionParser;
import org.springmodules.validation.util.context.BasicContextAware;
import org.springmodules.validation.util.fel.FunctionExpressionBased;
import org.springmodules.validation.util.fel.FunctionExpressionParser;
import org.springmodules.validation.util.fel.parser.ValangFunctionExpressionParser;

/**
 * A simple implementation of {@link ValidationAnnotationHandlerRegistry} that enableds registration of annotation
 * handlers.
 *
 * @author Uri Boness
 */
public class SimpleValidationAnnotationHandlerRegistry extends BasicContextAware
    implements ValidationAnnotationHandlerRegistry, InitializingBean, ConditionExpressionBased, FunctionExpressionBased {

    private final static Log logger = LogFactory.getLog(SimpleValidationAnnotationHandlerRegistry.class);

    private List<ClassValidationAnnotationHandler> classHandlers;
    private List<PropertyValidationAnnotationHandler> propertyHandlers;
    private List<MethodValidationAnnotationHandler> methodHandlers;

    private boolean conditionExpressionParserSet = false;
    private ConditionExpressionParser conditionExpressionParser;

    private boolean functionExpressionParserSet = false;
    private FunctionExpressionParser functionExpressionParser;

    /**
     * Constructs a new DefaultValidationAnnotationHandlerRegistry.
     */
    public SimpleValidationAnnotationHandlerRegistry() {
        classHandlers = new ArrayList<ClassValidationAnnotationHandler>();
        propertyHandlers = new ArrayList<PropertyValidationAnnotationHandler>();
        methodHandlers = new ArrayList<MethodValidationAnnotationHandler>();
        conditionExpressionParser = new ValangConditionExpressionParser();
        functionExpressionParser = new ValangFunctionExpressionParser();
    }

    /**
     * @see org.springmodules.validation.bean.conf.loader.annotation.ValidationAnnotationHandlerRegistry#findClassHanlder(java.lang.annotation.Annotation, Class)
     */
    public ClassValidationAnnotationHandler findClassHanlder(Annotation annotation, Class clazz) {
        for (ClassValidationAnnotationHandler handler : classHandlers) {
            if (handler.supports(annotation, clazz)) {
                return handler;
            }
        }
        return null;
    }

    /**
     * @see ValidationAnnotationHandlerRegistry#findPropertyHanlder(java.lang.annotation.Annotation, Class, java.beans.PropertyDescriptor)
     */
    public PropertyValidationAnnotationHandler findPropertyHanlder(Annotation annotation, Class clazz, PropertyDescriptor descriptor) {
        for (PropertyValidationAnnotationHandler handler : propertyHandlers) {
            if (handler.supports(annotation, clazz, descriptor)) {
                return handler;
            }
        }
        return null;
    }

    /**
     * @see ValidationAnnotationHandlerRegistry#findMethodHandler(java.lang.annotation.Annotation, Class, java.lang.reflect.Method)
     */
    public MethodValidationAnnotationHandler findMethodHandler(Annotation annotation, Class clazz, Method method) {
        for (MethodValidationAnnotationHandler handler : methodHandlers) {
            if (handler.supports(annotation, clazz, method)) {
                return handler;
            }
        }
        return null;
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Registers the given class validation annotation handler with this registry. The newly added handler will have
     * precedence over the already registered handlers, that is, for every annotation, this handler will be checked
     * for support before the already registered handlers.
     *
     * @param handler The class validation annoation handler to be registered.
     */
    public void registerClassHandler(ClassValidationAnnotationHandler handler) {
        classHandlers.add(0, handler);
    }

    /**
     * Registers the given class validation annoation handlers to the registry. These handlers will have precedence
     * over the already registered handlers, that is, for every annotation, these handlers will be check for suppport
     * before the already registered ones. The order of the given handler list is important for it determines the
     * precedence of the handlers within this list.
     *
     * @param handlers The extra class validation annotation handlers to register with this registry.
     */
    public void setExtraClassHandlers(List<ClassValidationAnnotationHandler> handlers) {
        Collections.reverse(handlers);
        for (ClassValidationAnnotationHandler handler : handlers) {
            registerClassHandler(handler);
        }
    }

    /**
     * Registers the given property validation annoation handler with this registry. The newly added handler will have
     * precedence over the already registered handlers, that is, for every annotation, this handler will be checked
     * for support before the already registered handlers.
     *
     * @param handler The property validation annoation handler to be registered.
     */
    public void registerPropertyHandler(PropertyValidationAnnotationHandler handler) {
        propertyHandlers.add(0, handler);
    }

    /**
     * Registers the given property validation annoation handlers to the registry. These handlers will have precedence
     * over the already registered handlers, that is, for every annotation, these handlers will be check for suppport
     * before the already registered ones. The order of the given handler list is important for it determines the
     * precedence of the handlers within this list.
     *
     * @param handlers The extra property validation annotation handlers to register with this registry.
     */
    public void setExtraPropertyHandlers(List<PropertyValidationAnnotationHandler> handlers) {
        Collections.reverse(handlers);
        for (PropertyValidationAnnotationHandler handler : handlers) {
            registerPropertyHandler(handler);
        }
    }

    /**
     * Registers the given method validation annotation handler with this registry. The newly added handler will have
     * precedence over the already registered handlers, that is, for every annotation, this handler wil be checked
     * for support before the already registered ones.
     *
     * @param handler The method validation annotation handler to be registered.
     */
    public void registerMethodHandler(MethodValidationAnnotationHandler handler) {
        methodHandlers.add(0, handler);
    }

    /**
     * Registers the given method validation annoation handlers to the registry. These handlers will have precedence
     * over the already registered handlers, that is, for every annotation, these handlers will be check for suppport
     * before the already registered ones. The order of the given handler list is important for it determines the
     * precedence of the handlers within this list.
     *
     * @param handlers The extra method validation annotation handlers to register with this registry.
     */
    public void setExtraMethodHandlers(List<MethodValidationAnnotationHandler> handlers) {
        Collections.reverse(handlers);
        for (MethodValidationAnnotationHandler handler : handlers) {
            registerMethodHandler(handler);
        }
    }

    /**
     * @see org.springmodules.validation.util.cel.ConditionExpressionBased#setConditionExpressionParser(org.springmodules.validation.util.cel.ConditionExpressionParser)
     */
    public void setConditionExpressionParser(ConditionExpressionParser conditionExpressionParser) {

        this.conditionExpressionParser = conditionExpressionParser;
    }

    /**
     * @see org.springmodules.validation.util.fel.FunctionExpressionBased#setFunctionExpressionParser(org.springmodules.validation.util.fel.FunctionExpressionParser)
     */
    public void setFunctionExpressionParser(FunctionExpressionParser functionExpressionParser) {
        this.functionExpressionParser = functionExpressionParser;
    }

    public void afterPropertiesSet() throws Exception {

        findConditionExpressionParserInApplicationContext();
        findFunctionExpressionParserInApplicationContext();

        for (ClassValidationAnnotationHandler handler : classHandlers) {
            setExpressionParsers(handler);
            initLifecycle(handler);
        }

        for (PropertyValidationAnnotationHandler handler : propertyHandlers) {
            setExpressionParsers(handler);
            initLifecycle(handler);
        }
    }

    //=============================================== Helper Methods ===================================================

    protected void setExpressionParsers(Object object) {
        if (ConditionExpressionBased.class.isInstance(object) && conditionExpressionParser != null) {
            ((ConditionExpressionBased)object).setConditionExpressionParser(conditionExpressionParser);
        }
        if (FunctionExpressionBased.class.isInstance(object) && functionExpressionParser != null) {
            ((FunctionExpressionBased)object).setFunctionExpressionParser(functionExpressionParser);
        }
    }

    protected void findConditionExpressionParserInApplicationContext() {
        if (conditionExpressionParserSet) {
            return;
        }
        ConditionExpressionParser parser = (ConditionExpressionParser)findObjectInApplicationContext(ConditionExpressionParser.class);
        if (parser == null) {
            return;
        }
        conditionExpressionParser = parser;
    }

    protected void findFunctionExpressionParserInApplicationContext() {
        if (functionExpressionParserSet) {
            return;
        }
        FunctionExpressionParser parser = (FunctionExpressionParser)findObjectInApplicationContext(FunctionExpressionParser.class);
        if (parser == null) {
            return;
        }
        functionExpressionParser = parser;
    }

    protected Object findObjectInApplicationContext(Class clazz) {
        if (applicationContext == null) {
            return null;
        }
        String[] names = applicationContext.getBeanNamesForType(clazz);
        if (names.length == 0) {
            return null;
        }
        if (names.length > 1) {
            SimpleValidationAnnotationHandlerRegistry.logger.warn("Multiple bean of type '" + clazz.getName() + "' are defined in the application context." +
                "Only the first encountered one will be used");
        }
        return applicationContext.getBean(names[0]);
    }

}
