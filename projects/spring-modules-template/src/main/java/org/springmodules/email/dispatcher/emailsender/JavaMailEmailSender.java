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

import java.util.Iterator;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springmodules.email.Attachment;
import org.springmodules.email.Email;

/**
 * An email sender that know how to utilize {@link JavaMailSender} to send emails.
 *
 * @author Uri Boness
 */
public class JavaMailEmailSender implements EmailSender {

    // not supported out of the box by spring in 2.0.3 release.
    private final static String HEADER_PRIORITY = "X-Priority";

    /**
     * Expects a {@link org.springframework.mail.javamail.JavaMailSender} as the given mail sender.
     *
     * @see EmailSender#send(MailSender, Email, String)
     */
    public void send(MailSender sender, Email email, String encoding) {
        ((JavaMailSender)sender).send(generateMimeMessagePreparator(email, encoding));
    }

    /**
     * Creates a MimeMessagePreparator that can translate the given email to a {@link javax.mail.internet.MimeMessage}.
     *
     * @param email The email to be translated.
     * @param encoding The encoding of the email.
     * @return The appropriate MimeMessagePreparator.
     */
    protected MimeMessagePreparator generateMimeMessagePreparator(final Email email, final String encoding) {
        return new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setHeader(HEADER_PRIORITY, String.valueOf(email.getPriority().getRank()));
                for (Iterator iter = email.getHeaders().entrySet().iterator(); iter.hasNext();) {
                    Map.Entry entry = (Map.Entry)iter.next();
                    mimeMessage.setHeader(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                }
                MimeMessageHelper message =
                    new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, encoding);

                message.setTo(email.getTo());
                message.setBcc(email.getBcc());
                message.setCc(email.getCc());
                message.setFrom(email.getFrom());
                message.setSubject(email.getSubject());
                if (email.getTextBody() != null && email.getHtmlBody() != null) {
                    message.setText(email.getTextBody(), email.getHtmlBody());
                } else {
                    if (email.getTextBody() != null) {
                        message.setText(email.getTextBody(), false);
                    }
                    if (email.getHtmlBody() != null) {
                        message.setText(email.getHtmlBody(), true);
                    }
                }
                if (email.getReplyTo() != null) {
                    message.setReplyTo(email.getReplyTo());
                }
                for (Iterator iter = email.getAttachments().iterator(); iter.hasNext();) {
                    Attachment attachment = (Attachment) iter.next();
                    message.addAttachment(attachment.getName(), attachment.getResource());
                }
                for (Iterator iter = email.getInlineAttachments().iterator(); iter.hasNext();) {
                    Attachment attachment = (Attachment) iter.next();
                    message.addInline(attachment.getName(), attachment.getResource());
                }
            }
        };
    }
}
