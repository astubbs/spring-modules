package org.springmodules.cache.key;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.collections.keyvalue.MultiKey;

/**
 * 
 * A {@link CacheKeyGenerator} based on commons collections.
 * 
 * @author David J. M. Karlsen <david@davidkarlsen.com>
 *
 */
public class MultiKeyCodeGenerator
    implements CacheKeyGenerator
{
    private final boolean cloneKeys;
    
    /**
     * @param cloneKeys set to true to clone keys to assure they stick immutable.
     */
    public MultiKeyCodeGenerator( boolean cloneKeys )
    {
        this.cloneKeys = cloneKeys;
    }
    
    /**
     * Equals <code>this(true)</code>
     */
    public MultiKeyCodeGenerator()
    {
        this( true );
    }
    
    /**
     * Generates a key based on {@linkplain MultiKey}.
     * {@inheritDoc}
     * @return a {@link MultiKey} co-variant of {@link Serializable}.
     */
    public MultiKey generateKey( MethodInvocation methodInvocation )
    {
        Object[] arguments = methodInvocation.getArguments();
        MultiKey multiKey = new MultiKey( arguments, cloneKeys );
        
        return multiKey;
    }

}
