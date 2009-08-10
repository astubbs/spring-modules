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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springmodules.email.Email;
import org.springmodules.email.EmailPreparator;

/**
 * An email preparator that when ran executes a chain of internal email preparators.
 *
 * @author Uri Boness
 */
public class ChainEmailPreparator implements EmailPreparator {

    private List preparators = new ArrayList();

    /**
     * Executes a chain of email preparators and returns the prepared email.
     *
     * @param email The email to prepare.
     * @return The prepared email.
     */
    public Email prepare(Email email) {
        for (Iterator iter = preparators.iterator(); iter.hasNext();) {
            email = ((EmailPreparator)preparators).prepare(email);
        }
        return email;
    }


    //============================================== Setter/Getter =====================================================

    /**
     * Sets the internal preparators that should prepare the email. The preparators will be executed by the order in
     * which they are located in the given list.
     *
     * @param preparators The {@link org.springmodules.email.EmailPreparator email preparators}.
     */
    public void setPreparators(List preparators) {
        this.preparators = preparators;
    }

    /**
     * Returns the internal email preparators.
     *
     * @return The internal email preparators.
     */
    public List getPreparators() {
        return preparators;
    }

    /**
     * Adds the given email preparator to the end of the preparators chain.
     *
     * @param preparator The email preparator to be added.
     */
    public void addPreparator(EmailPreparator preparator) {
        this.preparators.add(preparator);
    }

    /**
     * Adds the given email preparator to the preparators chain in the specified index.
     *
     * @param preparator The preparator to be added.
     * @param index The index in which the preparator will be located in the chain.
     */
    public void addPreparator(EmailPreparator preparator, int index) {
        this.preparators.add(index, preparator);
    }

}
