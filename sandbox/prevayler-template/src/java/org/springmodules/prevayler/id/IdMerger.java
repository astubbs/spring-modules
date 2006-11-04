package org.springmodules.prevayler.id;

import java.io.Serializable;
import org.springmodules.prevayler.system.PrevalenceInfo;

/**
 * Merge the identifiers of a source object graph into a destination object graph.<br>
 * It works by traversing the whole source object graph, copying into the destination object graph the identifiers of all objects whose class is a <i>prevalent class</i> in
 * the system. Prevalent classes in the system are retrieved from the {@link org.springmodules.prevayler.system.PrevalenceInfo} object.
 *
 * @author Sergio Bossa
 */
public interface IdMerger extends Serializable {
    
    /**
     * Merge the identifiers between the two objects, from source to destination.
     * 
     * @param source The source object graph.
     * @param destination The destination object graph.
     */
    public void merge(Object source, Object destination);
    
    /**
     * Set the {@link org.springmodules.prevayler.system.PrevalenceInfo} object to use for knowing the prevalent classes
     * of the system.
     */
    public void setPrevalenceInfo(PrevalenceInfo info);
}
