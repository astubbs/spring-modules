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

package org.springmodules.email.conf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.internet.InternetAddress;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.StringUtils;
import org.springmodules.email.Attachment;
import org.springmodules.email.Email;
import org.springmodules.email.EmailUtils;
import org.springmodules.email.EmailPriority;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A SAX based implementation of {@link EmailParser} where the parsed XML defines an email.
 *
 * @author Uri Boness
 */
public class SaxEmailParser implements EmailParser, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    /**
     * Constructs a new SaxEmailParser where the {@link DefaultResourceLoader} is used to load the attchment
     * resources.
     */
    public SaxEmailParser() {
        this(new DefaultResourceLoader());
    }

    /**
     * Constructs a new SaxEmailParser with a given {@link ResourceLoader} that is used to load the attchment
     * resources.
     *
     * @param resourceLoader The resource loader that is used to load the attachment resources.
     */
    public SaxEmailParser(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Parses the given XML resource using SAX parser and returning the parsed {@link Email}.
     *
     * @param resource The XML resource to parse.
     * @return The parsed emaill
     * @see EmailParser#parse(Resource)
     */
    public Email parse(Resource resource) {
        EmailContentHandler handler = new EmailContentHandler();
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(resource.getInputStream(), handler);
        } catch (ParserConfigurationException pce) {
            throw new EmailConfigurationException("Could not parse email resource '" + resource.getDescription() + "'", pce);
        } catch (SAXException se) {
            throw new EmailConfigurationException("Could not parse email resource '" + resource.getDescription() + "'", se);
        } catch (IOException ioe) {
            throw new EmailConfigurationException("Could not parse email resource '" + resource.getDescription() + "'", ioe);
        }
        return handler.getEmail();
    }

    /**
     * Sets the resource loader that is used to load the attachment resources.
     *
     * @param resourceLoader The resource loader that is used to load the attachment resources.
     * @see ResourceLoaderAware
     */
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * A SAX {@link java.net.ContentHandler} that builds an {@link Email} object along the parsing process.
     */
    protected class EmailContentHandler extends DefaultHandler {

        private Email email = new Email();
        private List list = new ArrayList();
        private Set set = new HashSet();
        private String name;
        private StringBuffer buffer = new StringBuffer();


        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            String elementName = qName.toLowerCase();
            if ("from".equals(elementName) || "reply-to".equals(elementName) || "address".equals(elementName)) {
                name = attributes.getValue("name");
                name = (StringUtils.hasText(name)) ? name : null;
            }
            if ("attachment".equals(elementName)) {
                name = attributes.getValue("name");
            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            String elementName = qName.toLowerCase();
            if ("from".equals(elementName)) {
                if (name != null) {
                    email.setFrom(name, buffer.toString().trim());
                } else {
                    email.setFrom(buffer.toString().trim());
                }
                buffer = new StringBuffer();
            }
            if ("reply-to".equals(elementName)) {
                if (name != null) {
                    email.setReplyTo(name, buffer.toString().trim());
                } else {
                    email.setFrom(buffer.toString().trim());
                }
                buffer = new StringBuffer();
            }
            if ("address".equals(elementName)) {
                if (name != null) {
                    list.add(EmailUtils.createAddress(name, buffer.toString().trim()));
                } else {
                    list.add(EmailUtils.createAddress(buffer.toString().trim()));
                }
                buffer = new StringBuffer();
            }
            if ("to".equals(elementName)) {
                email.setTo((InternetAddress[])list.toArray(new InternetAddress[list.size()]));
                list = new ArrayList();
            }
            if ("cc".equals(elementName)) {
                email.setCc((InternetAddress[])list.toArray(new InternetAddress[list.size()]));
                list = new ArrayList();
            }
            if ("bcc".equals(elementName)) {
                email.setBcc((InternetAddress[])list.toArray(new InternetAddress[list.size()]));
                list = new ArrayList();
            }
            if ("attachments".equals(elementName)) {
                email.setAttachments(set);
                set = new HashSet();
            }
            if ("inline-attachments".equals(elementName)) {
                email.setInlineAttachments(set);
                set = new HashSet();
            }
            if ("attachment".equals(elementName)) {
                set.add(new Attachment(name, resourceLoader.getResource(buffer.toString().trim())));
                buffer = new StringBuffer();
            }
            if ("priority".equals(elementName)) {
                email.setPriority(EmailPriority.name(buffer.toString().trim()));
                buffer = new StringBuffer();
            }
            if ("subject".equals(elementName)) {
                email.setSubject(buffer.toString().trim());
                buffer = new StringBuffer();
            }
            if ("text-body".equals(elementName)) {
                email.setTextBody(buffer.toString().trim());
                buffer = new StringBuffer();
            }
            if ("html-body".equals(elementName)) {
                email.setHtmlBody(buffer.toString().trim());
                buffer = new StringBuffer();
            }
        }

        public void characters(char ch[], int start, int length) throws SAXException {
            buffer.append(ch, start,  length);
        }

        public Email getEmail() {
            return email;
        }

    }
}
