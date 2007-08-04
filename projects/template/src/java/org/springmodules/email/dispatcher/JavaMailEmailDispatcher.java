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
import org.springframework.mail.javamail.JavaMailSenderImpl;
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

    private String host;
    private int port = JavaMailSenderImpl.DEFAULT_PORT;
    private String protocol = JavaMailSenderImpl.DEFAULT_PROTOCOL;
    private String username;
    private String password;

    /**
     * Sends the given email.
     *
     * @param email The email to be sent
     */
    public void send(Email email) {
        sender.send(getMailSender(), email, getEncoding());
    }

    public void doAfterPropertiesSet() throws Exception {
        if (getMailSender() != null) {
            Assert.isInstanceOf(JavaMailSender.class, getMailSender(), "JavaMailEmailDispatcher can only work with JavaMailSender");
        } else {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(host);
            mailSender.setPort(port);
            mailSender.setProtocol(protocol);
            mailSender.setUsername(username);
            mailSender.setPassword(password);
            mailSender.setDefaultEncoding(getEncoding());
            setMailSender(mailSender);
        }
    }

    //============================================== Setter/Getter =====================================================

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
