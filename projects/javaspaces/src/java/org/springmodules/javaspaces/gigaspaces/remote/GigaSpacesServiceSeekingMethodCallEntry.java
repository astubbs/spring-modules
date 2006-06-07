/*
 * Copyright 2006 GigaSpaces Technologies. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED INCLUDING BUT NOT LIMITED TO WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT. GIGASPACES WILL NOT BE LIABLE FOR ANY DAMAGE OR
 * LOSS IN CONNECTION WITH THE SOFTWARE.
 */

package org.springmodules.javaspaces.gigaspaces.remote;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.springmodules.javaspaces.entry.ServiceSeekingMethodCallEntry;

/**
 * <p>
 * Description: This class extends ServiceSeekingMethodCallEntry
 * representing a method call that hits a remote service.
 * That is, the endpoint is expected to host the service; only the
 * method and arguments will be shipped, and not the implementing code.
 * </p>
 * Copyright:    Copyright 2006 GigaSpaces Technologies. All rights reserved.
 * Company:      Gigaspaces Technologies
 * @author       Lior Ben Yizhak
 * @version      5.0
 */
public class GigaSpacesServiceSeekingMethodCallEntry extends ServiceSeekingMethodCallEntry
{
	/**
	 * Constructor
	 * @param method the method
	 * @param args the method arguments
	 * @param uid the uid
	 */
	public GigaSpacesServiceSeekingMethodCallEntry(Method method, Object[] args, Serializable uid) {
		super(method, args);
		this.uid = uid;
	}

	/**
	 * Constructor
	 * @param method the method
	 * @param args the method arguments
	 */
	public GigaSpacesServiceSeekingMethodCallEntry(Method method, Object[] args) {
		super(method, args);
	}

	/**
	 * Empty Constructor
	 */
	public GigaSpacesServiceSeekingMethodCallEntry() {
		super();
	}

	/**
	 * Make an index for hash-based load balancing.
	 * @return the cache id
	 */
	public static String[] __getSpaceIndexedFields() {
        String[] indexedFields = {GigaSpacesRunnableMethodCallEntry.UID};
        return indexedFields;
    }
}
