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

package org.springmodules.validation.bean.annotation.javascript.taglib;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.BaseCommandController;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springmodules.validation.valang.ValangValidator;
import org.springmodules.validation.valang.javascript.taglib.ValangCodebaseTag;
import org.springmodules.validation.valang.javascript.taglib.ValangJavaScriptTagUtils;

/**
 * Generates JavaScript validation code from bean annotations The generated code requires a set of JavaScript objects to
 * have been placed into the page either by using the {@link ValangCodebaseTag} or by directly including the code from
 * the file "valang_codebase.js" located in the org.springmodules.validation.valang.javascript package.
 * <p/>
 * <p>
 * If the tag has any body content this will be interpreted as a set of additional valang rules that will be appended to
 * the set of rules located for the provided command name; or if no command name is specified the translated body
 * content will be used to provide all rules.
 * <p/>
 * <p>
 * NOTE: this tag must be placed inside the HTML form tags that the validation rules are expected to apply too; failure
 * to do this will result in a JavaScript exception being thrown when the page loads.
 * 
 * @author Tim Taylor
 * @author Oliver Hutchison
 */

public class BeanAnnotationValidateTag extends RequestContextAwareTag implements BodyTag {

    public BeanAnnotationValidateTag() {
        super();

    }

    public static final String DEFAULT_COMMAND_NAME = "command";

    private String commandName = DEFAULT_COMMAND_NAME;

    private String globalVar = null;

    private boolean validateOnSubmit = true;

    private Object commandObj = null;

    private CommandObjectToValangConverter cotvc = null;

    public void setConverter(CommandObjectToValangConverter cotvc) {
        this.cotvc = cotvc;
    }

    /**
     * Sets the name of the command which will be validated by the generated JavaScript. If this value is specified it
     * is expected that a collection of valang rules for the specified command name have been placed in the model using
     * one of the methods from {@link ValangJavaScriptTagUtils}.
     * 
     * @see ValangJavaScriptTagUtils#addValangRulesToModel(BaseCommandController, Map)
     * @see ValangJavaScriptTagUtils#addValangRulesToModel(String, ValangValidator, Map)
     */
    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public void setCommandObj(Object commandObj) {
        this.commandObj = commandObj;
    }

    public void setGlobalVar(String globalVar) {
        this.globalVar = globalVar;
    }

    public void setValidateOnSubmit(boolean validateOnSubmit) {
        this.validateOnSubmit = validateOnSubmit;
    }

    protected int doStartTagInternal() {
        return EVAL_BODY_BUFFERED;
    }

    public void doInitBody() {
        // do nothing
    }

    public void setBodyContent(BodyContent bodyContent) {
        // do nothing
    }

    public int doAfterBody() throws JspException {
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        if (cotvc == null) {
            findConverter();
        }
        try {
            if (commandObj == null) { // favour commandObj over command name
                commandObj = this.pageContext.getAttribute(commandName, PageContext.REQUEST_SCOPE);
                if (commandName == null || commandObj == null) {
                    logger.error("Command object not found");
                    return EVAL_PAGE;
                }
            }
            JspWriter out = pageContext.getOut();
            Locale locale = getRequestContext().getLocale();
            WebApplicationContext webApplicationContext = getRequestContext().getWebApplicationContext();
            MessageSourceAccessor messages = new MessageSourceAccessor(webApplicationContext, locale);

            out.write("<script type=\"text/javascript\" id=\"" + commandName + "ValangValidator\">\n");
            cotvc.writeJS(commandName, commandObj, globalVar, validateOnSubmit, out, messages);
            out.write("\n</script>");

            return EVAL_PAGE;

        } catch (IOException e) {
            throw new JspException("Could not write validation rules", e);
        }
    }

    public void doFinally() {
        super.doFinally();
        commandName = null;
    }

    public void findConverter() throws BeansException {

        Map beansOfType = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                getRequestContext().getWebApplicationContext(), CommandObjectToValangConverter.class);
        if (beansOfType.size() >= 1) {
            cotvc = (CommandObjectToValangConverter) beansOfType.values().iterator().next();
        } else {
            cotvc = new CommandObjectToValangConverter();
        }

    }
}