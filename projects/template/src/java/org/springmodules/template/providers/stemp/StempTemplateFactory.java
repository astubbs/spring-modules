/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.template.providers.stemp;

import java.io.*;

import org.springmodules.template.*;

/**
 * A {@link TemplateFactory} that creates {@link StempTemplate}'s.
 *
 * @author Uri Boness
 */
public class StempTemplateFactory extends AbstractTemplateFactory {

    private ExpressionWrapping expressionWrapping;
    private ExpressionResolver expressionResolver;

    public StempTemplateFactory() {
    }

    public StempTemplateFactory(ExpressionWrapping expressionWrapping, ExpressionResolver expressionResolver) {
        this.expressionWrapping = expressionWrapping;
        this.expressionResolver = expressionResolver;
    }

    public StempTemplateFactory(ExpressionWrapping expressionWrapping) {
        this.expressionWrapping = expressionWrapping;
    }

    public StempTemplateFactory(ExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
    }

    public Template createTemplate(TemplateSource source) throws TemplateCreationException {
        try {

            if (expressionResolver != null && expressionWrapping != null) {
                Stemp stemp = Stemp.compile(source.getReader(), expressionResolver, expressionWrapping);
                return new StempTemplate(stemp);
            }

            if (expressionResolver != null) {
                Stemp stemp = Stemp.compile(source.getReader(), expressionResolver);
                return new StempTemplate(stemp);
            }

            Stemp stemp = Stemp.compile(source.getReader(), expressionWrapping);
            return new StempTemplate(stemp);

        } catch (IOException ioe) {
            throw new TemplateCreationException("Could not create stemp template. Could not read stemp template source", ioe);
        } catch (StempException se) {
            throw new TemplateCreationException("Could not create stemp template", se);
        }

    }

    public void setExpressionWrapping(ExpressionWrapping expressionWrapping) {
        this.expressionWrapping = expressionWrapping;
    }

    public void setExpressionResolver(ExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
    }

}
