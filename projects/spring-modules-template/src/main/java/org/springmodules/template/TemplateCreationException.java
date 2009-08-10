package org.springmodules.template;

/**
 * Thrown during output generation.
 *
 * @author Uri Boness
 */
public class TemplateCreationException extends TemplateException {

    /**
     * Constructs a new TemplateException with a message.
     *
     * @param message The message that describes this exception
     */
    public TemplateCreationException(String message) {
        super(message);
    }

    /**
     * Constructs a new TemplateException with a message and a cause.
     *
     * @param message The message that describes this exception.
     * @param cause   The cause for this exception.
     */
    public TemplateCreationException(String message, Throwable cause) {
        super(message, cause);
    }

}
