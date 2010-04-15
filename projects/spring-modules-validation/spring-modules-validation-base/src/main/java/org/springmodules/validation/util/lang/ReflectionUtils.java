package org.springmodules.validation.util.lang;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * @author Uri Boness
 */
public class ReflectionUtils {

    /**
     * Attempts to find a {@link Method} on the supplied type with the supplied name and
     * parameter types. Searches all superclasses up to <code>Object</code>. Returns
     * '<code>null</code>' if no {@link Method} can be found. supports all visibiliy levels
     * (public, private, protected and default).
     */
    public static Method findMethod(Class type, String name, Class[] paramTypes) {
        Assert.notNull(type, "'type' cannot be null.");
        Assert.notNull(name, "'name' cannot be null.");
        Class searchType = type;
        while (!Object.class.equals(searchType) && searchType != null) {
            Method[] methods = (type.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                if (name.equals(method.getName()) && Arrays.equals(paramTypes, method.getParameterTypes())) {
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    /**
     * Attempts to find a {@link Method} on the supplied type assuming it accepts no arguments.
     * Searches all superclasses up to <code>Object</code>. Returns '<code>null</code>' if no
     * {@link Method} can be found. supports all visibiliy levels (public, private, protected and default).
     */
    public static Method findMethod(Class type, String name) {
        return findMethod(type, name, new Class[0]);
    }

    /**
     * Returns a list of all methods (of all access level - public, protected, private, default) in the
     * hierarchy of the given class.
     *
     * @param type The given class
     */
    public static Method[] getAllMethods(Class type) {
        Assert.notNull(type, "'type' cannot be null.");
        Map methodBySigniture = new HashMap();
        Class searchType = type;
        while (!Object.class.equals(searchType) && searchType != null) {
            Method[] typeMethods = (type.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
            for (int i = 0; i < typeMethods.length; i++) {
                Method method = typeMethods[i];
                String sig = getSignature(method);
                if (!methodBySigniture.containsKey(sig)) {
                    methodBySigniture.put(sig, method);
                }
            }
            searchType = searchType.getSuperclass();
        }
        return (Method[]) methodBySigniture.values().toArray(new Method[methodBySigniture.size()]);
    }

    public static String getSignature(Method method) {
        String name = method.getName();
        Class returnType = method.getReturnType();
        Class[] paramTypes = method.getParameterTypes();
        StringBuffer buffer = new StringBuffer();
        buffer.append(name).append("(");
        for (int i = 0; i < paramTypes.length; i++) {
            if (i != 0) {
                buffer.append(",");
            }
            buffer.append(paramTypes[i].getName());
        }
        buffer.append(")").append(returnType.getName());
        return buffer.toString();
    }

}
