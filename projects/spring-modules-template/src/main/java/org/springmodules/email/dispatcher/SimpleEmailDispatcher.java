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

import org.springframework.mail.SimpleMailMessage;
import org.springmodules.email.Email;
import org.springmodules.email.dispatcher.emailsender.SimpleEmailSender;
import org.springmodules.email.dispatcher.emailsender.EmailSender;

/**
 * A simple implementation of {@link AbstractEmailDispatcher} where the type of the mail
 * sender does not matter and the email is translated to spring's {@link SimpleMailMessage}.
 * <p/>
 * <b>NOTE:</b> Using this dispatcher all attachments and address personal names will be ignored. Moreover, this
 *              dispatcher only support plain text bodies (no HTML support) as not all java mail senders support
 *              multipart messages.
 *
 * @author Uri Boness
 */
public class SimpleEmailDispatcher extends AbstractEmailDispatcher {

    private final static EmailSender sender = new SimpleEmailSender();

    /**
     * Sends the given email.
     *
     * @param email The email to be sent.
     */
    public void send(Email email) {
        sender.send(getMailSender(), email, getEncoding());
    }

}
