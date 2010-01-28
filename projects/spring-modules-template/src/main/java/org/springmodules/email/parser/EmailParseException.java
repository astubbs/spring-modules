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

package org.springmodules.email.parser;

/**
 * Thrown by {@link org.springmodules.email.EmailParser#parse(org.springframework.core.io.Resource)} when the parsing process fails.
 *
 * @author Uri Boness
 */
public class EmailParseException extends RuntimeException {

    /**
     * Constructs a new EmailParseException with a given error message.
     *
     * @param message The error message.
     */
    public EmailParseException(String message) {
        super(message);
    }

    /**
     * Constructs a new EmailParseException with given error message and the original cause exception.
     *
     * @param message The error message.
     * @param cause The cause for this exception.
     */
    public EmailParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
