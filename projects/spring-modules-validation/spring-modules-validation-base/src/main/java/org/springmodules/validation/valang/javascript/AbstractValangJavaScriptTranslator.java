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

package org.springmodules.validation.valang.javascript;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.JavaScriptUtils;
import org.springmodules.validation.valang.predicates.BasicValidationRule;

/**
 * Translates a collection of valang validation rules into a JavaScript statement that is capable of validating a HTML
 * form and writes this to a provided <code>Writer</code>. This class is <b>not</b> thread safe so it is recommended
 * that a new instance be created each time a translation is required.
 * <p/>
 * <p>
 * The generated JavaScript code is dependent on the code base found in the file "valang_codebase.js" having already
 * been loaded into the page where the validation will occur.
 * 
 * @author Oliver Hutchison
 */
public class AbstractValangJavaScriptTranslator {

    private static final String VALANG_CODEBASE_JS = "/META-INF/valang_codebase.js";

    /**
     * Returns a <code>Reader</code> for accessing the JavaScript codebase used by the translated validation rules.
     */
    public static Reader getCodebaseReader() {
        Class clazz = AbstractValangJavaScriptTranslator.class;
        InputStream resourceAsStream = clazz.getResourceAsStream(VALANG_CODEBASE_JS);

        Assert.notNull(resourceAsStream, "Valang Codebase not found!");
        return new InputStreamReader(resourceAsStream);
    }

    private static final Log logger = LogFactory.getLog(AbstractValangJavaScriptTranslator.class);

    private Writer writer;

    public AbstractValangJavaScriptTranslator() {
    }

    protected void setWriter(Writer writer) {
        Assert.state(this.writer == null,
                "Attempted to set writer when one already set - is this class being used is multiple threads?");
        this.writer = writer;
    }

    protected void clearWriter() {
        writer = null;
    }

    protected void append(String string) throws IOException {
        writer.write(string);
    }

    protected void appendJsString(String string) throws IOException {
        writer.write('\'');
        if (string == null) {
            writer.write("null");
        } else {
            writer.write(JavaScriptUtils.javaScriptEscape(string));
        }
        writer.write('\'');
    }

    protected void append(char c) throws IOException {
        writer.write(c);
    }

    protected void append(int i) throws IOException {
        writer.write(Integer.toString(i));
    }

    protected String getErrorMessage(BasicValidationRule rule, MessageSourceAccessor messageSource) {
        if (rule.getErrorArgs() != null && !rule.getErrorArgs().isEmpty()) {
            // TODO: implement message arguments in JavaScript
            logger.warn("Translating error message with arguments is not implemented; using default message");
            return rule.getErrorMessage();
        } else {
            return getErrorMessage(rule.getErrorKey(), rule.getErrorMessage(), messageSource);
        }
    }

    protected String getErrorMessage(String key, String defaultMsg, MessageSourceAccessor messageSource) {
        if (StringUtils.hasLength(key)) {
            return messageSource.getMessage(key, defaultMsg);
        } else {
            return defaultMsg;
        }
    }

}