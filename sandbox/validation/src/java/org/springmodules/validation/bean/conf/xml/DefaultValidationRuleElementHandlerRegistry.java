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

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.springmodules.validation.bean.conf.xml.parser.LengthRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.parser.EmailRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.parser.NotBlankRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.parser.NotEmptyRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.parser.NotNullRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.parser.RangeRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.parser.RegExpRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.parser.SizeRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.parser.ValangRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.parser.DateInPastRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.parser.DateInFutureRuleElementHandler;

/**
 * A default implementation of {@link ValidationRuleElementHandlerRegistry}. The order in which the the handlers are
 * registered with this registry is meaningful. The last to register will be the first to be checked for support.<br/>
 *
 * @author Uri Boness
 */
public class DefaultValidationRuleElementHandlerRegistry implements ValidationRuleElementHandlerRegistry {

    private final static ValidationRuleElementHandler[] DEFAULT_PARSERS;

    static {
        DEFAULT_PARSERS = new ValidationRuleElementHandler[] {
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
        this(DEFAULT_PARSERS);
    }

    /**
     * Constructs a new DefaultValidationRuleElementHandlerRegistry with the given handlers.
     *
     * @param handlers The handlers to register with this registry.
     */
    public DefaultValidationRuleElementHandlerRegistry(ValidationRuleElementHandler[] handlers) {
        this.handlers = new ArrayList();
        setExtraParsers(handlers);
    }

    /**
     * Registers the given handler with this registry. The registered handler is registered in such a way that it will
     * be checked first for support (LIFC - Last In First Checked).
     *
     * @see ValidationRuleElementHandlerRegistry#registerParser(ValidationRuleElementHandler)
     */
    public void registerParser(ValidationRuleElementHandler handler) {
        handlers.add(0, handler);
    }

    /**
     * @see ValidationRuleElementHandlerRegistry#findHandler(org.w3c.dom.Element)
     */
    public ValidationRuleElementHandler findHandler(Element element) {
        for (Iterator iter = handlers.iterator(); iter.hasNext();) {
            ValidationRuleElementHandler parser = (ValidationRuleElementHandler)iter.next();
            if (parser.supports(element)) {
                return parser;
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
    public void setExtraParsers(ValidationRuleElementHandler[] handlers) {
        for (int i=0; i<handlers.length; i++) {
            registerParser(handlers[i]);
        }
    }

}
