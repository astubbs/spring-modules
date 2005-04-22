package org.springmodules.cache.integration;
import java.util.List;
public class CacheableImpl$__attributeRepository implements org.apache.commons.attributes.AttributeRepositoryClass {
    private final java.util.Set classAttributes = new java.util.HashSet ();
    private final java.util.Map fieldAttributes = new java.util.HashMap ();
    private final java.util.Map methodAttributes = new java.util.HashMap ();
    private final java.util.Map constructorAttributes = new java.util.HashMap ();

    public CacheableImpl$__attributeRepository () {
        initClassAttributes ();
        initMethodAttributes ();
        initFieldAttributes ();
        initConstructorAttributes ();
    }

    public java.util.Set getClassAttributes () { return classAttributes; }
    public java.util.Map getFieldAttributes () { return fieldAttributes; }
    public java.util.Map getConstructorAttributes () { return constructorAttributes; }
    public java.util.Map getMethodAttributes () { return methodAttributes; }

    private void initClassAttributes () {
    }

    private void initFieldAttributes () {
        java.util.Set attrs = null;
        attrs = new java.util.HashSet ();
        fieldAttributes.put ("serialVersionUID", attrs);
        attrs = null;

    }
    private void initMethodAttributes () {
        java.util.Set attrs = null;
        java.util.List bundle = null;
        bundle = new java.util.ArrayList ();
        attrs = new java.util.HashSet ();
        {
            org.springmodules.cache.interceptor.caching.Cached _attr = new org.springmodules.cache.interceptor.caching.Cached(  // C:/wanghy/springmodules/sandbox/test/org/springmodules/cache/integration/CacheableImpl.java:66
);
            _attr.setCacheProfileId(
"test"  // C:/wanghy/springmodules/sandbox/test/org/springmodules/cache/integration/CacheableImpl.java:66
);
        Object _oattr = _attr; // Need to erase type information
        if (_oattr instanceof org.apache.commons.attributes.Sealable) {
            ((org.apache.commons.attributes.Sealable) _oattr).seal ();
        }
        attrs.add ( _attr );
        }
        bundle.add (attrs);
        attrs = null;
        attrs = new java.util.HashSet ();
        bundle.add (attrs);
        attrs = null;
        attrs = new java.util.HashSet ();
        bundle.add (attrs);
        attrs = null;
        methodAttributes.put ("getName(int)", bundle);
        bundle = null;

        bundle = new java.util.ArrayList ();
        attrs = new java.util.HashSet ();
        bundle.add (attrs);
        attrs = null;
        attrs = new java.util.HashSet ();
        bundle.add (attrs);
        attrs = null;
        attrs = new java.util.HashSet ();
        bundle.add (attrs);
        attrs = null;
        methodAttributes.put ("setNames(java.util.List)", bundle);
        bundle = null;

        bundle = new java.util.ArrayList ();
        attrs = new java.util.HashSet ();
        {
            org.springmodules.cache.interceptor.flush.FlushCache _attr = new org.springmodules.cache.interceptor.flush.FlushCache(  // C:/wanghy/springmodules/sandbox/test/org/springmodules/cache/integration/CacheableImpl.java:84
);
            _attr.setCacheProfileIds(
"test"  // C:/wanghy/springmodules/sandbox/test/org/springmodules/cache/integration/CacheableImpl.java:84
);
        Object _oattr = _attr; // Need to erase type information
        if (_oattr instanceof org.apache.commons.attributes.Sealable) {
            ((org.apache.commons.attributes.Sealable) _oattr).seal ();
        }
        attrs.add ( _attr );
        }
        bundle.add (attrs);
        attrs = null;
        attrs = new java.util.HashSet ();
        bundle.add (attrs);
        attrs = null;
        attrs = new java.util.HashSet ();
        bundle.add (attrs);
        attrs = null;
        attrs = new java.util.HashSet ();
        bundle.add (attrs);
        attrs = null;
        methodAttributes.put ("updateName(int,java.lang.String)", bundle);
        bundle = null;

    }
    private void initConstructorAttributes () {
        java.util.Set attrs = null;
        java.util.List bundle = null;
    }
}
