package org.springmodules.prevayler.system.callback;

import org.springmodules.prevayler.system.PrevalentSystem;

/**
 *
 * @author Sergio Bossa
 */
public class MergeCallback implements SystemCallback {
    
    private static final long serialVersionUID = 478805268506333703L;
    
    private Object source;
    private Object destination;
    
    public MergeCallback(Object source, Object destination) {
        this.source = source;
        this.destination = destination;
    }
    
    public Object execute(PrevalentSystem system) {
        system.merge(this.source, this.destination);
        return null;
    }
}
