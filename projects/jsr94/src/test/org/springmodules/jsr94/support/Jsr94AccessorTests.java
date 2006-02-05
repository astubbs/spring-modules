
package org.springmodules.jsr94.support;

import junit.framework.TestCase;

/**
 * @author robh
 */
public class Jsr94AccessorTests extends TestCase {

	public void testWithNullRulesSource() {
		try {
			new Jsr94Accessor(){}.afterPropertiesSet();
			fail("Should raise IllegalArgumentException");
		} catch(IllegalArgumentException ex) {
			// success
		}
	}

}
