/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.validation.bean.conf.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springmodules.validation.bean.conf.xml.handler.DateInFutureRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.DateInPastRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.EmailRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.LengthRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.NotBlankRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.NotEmptyRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.NotNullRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.RangeRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.RegExpRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.SizeRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.ValangRuleElementHandler;
import org.w3c.dom.Element;

/**
 * A default implementation of {@link ValidationRuleElementHandlerRegistry}. The order in which the the handlers are
 * registered with this registry is meaningful. The last to register will be the first to be checked for support.<br/>
 *
 * @author Uri Boness
 */
public class DefaultValidationRuleElementHandlerRegistry implements ValidationRuleElementHandlerRegistry {

    private final static ValidationRuleElementHandler[] DEFAULT_HANDLER;

    static {
        DEFAULT_HANDLER = new ValidationRuleElementHandler[] {
            new NotNullRuleElementHandler(),
            new LengthRuleElementHandler(),
            new NotBlankRuleElementHandler(),
            new EmailRuleElementHandler(),
            new RegExpRuleElementHandler(),
            new SizeRuleElementHandler(),
            new NotEmptyRuleElementHandler(),
            new RangeRuleElementHandler(),
            new ValangRuleElementHandler(),
            new DateInPastRuleElementHandler(),
            new DateInFutureRuleElementHandler()
        };
    }

    private List handlers;

    /**
     * Constructs a new DefaultValidationRuleElementHandlerRegistry with the following default handlers (in that order):
     *
     * <ol>
     *  <li>{@link NotNullRuleElementHandler}</li>
     *  <li>{@link LengthRuleElementHandler}</li>
     *  <li>{@link NotBlankRuleElementHandler}</li>
     *  <li>{@link EmailRuleElementHandler}</li>
     *  <li>{@link RegExpRuleElementHandler}</li>
     *  <li>{@link SizeRuleElementHandler}</li>
     *  <li>{@link NotEmptyRuleElementHandler}</li>
     *  <li>{@link RangeRuleElementHandler}</li>
     *  <li>{@link ValangRuleElementHandler}</li>
     *  <li>{@link DateInPastRuleElementHandler}</li>
     *  <li>{@link DateInFutureRuleElementHandler}</li>
     * </ol>
     */
    public DefaultValidationRuleElementHandlerRegistry() {
        this(DEFAULT_HANDLER);
    }

    /**
     * Constructs a new DefaultValidationRuleElementHandlerRegistry with the given handlers.
     *
     * @param handlers The handlers to register with this registry.
     */
    public DefaultValidationRuleElementHandlerRegistry(ValidationRuleElementHandler[] handlers) {
        this.handlers = new ArrayList();
        setExtraHandlers(handlers);
    }

    /**
     * Registers the given handler with this registry. The registered handler is registered in such a way that it will
     * be checked first for support (LIFC - Last In First Checked).
     *
     * @see ValidationRuleElementHandlerRegistry#registerHandler(ValidationRuleElementHandler)
     */
    public void registerHandler(ValidationRuleElementHandler handler) {
        handlers.add(0, handler);
    }

    /**
     * @see ValidationRuleElementHandlerRegistry#findHandler(org.w3c.dom.Element)
     */
    public ValidationRuleElementHandler findHandler(Element element) {
        for (Iterator iter = handlers.iterator(); iter.hasNext();) {
            ValidationRuleElementHandler handler = (ValidationRuleElementHandler)iter.next();
            if (handler.supports(element)) {
                return handler;
            }
        }
        return null;
    }


    //=============================================== Setter/Getter ====================================================

    /**
     * Registeres the given handlers with this registry.
     *
     * @param handlers The handlers to register with this registry.
     */
    public void setExtraHandlers(ValidationRuleElementHandler[] handlers) {
        for (int i=0; i<handlers.length; i++) {
            registerHandler(handlers[i]);
        }
    }

    /**
     * Return all handlers that are registered with this registry.
     *
     * @return All handlers that are registered with this registry.
     */
    public ValidationRuleElementHandler[] getHandlers() {
        return (ValidationRuleElementHandler[])handlers.toArray(new ValidationRuleElementHandler[handlers.size()]);
    }

}
