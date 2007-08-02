/*
 * Copyright (c) 2007, Your Corporation. All Rights Reserved.
 */

package org.springmodules.email.dispatcher.emailsender;

import java.util.Date;

import javax.mail.internet.InternetAddress;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springmodules.email.Email;
import org.springmodules.email.dispatcher.emailsender.EmailSender;

/**
 * @author Uri Boness
 */
public class SimpleEmailSender implements EmailSender {

    public void send(MailSender sender, Email email, String encoding) {
        sender.send(generateSimpleMessage(email));
    }

    /**
     * Generates a {@link org.springframework.mail.SimpleMailMessage} from the given email.
     *
     * @param email The given email.
     * @return The newly generated {@link org.springframework.mail.SimpleMailMessage}.
     */
    protected SimpleMailMessage generateSimpleMessage(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email.getFrom().getAddress());
        message.setTo(extractEmails(email.getTo()));
        message.setCc(extractEmails(email.getCc()));
        message.setBcc(extractEmails(email.getBcc()));
        if (email.getReplyTo() != null) {
            message.setReplyTo(email.getReplyTo().getAddress());
        }
        message.setSentDate(new Date());
        message.setSubject(email.getSubject());
        message.setText(email.getTextBody());
        return message;
    }


    //============================================== Helper Methods ====================================================

    /**
     * Extracts the email addresses from the given addresses.
     *
     * @param addresses The addresses from which the emails will be extracted.
     * @return The extracted email addresses.
     */
    protected static String[] extractEmails(InternetAddress[] addresses) {
        String[] emails = new String[addresses.length];
        for (int i=0; i<addresses.length; i++) {
            emails[i] = addresses[i].getAddress();
        }
        return emails;
    }
}
