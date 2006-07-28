package org.springmodules.validation.bean.conf.loader.annotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;

/**
 * Tests for {@link org.springmodules.validation.bean.conf.loader.annotation.DefaultValidationAnnotationHandlerRegistry}.
 *
 * @author Uri Boness
 */
@TestAnnotation
public class DefaultValidationAnnotationHandlerRegistryTests extends TestCase {

    private DefaultValidationAnnotationHandlerRegistry registry;

    private ClassValidationAnnotationHandler classHandler;
    private MockControl classHandlerControl;

    private PropertyValidationAnnotationHandler propertyHandler;
    private MockControl propertyHandlerControl;

    protected void setUp() throws Exception {

        classHandlerControl = MockControl.createControl(ClassValidationAnnotationHandler.class);
        classHandler = (ClassValidationAnnotationHandler)classHandlerControl.getMock();

        propertyHandlerControl = MockControl.createControl(PropertyValidationAnnotationHandler.class);
        propertyHandler = (PropertyValidationAnnotationHandler)propertyHandlerControl.getMock();

        registry = new DefaultValidationAnnotationHandlerRegistry() {
            protected void registerDefaultHandlers() {
            }
        };
    }

    public void testFindClassHanlder() throws Exception {

        Annotation annotation = getAnnotation();
        classHandlerControl.expectAndReturn(classHandler.supports(annotation, Object.class), true);
        classHandlerControl.replay();

        registry.registerClassHandler(classHandler);
        Object returnedHandler = registry.findClassHanlder(annotation, Object.class);

        assertSame(classHandler, returnedHandler);

        classHandlerControl.verify();
    }

    public void testFindClassHanlder_WhenNotFound() throws Exception {

        Annotation annotation = getAnnotation();
        classHandlerControl.expectAndReturn(classHandler.supports(annotation, Object.class), false);
        classHandlerControl.replay();

        registry.registerClassHandler(classHandler);
        Object returnedHandler = registry.findClassHanlder(annotation, Object.class);

        assertNull(returnedHandler);

        classHandlerControl.verify();
    }

    public void testSetExtraClassHandlers() throws Exception {
        List<ClassValidationAnnotationHandler> handlers = new ArrayList<ClassValidationAnnotationHandler>();
        handlers.add(createClassHandler(false));
        handlers.add(createClassHandler(false));
        handlers.add(createClassHandler(false));
        ClassValidationAnnotationHandler h1 = createClassHandler(true);
        handlers.add(h1);

        registry.setExtraClassHandlers(handlers);

        Object returnedHandler = registry.findClassHanlder(getAnnotation(), Object.class);

        assertSame(h1, returnedHandler);

    }


    //=============================================== Helper Methods ===================================================

    protected Annotation getAnnotation() {
        return getClass().getAnnotation(TestAnnotation.class);
    }

    protected ClassValidationAnnotationHandler createClassHandler(final boolean supports) {
        return new ClassValidationAnnotationHandler() {
            public boolean supports(Annotation annotation, Class clazz) {
                return supports;
            }
            public void handleAnnotation(Annotation annotation, Class clazz, MutableBeanValidationConfiguration configuration) {
            }
        };
    }


}