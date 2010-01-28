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
 * An error handler that doesn't really handles the errors but instead it rethrows the errors to be handled
 * higher in the call stack.
 *
 * @author Uri Boness
 */
public class RethrowingDispatchingErrorHandler implements DispatchingErrorHandler {

    /**
     * Rethrows the given error.
     *
     * @see DispatchingErrorHandler#handle(MailException, Email)
     */
    public void handle(MailException error, Email email) {
        throw error;
    }
}
