/*
 * Copyright (c) 2007, Your Corporation. All Rights Reserved.
 */

package org.springmodules.email.dispatcher.emailsender;

import com.dumbster.smtp.SimpleSmtpServer;
import junit.framework.TestCase;
import org.springframework.mail.cos.CosMailSenderImpl;

/**
 * This test is commented out. the crapy Cos MailMessage is hardcoded with the 25 port as the port for the mail server. This
 * port might already be taken by the build server.
 *
 * @author Uri Boness
 */
public class SimpleEmailSenderTests extends TestCase {

    private SimpleEmailSender sender;
    private CosMailSenderImpl mailSender;
    private SimpleSmtpServer server;

    public void testDummy() throws Exception {
    }

//    protected void setUp() throws Exception {
//        sender = new SimpleEmailSender();
//
//        mailSender = new CosMailSenderImpl();
//        mailSender.setHost("localhost");
//
//        server = SimpleSmtpServer.start(25);
//    }
//
//    protected void tearDown() throws Exception {
//        server.stop();
//    }
//
//    public void testSend() throws Exception {
//
//        Email email = new Email();
//        email.setFrom("From", "from@bla.com");
//        email.setTo("To", "to@bla.com");
//        email.setCc("cc@bla.com");
//        email.setBcc("bcc@bla.com");
//        email.setPriority(EmailPriority.HIGH);
//        email.setSubject("subject");
//        email.setTextBody("text");
//
//        sender.send(mailSender, email, "UTF-8");
//
//        assertEquals(1, server.getReceivedEmailSize());
//        SmtpMessage message = (SmtpMessage)server.getReceivedEmail().next();
//        assertEquals("From<from@bla.com>", message.getHeaderValue("From"));
//        assertEquals("To<to@bla.com>", message.getHeaderValue("To"));
//        assertEquals("cc@bla.com", message.getHeaderValue("Cc"));
//        assertEquals("subject", message.getHeaderValue("Subject"));
//        assertEquals("text", message.getBody());
//    }

}
