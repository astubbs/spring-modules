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

package org.springmodules.email.dispatcher.callback;

import org.springmodules.email.Email;

/**
 * Called by {@link org.springmodules.email.dispatcher.AsyncEmailDispatcher} when after an email was dispatched.
 *
 * @author Uri Boness
 */
public interface DispatchingCallback {

    /**
     * Called by the dispacher indicating the given email was dispatch.
     *
     * @param email The dispatched email.
     * @param success Whether the email was dipatched succesfully or not.
     */
    void emailDispatched(Email email, boolean success);

}
