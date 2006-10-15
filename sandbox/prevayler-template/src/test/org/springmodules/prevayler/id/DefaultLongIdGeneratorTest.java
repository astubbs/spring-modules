package org.springmodules.prevayler.id;

import junit.framework.TestCase;

/**
 * @author Sergio Bossa
 */
public class DefaultLongIdGeneratorTest extends TestCase {
    
    private DefaultLongIdGenerator generator;
    
    public DefaultLongIdGeneratorTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        this.generator = new DefaultLongIdGenerator();
    }

    public void testGenerateId() {
        for (long i = 1; i <=100; i++) {
            assertEquals(new Long(i), this.generator.generateId());
        }
    }
}
