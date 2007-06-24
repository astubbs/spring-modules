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

import org.springframework.core.io.Resource;

/**
 * Represents an attachment for an email. This attachment holds a name (which serves as the file name for normal
 * attachments and id for inline attachments) and a resource.
 *
 * @author Uri Boness
 */
public class Attachment {

    private String name;

    private Resource resource;

    /**
     * Constructs a new attachment with no name and resource.
     */
    public Attachment() {
        this(null, null);
    }

    /**
     * Constructs a new attachment with a given name and a given resource.
     *
     * @param name The name of the attachment.
     * @param resource The resource that represents the attachment.
     */
    public Attachment(String name, Resource resource) {
        this.name = name;
        this.resource = resource;
    }


    //============================================== Setter/Getter =====================================================

    /**
     * Return the name of the attachment.
     *
     * @return The name of the attachment.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the attachment.
     *
     * @param name The name of the attachment.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the resource of the attachment.
     *
     * @return The resource of the attachment.
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Sets the resource of the attachment.
     *
     * @param resource The resource of the attachment.
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
