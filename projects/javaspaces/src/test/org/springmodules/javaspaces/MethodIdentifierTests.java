package org.springmodules.javaspaces;

import org.springmodules.javaspaces.MethodIdentifier;
import org.springmodules.javaspaces.entry.AbstractMethodCallEntry;
import org.springmodules.util.SerializationTestUtils;

import junit.framework.TestCase;

public class MethodIdentifierTests extends TestCase {

	public void testSerializeMethodIdentifier() throws Exception {
		MethodIdentifier mi = new MethodIdentifier(Object.class.getMethod("hashCode", null));
		MethodIdentifier mi2 = (MethodIdentifier) SerializationTestUtils.serializeAndDeserialize(mi);
		assertEquals(mi.getMethod(), mi2.getMethod());
	}
	
	public void testSerializeMethodCallEntry() throws Exception {
		AbstractMethodCallEntry mi = new AbstractMethodCallEntry(Object.class.getMethod("hashCode", null), null);
		AbstractMethodCallEntry mi2 = (AbstractMethodCallEntry) SerializationTestUtils.serializeAndDeserialize(mi);
		assertEquals(mi.getMethod(), mi2.getMethod());
	}
}
