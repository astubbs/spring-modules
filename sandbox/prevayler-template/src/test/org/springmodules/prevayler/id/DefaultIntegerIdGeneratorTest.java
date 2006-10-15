package org.springmodules.prevayler.id;

import junit.framework.TestCase;

/**
 * @author Sergio Bossa
 */
public class DefaultIntegerIdGeneratorTest extends TestCase {
    
    private DefaultIntegerIdGenerator generator;
    
    public DefaultIntegerIdGeneratorTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        this.generator = new DefaultIntegerIdGenerator();
    }

    public void testGenerateId() {
        for (int i = 1; i <=100; i++) {
            assertEquals(new Integer(i), this.generator.generateId());
        }
    }
}
