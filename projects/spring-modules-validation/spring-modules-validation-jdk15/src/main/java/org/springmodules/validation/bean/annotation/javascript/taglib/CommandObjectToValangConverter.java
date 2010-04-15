package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.valang.javascript.AbstractValangJavaScriptTranslator;

public class CommandObjectToValangConverter extends AbstractValangJavaScriptTranslator {

    private static final String FIRST_LINE_PREFIX = "\n";

    private static final String LINE_PREFIX = ",\n";

    String prefix;

    private final static Log logger = LogFactory.getLog(CommandObjectToValangConverter.class);

    Collection<Handler> supportedFieldAnnotations = new ArrayList<Handler>();

    Collection<Handler> supportedClassAnnotations = new ArrayList<Handler>();

    public CommandObjectToValangConverter() {
        super();
        // class level
        registerClassAnnotationHandler(new ExpressionHandler());
        registerClassAnnotationHandler(new ExpressionsHandler());

        // fields
        registerFieldAnnotationHandler(new EmailHandler());
        registerFieldAnnotationHandler(new ExpressionHandler());
        registerFieldAnnotationHandler(new ExpressionsHandler());
        registerFieldAnnotationHandler(new InThePastFutureHandler());
        registerFieldAnnotationHandler(new LengthSizeHandler());
        registerFieldAnnotationHandler(new MaxHandler());
        registerFieldAnnotationHandler(new MaxLengthSizeHandler());
        registerFieldAnnotationHandler(new MinHandler());
        registerFieldAnnotationHandler(new MinLengthSizeHandler());
        registerFieldAnnotationHandler(new NotBlankEmptyHandler());
        registerFieldAnnotationHandler(new NotNullHandler());
        registerFieldAnnotationHandler(new RangeHandler());
        registerFieldAnnotationHandler(new RegExpHandler());
    }

    public void writeJS(String commandName, Object commandObj, JspWriter out, MessageSourceAccessor messages)
            throws IOException {

        setWriter(out);
        prefix = FIRST_LINE_PREFIX; // No comma for first line

        out.write("new ValangValidator(");
        appendJsString(commandName);
        append(',');
        append(Boolean.toString(true)); // install to the form on creation
        append(", new Array(");

        // class level
        Annotation[] annotations = commandObj.getClass().getAnnotations();
        extractAnnotations(commandObj, messages, null, annotations);

        // field level
        for (Field field : commandObj.getClass().getDeclaredFields()) {
            annotations = field.getAnnotations();
            extractAnnotations(commandObj, messages, field, annotations);
        }

        append("\n) )");
        clearWriter();
    }

    /**
     * Extracts valang from the annotations supplied.
     * 
     * @param commandObj
     * @param messages
     * @param field
     *            The field the annotations are from. If null
     * @param annotations
     * @throws IOException
     */
    private void extractAnnotations(Object commandObj, MessageSourceAccessor messages, Field field,
            Annotation[] annotations) throws IOException {

        boolean isClassLevel = (field == null);

        for (Annotation annotation : annotations) {
            Handler hndl = (isClassLevel) ? getClassHandler(annotation) : getFieldHandler(annotation);

            if (hndl != null) {
                String valangJS = hndl.convertToValang(field, annotation, messages);
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

    private Handler getFieldHandler(Annotation annotation) {
        return getHandler(annotation, supportedFieldAnnotations);
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

    public void registerFieldAnnotationHandler(Handler handler) {
        if (handler == null) {
            return;
        }
        supportedFieldAnnotations.add(handler);
    }

    public void registerClassAnnotationHandler(Handler handler) {
        if (handler == null) {
            return;
        }
        supportedClassAnnotations.add(handler);
    }

}
