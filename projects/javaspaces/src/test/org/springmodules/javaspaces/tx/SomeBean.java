/**
 * 
 */
package org.springmodules.javaspaces.tx;

import net.jini.core.entry.Entry;

/**
 * @author costin
 *
 */
public class SomeBean implements Entry {
	public String name = "someBean";

	public String toString() {
		return name;
	}

}
