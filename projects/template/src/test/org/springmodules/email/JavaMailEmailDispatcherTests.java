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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import junit.framework.TestCase;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springmodules.email.conf.SaxEmailParser;
import org.springmodules.template.engine.velocity.VelocityTemplateEngine;
import org.springmodules.template.resolver.BasicTemplateResolver;

/**
 * @author Uri Boness
 */
public class JavaMailEmailDispatcherTests extends TestCase {

    private JavaMailEmailDispatcher dispatcher;

    private SimpleSmtpServer server;

    protected void setUp() throws Exception {
        ResourceLoader loader = new ClassPathResourceLoader();
        VelocityTemplateEngine engine = new VelocityTemplateEngine(loader);
        engine.afterPropertiesSet();
        BasicTemplateResolver resolver = new BasicTemplateResolver(engine, loader);
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(2525);

        dispatcher = new JavaMailEmailDispatcher();
        dispatcher.setEmailParser(new SaxEmailParser(loader));
        dispatcher.setTemplateResolver(resolver);
        dispatcher.setMailSender(mailSender);

        server = SimpleSmtpServer.start(2525);
    }

    protected void tearDown() throws Exception {
        server.stop();
    }

    public void testSend() throws Exception {

        Map model = new HashMap();
        model.put("sender", new Person("Daan", "daan@bla.com"));
        model.put("recipient", new Person("Lian", "lian@bla.com"));
        dispatcher.send("email.eml", model);

        assertEquals(1, server.getReceivedEmailSize());
        SmtpMessage message = (SmtpMessage)server.getReceivedEmail().next();
        assertEquals("Daan <daan@bla.com>", message.getHeaderValue("From"));
        assertEquals("Lian <lian@bla.com>", message.getHeaderValue("To"));
        assertEquals("subject", message.getHeaderValue("Subject"));
        assertEquals("No Reply <noreply@bla.org>", message.getHeaderValue("Reply-To"));
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


    //============================================== Inner Classes =====================================================

    protected class ClassPathResourceLoader implements ResourceLoader {

        public Resource getResource(String location) {
            return new ClassPathResource(location, JavaMailEmailDispatcherTests.class);
        }

        public ClassLoader getClassLoader() {
            return Thread.currentThread().getContextClassLoader();
        }
    }
}