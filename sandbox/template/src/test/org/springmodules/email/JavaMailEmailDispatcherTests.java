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
package org.springmodules.email;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import junit.framework.TestCase;
import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 * @author Uri Boness
 */
public class JavaMailEmailDispatcherTests extends TestCase {

    private JavaMailEmailDispatcher dispatcher;

    protected void setUp() throws Exception {
        dispatcher = new JavaMailEmailDispatcher();
    }

    public void testGenerateMimeMessagePreparator() throws Exception {

        Email email = new Email();
        email.setFrom("From", "from@bla.com");
        email.setReplyTo("Reply To", "replyto@bla.com");
        email.setTo("to@bla.com");
        email.setCc("cc@bla.com");
        email.setBcc("bcc@bla.com");
        email.setPriority(EmailPriority.HIGH);
        email.setSubject("subject");
        email.setTextBody("text");
        
        MimeMessagePreparator preparator = dispatcher.generateMimeMessagePreparator(email);
        MimeMessage mimeMessage = createMimeMessage();
        preparator.prepare(mimeMessage);

        assertEquals(1, mimeMessage.getFrom().length);
        InternetAddress address = (InternetAddress)mimeMessage.getFrom()[0];
        assertEquals("From", address.getPersonal());
        assertEquals("from@bla.com", address.getAddress());

        Address[] addresses = mimeMessage.getRecipients(MimeMessage.RecipientType.TO);
        assertEquals(1, addresses.length);
        address = (InternetAddress)addresses[0];
        assertEquals("to@bla.com", address.getAddress());

        addresses = mimeMessage.getRecipients(MimeMessage.RecipientType.CC);
        assertEquals(1, addresses.length);
        address = (InternetAddress)addresses[0];
        assertEquals("cc@bla.com", address.getAddress());

        addresses = mimeMessage.getRecipients(MimeMessage.RecipientType.BCC);
        assertEquals(1, addresses.length);
        address = (InternetAddress)addresses[0];
        assertEquals("bcc@bla.com", address.getAddress());

        assertEquals("2", mimeMessage.getHeader("X-Priority")[0]);
        assertEquals("subject", mimeMessage.getSubject());
    }

    protected MimeMessage createMimeMessage() {
        Session session = Session.getInstance(new Properties());
        return new MimeMessage(session);
    }
}