package org.springmodules.validation.bean.conf.xml;

import junit.framework.TestCase;
import org.easymock.MockControl;

/**
 * Tests for {@link org.springmodules.validation.bean.conf.xml.ValidationRuleElementHandlerRegistrar}.
 *
 * @author Uri Boness
 */
public class ValidationRuleElementHandlerRegistrarTests extends TestCase {

    private ValidationRuleElementHandlerRegistrar registrar;

    private MockControl registryControl;
    private ValidationRuleElementHandlerRegistry registry;

    private MockControl handler1Control;
    private ValidationRuleElementHandler handler1;

    private MockControl handler2Control;
    private ValidationRuleElementHandler handler2;

    private ValidationRuleElementHandler[] handlers;

    protected void setUp() throws Exception {

        registryControl = MockControl.createControl(ValidationRuleElementHandlerRegistry.class);
        registry = (ValidationRuleElementHandlerRegistry)registryControl.getMock();

        handler1Control = MockControl.createControl(ValidationRuleElementHandler.class);
        handler1 = (ValidationRuleElementHandler)handler1Control.getMock();

        handler2Control = MockControl.createControl(ValidationRuleElementHandler.class);
        handler2 = (ValidationRuleElementHandler)handler2Control.getMock();

        handlers = new ValidationRuleElementHandler[] {handler1, handler2};

        registrar = new ValidationRuleElementHandlerRegistrar(registry, handlers);
    }

    public void testAfterPropertiesSet() throws Exception {
        registry.registerHandler(handler1);
        registry.registerHandler(handler2);

        replay();
        registrar.afterPropertiesSet();
        verify();
    }

    public void testAfterPropertiesSet_WithNoHandlers() throws Exception {
        registrar = new ValidationRuleElementHandlerRegistrar(registry, new ValidationRuleElementHandler[0]);

        replay();
        registrar.afterPropertiesSet();
        verify();
    }

    //=============================================== Helper Methods ===================================================

    protected void replay() {
        registryControl.replay();
        handler1Control.replay();
        handler2Control.replay();
    }

    protected void verify() {
        registryControl.verify();
        handler1Control.verify();
        handler2Control.verify();
    }
}