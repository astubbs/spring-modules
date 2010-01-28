package org.springmodules.cache.key;



public class MultiKeyCodeGeneratorTest
    extends AbstractCacheKeyGeneratorTests
{
    private MultiKeyCodeGenerator multiKeyCodeGenerator;
        
    
    @Override
    protected void afterSetUp()
        throws Exception
    {
        this.multiKeyCodeGenerator = new MultiKeyCodeGenerator();
    }

    @Override
    protected CacheKeyGenerator getCacheKeyGenerator()
    {
        return multiKeyCodeGenerator;
    }

}
