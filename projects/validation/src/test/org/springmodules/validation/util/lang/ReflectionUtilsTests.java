package org.springmodules.validation.util.lang;

import java.lang.reflect.Method;

import junit.framework.TestCase;

/**
 * Tests for {@link org.springmodules.validation.util.lang.ReflectionUtils}.
 *
 * @author Uri Boness
 */
public class ReflectionUtilsTests extends TestCase {

    public void testGetAllMethods() throws Exception {
        Method[] methods = ReflectionUtils.getAllMethods(Employee.class);
        assertEquals(3, methods.length);
    }
}