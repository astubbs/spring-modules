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

package org.springmodules.email.dispatcher.emailsender;

import org.springmodules.email.Email;
import org.springframework.mail.MailSender;

/**
 * A a strategy to send emails based on a given {@link MailSender mail sender}.
 *
 * @author Uri Boness
 */
public interface EmailSender {

    /**
     * Sends the given email using the provided {@link MailSender} and encoding.
     *
     * @param sender The mail sender to use when sending the email.
     * @param email The email to send.
     * @param encoding The encoding of the email.
     */
    void send(MailSender sender, Email email, String encoding);

}
