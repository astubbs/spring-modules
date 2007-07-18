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

import java.util.Iterator;

import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springmodules.email.Email;
import org.springmodules.email.Attachment;

/**
 * A JavaMail implementation of {@link AbstractEmailDispatcher} where a {@link JavaMailSender} is
 * assumed to be used and a {@link MimeMessage} is constructed based on the resolved email.
 *
 * @author Uri Boness
 * @see JavaMailSender
 */
public class JavaMailEmailDispatcher extends AbstractEmailDispatcher {

    // not supported out of the box by spring in 2.0.3 release.
    private final static String HEADER_PRIORITY = "X-Priority";

    /**
     * Sends the given email.
     *
     * @param email The email to be sent
     */
    public void send(Email email) {
        ((JavaMailSender)getMailSender()).send(generateMimeMessagePreparator(email));
    }

    /**
     * Creates a MimeMessagePreparator that can translate the given email to a {@link MimeMessage}.
     *
     * @param email The email to be translated.
     * @return The appropriate MimeMessagePreparator.
     */
    protected MimeMessagePreparator generateMimeMessagePreparator(final Email email) {
        return new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setHeader(HEADER_PRIORITY, String.valueOf(email.getPriority().getRank()));
                MimeMessageHelper message =
                    new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, getEncoding());
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
