/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springmodules.email.dispatcher;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.MailSender;
import org.springframework.util.Assert;
import org.springmodules.email.Email;
import org.springmodules.email.EmailDispatcher;
import org.springmodules.email.EmailParser;
import org.springmodules.email.EmailPreparator;
import org.springmodules.email.parser.xml.SaxEmailParser;
import org.springmodules.template.Template;
import org.springmodules.template.TemplateResolver;
import org.springmodules.util.StringResource;

/**
 * A template based based class for email dispatchers. A {@link org.springmodules.template.TemplateResolver} is used
 * to resolve the appropriate template based on the email name. Then the template is executed with the email model,
 * and the output of that execution is parsed using a {@link EmailParser} into an {@link org.springmodules.email.Email} object which is sent
 * using the configured {@link org.springframework.mail.MailSender}.
 *
 * @author Uri Boness
 */
public abstract class AbstractEmailDispatcher implements EmailDispatcher, InitializingBean, ResourceLoaderAware {

    private final static Log logger = LogFactory.getLog(AbstractEmailDispatcher.class);

    private final static EmailPreparator EMPTY_PREPARATOR = new EmailPreparator() {
        public Email prepare(Email email) {
            return email;
        }
    };

    private final static String DEFAULT_ENCODING = "UTF-8";

    private TemplateResolver templateResolver;

    private MailSender mailSender;

    private EmailParser emailParser;

    private ResourceLoader resourceLoader;

    private String encoding = DEFAULT_ENCODING;
    
    /**
     * Sends the email associated with the given name.
     *
     * @param emailName The name of the email to send.
     */
    public void send(String emailName) {
        send(emailName, (Locale)null);
    }

    public void send(String emailName, Locale locale) {
        send(emailName, new HashMap(), locale);
    }

    /**
     * Sends the email associated with the given email name, while populating it first with the given model.
     *
     * @param emailName The name of the email to send.
     * @param model The model to populate the email prior to sending.
     */
    public void send(String emailName, Map model) {
        send(emailName, model, (Locale)null);
    }

    public void send(String emailName, Map model, Locale locale) {
        send(emailName, model, EMPTY_PREPARATOR);
    }

    public void send(String emailName, Map model, EmailPreparator emailPreparator) {
        send(emailName, model, null, emailPreparator);
    }

    public void send(String emailName, Map model, Locale locale, EmailPreparator emailPreparator) {
        Email email = resolveEmail(emailName, model, locale);
        email = emailPreparator.prepare(email);
        send(email);
    }

    // todo improve performance using pipes ?
    /**
     * Resolves the {@link Email} by its name and given model.
     *
     * @param name The name of the email.
     * @param model The model to populate the email.
     * @return The resolved email.
     */
    protected Email resolveEmail(String name, Map model, Locale locale) {
        StringWriter writer = new StringWriter();
        Template template = templateResolver.resolve(name, getEncoding(), locale);
        template.generate(writer, model);

        if (logger.isDebugEnabled()) {
            logger.debug("The generated email descriptor for '" + name + "' is:\n" + writer.toString());
        }

        return emailParser.parse(new StringResource(writer.toString()));
    }

    /**
     * Called by the Spring application context after this object has been wired with its dependencies. This method
     * only validates that the wiring is correct.
     *
     * @throws Exception Thrown if for some reason the wiring of this object is incorrect.
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public final void afterPropertiesSet() throws Exception {
        doAfterPropertiesSet();
        if (emailParser == null && resourceLoader != null) {
            emailParser = new SaxEmailParser(resourceLoader);
        }
        Assert.notNull(templateResolver, "Property 'templateResolver' must be set");
        Assert.notNull(mailSender, "Property 'mailSender' must be set");
        Assert.notNull(emailParser, "Property 'templateResolver' must be set. Otherwise please set the " +
            "'resourceLoader' property to enable using the default email parser (SaxEmailParser)");
        Assert.notNull(templateResolver, "Property 'templateResolver' must be set");
    }

    public void doAfterPropertiesSet() throws Exception {
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    //============================================== Setter/Getter =====================================================

    /**
     * Sets the template resolver that is used to resolve the email templates based on the email name and model.
     *
     * @param templateResolver The template resolver that is used to resolve the email templates.
     */
    public void setTemplateResolver(TemplateResolver templateResolver) {
        this.templateResolver = templateResolver;
    }

    /**
     * Returns the template resolver that is used to resolve the email templates based on the email name and model.
     *
     * @return The template resolver that is used to resolve the email templates based on the email name and model.
     */
    protected TemplateResolver getTemplateResolver() {
        return templateResolver;
    }

    /**
     * Sets the {@link MailSender} by which the emails will be sent.
     *
     * @param mailSender The email sender by which the emails will be sent.
     */
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Returns the mail sender by which the emails will be sent.
     *
     * @return The mail sender by which the emails will be sent.
     */
    protected MailSender getMailSender() {
        return mailSender;
    }

    /**
     * Sets the {@link EmailParser} that will be used to parse the email template outputs.
     *
     * @param emailParser The mail parser that will be used to parse the email template outputs.
     */
    public void setEmailParser(EmailParser emailParser) {
        this.emailParser = emailParser;
    }

    /**
     * Sets the encoding of the emails to be sent. The default encoding is set to UTF-8.
     *
     * @param encoding The encoding of the emails to be sent.
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Returns the encoding of the emails to be sent.
     *
     * @return The encoding of the emails to be sent.
     */
    public String getEncoding() {
        return encoding;
    }

}
