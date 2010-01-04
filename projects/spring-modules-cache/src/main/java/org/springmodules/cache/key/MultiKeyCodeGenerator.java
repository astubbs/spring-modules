package org.springmodules.cache.key;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.collections.keyvalue.MultiKey;

public class MultiKeyCodeGenerator
    implements CacheKeyGenerator
{
    private final boolean cloneKeys;
    
    public MultiKeyCodeGenerator( boolean cloneKeys )
    {
        this.cloneKeys = cloneKeys;
    }
    
    public MultiKeyCodeGenerator()
    {
        this( true );
    }
    
    public Serializable generateKey( MethodInvocation methodInvocation )
    {
        Object[] arguments = methodInvocation.getArguments();
        MultiKey multiKey = new MultiKey( arguments, cloneKeys );
        
        return multiKey;
    }

}
