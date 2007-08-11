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

package org.springmodules.email.preparator;

import java.util.Set;
import java.util.Map;

import org.springmodules.email.Email;
import org.springmodules.email.EmailPriority;
import org.springmodules.email.Attachment;
import org.springframework.core.io.Resource;
import javax.mail.internet.InternetAddress;

/**
 * Represents an identifiable email. Serves as a wrapper for an none identifiable email.
 *
 * @author Uri Boness
 */
public class IdentifiableEmail extends Email {

    private final String id;
    private final Email email;

    /**
     * Constructs a new IdentifiableEmail with a given id and the actual email.
     *
     * @param id The id of the email.
     * @param email The email.
     */
    public IdentifiableEmail(String id, Email email) {
        this.id = id;
        this.email = email;
    }

    /**
     * Returns the id of the email.
     *
     * @return The id of the email.
     */
    public String getId() {
        return id;
    }

    public void setFrom(InternetAddress from) {
        email.setFrom(from);
    }

    public void setFrom(String address) {
        email.setFrom(address);
    }

    public void setFrom(String name, String address) {
        email.setFrom(name, address);
    }

    public InternetAddress getFrom() {
        return email.getFrom();
    }

    public void setTo(InternetAddress[] to) {
        email.setTo(to);
    }

    public void setTo(InternetAddress to) {
        email.setTo(to);
    }

    public void addTo(InternetAddress address) {
        email.addTo(address);
    }

    public void addTo(InternetAddress[] addresses) {
        email.addTo(addresses);
    }

    public void setTo(String address) {
        email.setTo(address);
    }

    public void addTo(String address) {
        email.addTo(address);
    }

    public void addTo(String[] addresses) {
        email.addTo(addresses);
    }

    public InternetAddress[] getTo() {
        return email.getTo();
    }

    public void setCc(InternetAddress[] cc) {
        email.setCc(cc);
    }

    public void setCc(InternetAddress cc) {
        email.setCc(cc);
    }

    public void setCc(String cc) {
        email.setCc(cc);
    }

    public void addCc(InternetAddress address) {
        email.addCc(address);
    }

    public void addCc(InternetAddress[] addresses) {
        email.addCc(addresses);
    }

    public void addCc(String address) {
        email.addCc(address);
    }

    public void addCc(String[] addresses) {
        email.addCc(addresses);
    }

    public InternetAddress[] getCc() {
        return email.getCc();
    }

    public void setBcc(InternetAddress[] bcc) {
        email.setBcc(bcc);
    }

    public void setBcc(InternetAddress bcc) {
        email.setBcc(bcc);
    }

    public void setBcc(String bcc) {
        email.setBcc(bcc);
    }

    public void addBcc(InternetAddress address) {
        email.addBcc(address);
    }

    public void addBcc(InternetAddress[] addresses) {
        email.addBcc(addresses);
    }

    public void addBcc(String address) {
        email.addBcc(address);
    }

    public void addBcc(String[] addresses) {
        email.addBcc(addresses);
    }

    public InternetAddress[] getBcc() {
        return email.getBcc();
    }

    public void setReplyTo(InternetAddress replyTo) {
        email.setReplyTo(replyTo);
    }

    public void setReplyTo(String replyTo) {
        email.setReplyTo(replyTo);
    }

    public void setReplyTo(String name, String address) {
        email.setReplyTo(name, address);
    }

    public InternetAddress getReplyTo() {
        return email.getReplyTo();
    }

    public void setPriority(EmailPriority priority) {
        email.setPriority(priority);
    }

    public EmailPriority getPriority() {
        return email.getPriority();
    }

    public void setSubject(String subject) {
        email.setSubject(subject);
    }

    public String getSubject() {
        return email.getSubject();
    }

    public void setTextBody(String textBody) {
        email.setTextBody(textBody);
    }

    public String getTextBody() {
        return email.getTextBody();
    }

    public void setHtmlBody(String htmlBody) {
        email.setHtmlBody(htmlBody);
    }

    public String getHtmlBody() {
        return email.getHtmlBody();
    }

    public void setAttachments(Set attachments) {
        email.setAttachments(attachments);
    }

    public void addAttachment(Attachment attachment) {
        email.addAttachment(attachment);
    }

    public void addAttachment(String fileName, Resource resource) {
        email.addAttachment(fileName, resource);
    }

    public Set getAttachments() {
        return email.getAttachments();
    }

    public void setInlineAttachments(Set inlineAttachments) {
        email.setInlineAttachments(inlineAttachments);
    }

    public void addInlineAttachment(Attachment attachment) {
        email.addInlineAttachment(attachment);
    }

    public void addInlineAttachment(String contentId, Resource resource) {
        email.addInlineAttachment(contentId, resource);
    }

    public Set getInlineAttachments() {
        return email.getInlineAttachments();
    }

    public Map getHeaders() {
        return email.getHeaders();
    }

    public void setHeaders(Map headers) {
        email.setHeaders(headers);
    }

    public void setHeader(String name, String value) {
        email.setHeader(name, value);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdentifiableEmail that = (IdentifiableEmail) o;

        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
