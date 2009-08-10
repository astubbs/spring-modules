package org.springmodules.xt.test.domain;

/**
 * Error notification.
 *
 * @author Sergio Bossa
 */
public class Error {
    
    private String code;
    private String message;
    private String propertyName;
    
    public Error(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Error(String code, String message, String propertyName) {
        this(code, message);
        this.propertyName = propertyName;
    }
    
    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getPropertyName() {
        return this.propertyName;
    }
}
