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

import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.Assert;
import org.springmodules.email.Email;
import org.springmodules.email.dispatcher.emailsender.JavaMailEmailSender;
import org.springmodules.email.dispatcher.emailsender.EmailSender;

/**
 * A JavaMail implementation of {@link AbstractEmailDispatcher} where a {@link JavaMailSender} is
 * assumed to be used and a {@link MimeMessage} is constructed based on the resolved email.
 *
 * @author Uri Boness
 * @see JavaMailSender
 */
public class JavaMailEmailDispatcher extends AbstractEmailDispatcher {

    private final static EmailSender sender = new JavaMailEmailSender();

    /**
     * Sends the given email.
     *
     * @param email The email to be sent
     */
    public void send(Email email) {
        sender.send(getMailSender(), email, getEncoding());
    }

    public void doAfterPropertiesSet() throws Exception {
        Assert.isInstanceOf(JavaMailSender.class, getMailSender(), "JavaMailEmailDispatcher can only work with JavaMailSender");
    }
}
