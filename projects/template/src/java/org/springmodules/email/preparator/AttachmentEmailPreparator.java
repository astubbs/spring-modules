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

import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springmodules.email.Email;
import org.springmodules.email.EmailPreparator;

/**
 * An email preparator that adds adds an attachment to the email.
 *
 * @author Uri Boness
 */
public class AttachmentEmailPreparator implements EmailPreparator {

    private String fileName;
    private Resource resource;

    /**
     * Creates a new AttachmentEmailPreparator that will add the given input stream as an attachment to the prepared
     * email. The attachement will be associated with the given file name.
     *
     * @param fileName The file name that is associated with the attachment.
     * @param input The input stream from which the attachemnt will be read.
     */
    public AttachmentEmailPreparator(String fileName, InputStream input) {
        this(fileName, new InputStreamResource(input));
    }

    /**
     * Adds the given resource as an attachment to the prepared email. The attachment will be associated with the given
     * file name.
     *
     * @param fileName The file name that is associated with the attachment.
     * @param resource The resource that will be attached to the email.
     */
    public AttachmentEmailPreparator(String fileName, Resource resource) {
        this.fileName = fileName;
        this.resource = resource;
    }

    /**
     * Attaches the configured resource/input stream to the email.
     *
     * @param email The prepared email.
     * @return The prepared email with the attached resource/input stream
     */
    public Email prepare(Email email) {
        email.addAttachment(fileName, resource);
        return email;
    }

}
