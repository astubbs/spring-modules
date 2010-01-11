package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.valang.javascript.AbstractValangJavaScriptTranslator;

public class CommandObjectToValangConverter extends AbstractValangJavaScriptTranslator {

    public enum AnnotationLocation {
        METHOD, FIELD, CLASS
    }

    public static final String CLASS_RULENAME = "valang-global-rules";

    private static final String FIRST_LINE_PREFIX = "\n";

    private static final String LINE_PREFIX = ",\n";

    String prefix;

    private final static Log logger = LogFactory.getLog(CommandObjectToValangConverter.class);

    Collection<Handler> supportedFieldMethodAnnotations = new ArrayList<Handler>();

    Collection<Handler> supportedClassAnnotations = new ArrayList<Handler>();

    public CommandObjectToValangConverter() {
        super();
        // class level
        registerClassAnnotationHandler(new ExpressionHandler());
        registerClassAnnotationHandler(new ExpressionsHandler());

        // fields / methods
        registerFieldMethodAnnotationHandler(new EmailHandler());
        registerFieldMethodAnnotationHandler(new ExpressionHandler());
        registerFieldMethodAnnotationHandler(new ExpressionsHandler());
        registerFieldMethodAnnotationHandler(new InThePastFutureHandler());
        registerFieldMethodAnnotationHandler(new LengthSizeHandler());
        registerFieldMethodAnnotationHandler(new MaxHandler());
        registerFieldMethodAnnotationHandler(new MaxLengthSizeHandler());
        registerFieldMethodAnnotationHandler(new MinHandler());
        registerFieldMethodAnnotationHandler(new MinLengthSizeHandler());
        registerFieldMethodAnnotationHandler(new NotBlankEmptyHandler());
        registerFieldMethodAnnotationHandler(new NotNullHandler());
        registerFieldMethodAnnotationHandler(new RangeHandler());
        registerFieldMethodAnnotationHandler(new RegExpHandler());
    }

    public void writeJS(String commandName, Object commandObj, String globalVar, boolean validateOnSubmit,
            JspWriter out, MessageSourceAccessor messages) throws IOException {

        try {
            setWriter(out);
            prefix = FIRST_LINE_PREFIX; // No comma for first line

            if (globalVar != null) {
                out.write("var " + globalVar + " = ");
            }
            out.write("new ValangValidator(");
            appendJsString(commandName);
            append(',');
            append(Boolean.toString(true)); // install to the form
            append(", new Array(");

            Class<? extends Object> commandObjClass = commandObj.getClass();

            // class level
            Annotation[] annotations = commandObjClass.getAnnotations();
            extractAnnotations(commandObj, messages, CLASS_RULENAME, annotations, AnnotationLocation.CLASS);

            // field level: traverse superclasses, as getDeclaredFields() doesn't, and getFields() excludes private fields
            Class<? extends Object> commandOrSuper = commandObjClass;
            do {
                for (Field field : commandOrSuper.getDeclaredFields()) {
                    annotations = field.getAnnotations();
                    extractAnnotations(commandObj, messages, field.getName(), annotations, AnnotationLocation.FIELD);
                }
                commandOrSuper = commandOrSuper.getSuperclass();
            } while (commandOrSuper != null);

            // method level
            for (Method m : commandObjClass.getMethods()) {
                annotations = m.getAnnotations();
                extractAnnotations(commandObj, messages, m.getName(), annotations, AnnotationLocation.METHOD);
            }

            append("\n),");
            append(Boolean.toString(validateOnSubmit) + ");");
        } finally {
            clearWriter();
        }
    }

    /**
     * Extracts valang from the annotations supplied.
     * 
     * @param commandObj
     * @param messages
     * @param name
     *            The field/method the annotations are from. If null
     * @param annotations
     * @throws IOException
     */
    private void extractAnnotations(Object commandObj, MessageSourceAccessor messages, String name,
            Annotation[] annotations, AnnotationLocation aLoc) throws IOException {

        for (Annotation annotation : annotations) {

            Handler hndl = (aLoc == AnnotationLocation.CLASS) ? getClassHandler(annotation)
                    : getFieldMethodHandler(annotation);

            if (hndl != null) {
                if (hndl.isDelegateAnnotations()) {
                    Annotation[] delegateAnnotations = hndl.getDelegateAnnotations(annotation, name);
                    if (delegateAnnotations != null) {
                        extractAnnotations(commandObj, messages, name, delegateAnnotations, aLoc);
                    }
                    continue;
                }
                String propName = (aLoc == AnnotationLocation.METHOD) ? convertMethodNameToProperty(name) : name;
                String valangJS = hndl.convertToValang(propName, annotation, messages);
                if (valangJS != null) {
                    append(prefix + valangJS);
                    prefix = LINE_PREFIX;
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Annotation " + annotation.getClass() + " on command object " + commandObj.getClass()
                            + " generated: " + valangJS);
                }

            } else if (logger.isWarnEnabled()) {
                logger.warn("Unsupported annotation " + annotation.getClass() + " on command object "
                        + commandObj.getClass());
            }

        }
    }

    private Handler getClassHandler(Annotation annotation) {
        return getHandler(annotation, supportedClassAnnotations);
    }

    private Handler getFieldMethodHandler(Annotation annotation) {
        return getHandler(annotation, supportedFieldMethodAnnotations);
    }

    private Handler getHandler(Annotation annotation, Collection<Handler> handlers) {
        for (Handler h : handlers) {
            if (h.supports(annotation)) {
                return h;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Annotation not supported: " + annotation);
        }
        return null;
    }

    public void registerFieldMethodAnnotationHandler(Handler handler) {
        if (handler == null) {
            return;
        }
        supportedFieldMethodAnnotations.add(handler);
    }

    public void registerClassAnnotationHandler(Handler handler) {
        if (handler == null) {
            return;
        }
        supportedClassAnnotations.add(handler);
    }

    public void setRegisterFieldMethodHandlers(Collection<Handler> handlers) {
        for (Handler h : handlers) {
            registerFieldMethodAnnotationHandler(h);
        }
    }

    public void setRegisterClassHandlers(Collection<Handler> handlers) {
        for (Handler h : handlers) {
            registerClassAnnotationHandler(h);
        }
    }

    public String convertMethodNameToProperty(String in) {
        if (in == null) {
            return null;
        }
        // TODO find elegant spring way
        String out = in.replaceFirst("^is", "").replaceFirst("^get", "").replaceFirst("^set", "");
        if (out.length() == 0) {
            return null;
        }
        out = out.substring(0, 1).toLowerCase() + out.substring(1); // drop first letter case

        return out;
    }
}
