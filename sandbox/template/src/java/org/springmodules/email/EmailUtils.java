package org.springmodules.email;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;

/**
 * @author Uri Boness
 */
public class EmailUtils {

    public static InternetAddress createAddress(String address) {
        try {
            return new InternetAddress(address);
        } catch (AddressException ae) {
            throw new IllegalArgumentException("Could not create address '" + address + "'", ae);
        }
    }

    public static InternetAddress createAddress(String name, String address) {
        try {
            return new InternetAddress(address, name);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException(uee); //todo figure out what runtime exception to throw.
        }
    }

    public static InternetAddress createAddress(String name, String address, String encoding) {
        try {
            return new InternetAddress(address, name, encoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException(uee); //todo figure out what runtime exception to throw.
        }
    }

}
