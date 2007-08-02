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

import org.springmodules.email.Email;
import org.springmodules.email.dispatcher.emailsender.EmailSender;
import org.springframework.util.Assert;

/**
 * An email dispacher that uses a configured {@link EmailSender} to send the emails.
 *
 * @author Uri Boness
 */
public class ConfigurableEmailDispatcher extends AbstractEmailDispatcher {

    private EmailSender emailSender;

    /**
     * Sends the given email using the configured email sender.
     *
     * @param email The email to be sent.
     */
    public void send(Email email) {
        emailSender.send(getMailSender(), email, getEncoding());
    }

    public void doAfterPropertiesSet() throws Exception {
        Assert.notNull(emailSender, "Property 'emailSender' is required");
    }


    //============================================== Setter/Getter =====================================================

    /**
     * Returns the email sender that is used by this dispatcher.
     *
     * @return The email sender that is used by this dispatcher.
     */
    public EmailSender getEmailSender() {
        return emailSender;
    }

    /**
     * Sets the email sender that will be used by this dispatcher.
     *
     * @param emailSender The email sender that will be used by this dispatcher.
     */
    public void setEmailSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

}
