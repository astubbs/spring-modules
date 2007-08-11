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

import org.springmodules.email.EmailPreparator;
import org.springmodules.email.Email;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

/**
 * An email preparator that adds an inline attachment to the email.
 *
 * @author Uri Boness
 */
public class InlineAttachmentEmailPreparator implements EmailPreparator {

    private String id;
    private Resource resource;

    /**
     * Creates a new InlineAttachmentEmailPreparator that will add the given input stream as an inline attachment to the
     * prepared email. The attachement will be associated with the given id.
     *
     * @param id The id that is associated with the attachment.
     * @param input The input stream from which the attachemnt will be read.
     */
    public InlineAttachmentEmailPreparator(String id, InputStream input) {
        this(id, new InputStreamResource(input));
    }

    /**
     * Creates a new InlineAttachmentEmailPreparator that will add the given resource as an inline attachment to the
     * prepared email. The attachement will be associated with the given id.
     *
     * @param id The id that is associated with the attachment.
     * @param resource The resource that will be attached to the email.
     */
    public InlineAttachmentEmailPreparator(String id, Resource resource) {
        this.id = id;
        this.resource = resource;
    }

    /**
     * Attaches the configured resource/input stream as an inline attachment to the email.
     *
     * @param email The prepared email.
     * @return The prepared email with the attached resource/input stream
     */
    public Email prepare(Email email) {
        email.addInlineAttachment(id, resource);
        return email;
    }

}
