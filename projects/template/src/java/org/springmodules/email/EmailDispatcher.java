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

import java.util.Map;
import java.util.Locale;

/**
 * A general interface for sending {@link Email emails}.
 *
 * @author Uri Boness
 */
public interface EmailDispatcher {

    /**
     * Sends the given email
     *
     * @param email The email to be sent
     */
    void send(Email email);

    /**
     * Sends the email identified by the given name.
     *
     * @param emailName The name of the email.
     */
    void send(String emailName);

    /**
     * Sends the email identified by the given name and locale.
     *
     * @param email The name of the email.
     * @param locale The locale of the email.
     */
    void send(String email, Locale locale);

    /**
     * Sends the email identified by the given name. This method enables dynamic email generation based on a given model.
     *
     * @param emailName The name of the email.
     * @param model The model based on which the email will be generated/resolved.
     */
    void send(String emailName, Map model);

    /**
     * Sends the email identified by the given name and locale. This method enables dynamic email generation based on
     * the given model.
     *
     * @param emailName The name of the email.
     * @param model The model by which the email will be generated.
     * @param locale The locale of the email.
     */
    void send(String emailName, Map model, Locale locale);

    /**
     * Sends the email identified by the given name. This method enables dynamic email generation based on a given model.
     * The given email preparator enables to customize and prepare the email before sending. A typical use
     * of the preparator is to attach runtime generated resources (e.g. reports, invoices, images, etc...).
     *
     * @param emailName The name of the email
     * @param model The model by which the email will be created.
     * @param emailPreparator The preparator that will prepare and customize the email before sending.
     */
    void send(String emailName, Map model, EmailPreparator emailPreparator);

    /**
     * Sends the emaild identified by the given name and locale. The method enables dynamic email generation based on the
     * given model. The given email preparator can be used to customize the email prior to its dispatch. A typical use
     * of the preparator is to attach runtime generated resources (e.g reports, invoices, images, etc...).
     *
     * @param emailName The name of the email.
     * @param model The model by which the email will be generated.
     * @param locale The locale of the email.
     * @param emailPreparator The email preparator to enable runtime email customization.
     */
    void send(String emailName, Map model, Locale locale, EmailPreparator emailPreparator);

}
