package org.springmodules.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.AbstractResource;

/**
 * A {@link org.springframework.core.io.Resource resource} that wraps a static string.
 *
 * @author Uri Boness
 */
public class StringResource extends AbstractResource {

    /**
     * Default encoding is set to UTF-8
     */
    public final static String DEFAULT_ENCODING = "UTF-8";

    private String text;

    private String encoding;

    public StringResource(String text) {
        this(text, DEFAULT_ENCODING);
    }

    public StringResource(String text, String encoding) {
        this.text = text;
        this.encoding = encoding;
    }

    public boolean exists() {
        return true;
    }

    public String getFilename() throws IllegalStateException {
        return "";
    }

    public String getDescription() {
        return text;
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(text.getBytes(encoding));
    }

}
