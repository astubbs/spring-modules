/*
 * The Spring Framework is published under the terms
 * of the Apache Software License.
 */

package org.springmodules.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Utilities for testing serializability of objects.
 * Exposes static methods for use in other test cases.
 * Extends TestCase only to test itself.
 *
 * @author Rod Johnson
 */
public class SerializationTestUtils {
	
	public static void testSerialization(Object o) throws IOException {
		OutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
	}
	
	public static boolean isSerializable(Object o) throws IOException {
		try {
			testSerialization(o);
			return true;
		}
		catch (NotSerializableException ex) {
			return false;
		}
	}
	
	public static Object serializeAndDeserialize(Object o) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.flush();
		baos.flush();
		byte[] bytes = baos.toByteArray();
		
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(is);
		Object o2 = ois.readObject();
		
		return o2;
	}
}
