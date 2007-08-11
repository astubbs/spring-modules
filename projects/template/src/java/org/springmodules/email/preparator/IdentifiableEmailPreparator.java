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

import org.springmodules.email.Email;
import org.springmodules.email.EmailPreparator;

/**
 * An email preparator that creats a new identifiable email from the given email based on a configurable id.
 *
 * @author Uri Boness
 */
public class IdentifiableEmailPreparator implements EmailPreparator {

    private final String id;

    /**
     * The id to be associated with the created identifiable email.
     *
     * @param id The id of the email
     */
    public IdentifiableEmailPreparator(String id) {
        this.id = id;
    }

    /**
     * Creates and returns a new {@link org.springmodules.email.preparator.IdentifiableEmail} from the given email
     * and the configured id.
     *
     * @param email The original email
     * @return The new identifiable email
     */
    public Email prepare(Email email) {
        return new IdentifiableEmail(id, email);
    }
}
