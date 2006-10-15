package org.springmodules.prevayler.test.domain;

import java.io.Serializable;
import java.util.Set;

/**
 * Interface for an office.
 *
 * @author Sergio Bossa
 */
public interface Office extends Serializable {

    String getName();

    String getOfficeId();
}
