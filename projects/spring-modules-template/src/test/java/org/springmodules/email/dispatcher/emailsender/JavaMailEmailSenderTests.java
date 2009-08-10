/*
 * Copyright (c) 2007, Your Corporation. All Rights Reserved.
 */

package org.springmodules.email.dispatcher.emailsender;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import junit.framework.TestCase;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springmodules.email.Email;
import org.springmodules.email.EmailPriority;

/**
 * @author Uri Boness
 */
public class JavaMailEmailSenderTests extends TestCase {

    private JavaMailEmailSender sender;

    protected void setUp() throws Exception {
        sender = new JavaMailEmailSender();
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

        MimeMessagePreparator preparator = sender.generateMimeMessagePreparator(email, "UTF-8");
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