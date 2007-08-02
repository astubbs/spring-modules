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

package org.springmodules.email.dispatcher.errorhandler;

import org.springmodules.email.Email;
import org.springframework.mail.MailException;

/**
 * A strategy to handle email dispatching errors. Uses along with the {@link org.springmodules.email.dispatcher.AsyncEmailDispatcher}.
 *
 * @author Uri Boness
 */
public interface DispatchingErrorHandler {

    /**
     * Handles the given error that occured while dispatching the given email.
     *
     * @param error The error to handle.
     * @param email The email that failed to be sent.
     */
    void handle(MailException error, Email email);

}
