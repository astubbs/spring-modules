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

import java.io.UnsupportedEncodingException;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;

/**
 * Email utility methods.
 *
 * @author Uri Boness
 */
public class EmailUtils {

    /**
     * Creates a new InternetAddress from the given email address.
     *
     * @param address The email address.
     * @return The created InternetAddress.
     */
    public static InternetAddress createAddress(String address) {
        try {
            return new InternetAddress(address);
        } catch (AddressException ae) {
            throw new IllegalArgumentException("Could not create address '" + address + "'", ae);
        }
    }

    /**
     * Creates a new InternetAddress from the given recipient name and email address.
     *
     * @param name The recipient name.
     * @param address The recipient email address;
     * @return The created InternetAddress.
     */
    public static InternetAddress createAddress(String name, String address) {
        try {
            return new InternetAddress(address, name);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException(uee); //todo figure out what runtime exception to throw.
        }
    }

    /**
     * Creates a new InternetAddress from the given recipient name and email address and encoding.
     *
     * @param name The recipient name.
     * @param address The recipient email address;
     * @param encoding the given encoding
     * @return The created InternetAddress.
     */
    public static InternetAddress createAddress(String name, String address, String encoding) {
        try {
            return new InternetAddress(address, name, encoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException(uee); //todo figure out what runtime exception to throw.
        }
    }

}
