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

package org.springmodules.validation.bean.conf.annotation.handler;

import java.lang.annotation.Annotation;

import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.parser.valang.ValangConditionParser;

/**
 * An {@link AbstractClassValidationAnnotationHandler} implementation that handles {@link Valang} annnotations.
 *
 * @author Uri Boness
 */
public class ValangClassValidationAnnotationHandler extends AbstractClassValidationAnnotationHandler {

    private ValangConditionParser parser;

    /**
     * Constructs a new ValangPropertyValidationAnnotationHandler.
     */
    public ValangClassValidationAnnotationHandler() {
        super(Valang.class);
        parser = new ValangConditionParser();
    }

    /**
     * Creates a condition out of the given valang annotation.
     *
     * @see AbstractClassValidationAnnotationHandler#extractCondition(java.lang.annotation.Annotation, Class)
     */
    protected Condition extractCondition(Annotation annotation, Class clazz) {
        Valang valang = (Valang)annotation;
        return parser.parse(valang.value());
    }

    /**
     * Returns the valang condition parser this handler uses to parse the valang expressions.
     *
     * @return The valang condition parser this handler uses to parse the valang expressions.
     */
    public ValangConditionParser getParser() {
        return parser;
    }

    /**
     * Sets the valang condition parser this handler will use to parse the valang expressions.
     *
     * @param parser The valang condition parser this handler will use to parse the valang expressions.
     */
    public void setParser(ValangConditionParser parser) {
        this.parser = parser;
    }

}
