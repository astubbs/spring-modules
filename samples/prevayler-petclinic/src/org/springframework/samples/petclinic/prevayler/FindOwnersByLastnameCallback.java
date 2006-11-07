package org.springframework.samples.petclinic.prevayler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.springframework.samples.petclinic.Owner;
import org.springmodules.prevayler.callback.PrevaylerCallback;
import org.springmodules.prevayler.system.PrevalentSystem;

/**
 *
 * @author Sergio Bossa
 */
public class FindOwnersByLastnameCallback implements PrevaylerCallback {
    
    private String name;
    
    public FindOwnersByLastnameCallback(String name) {
        this.name = name;
    }

    public Object doInTransaction(PrevalentSystem system) {
        Iterator ownersIt = system.get(Owner.class).iterator();
        List result = new LinkedList();
        while (ownersIt.hasNext()) {
            Owner tmp = (Owner) ownersIt.next();
            if (tmp.getLastName().startsWith(this.name)) result.add(tmp);
        }
        return result;
    }
}
