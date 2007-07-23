/*
 * Copyright 2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.xt.model.event.collector;

import junit.framework.TestCase;
import org.springmodules.xt.model.event.support.PayloadEvent;

/**
 * @author Sergio Bossa
 */
public class SimpleCollectorTest extends TestCase {
    
    private SimpleCollector collector;
    
    public SimpleCollectorTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        this.collector = new SimpleCollector();
    }
    
    public void testCollectAndFIFOOrder() throws Exception {
        PayloadEvent e1 = new PayloadEvent(this, new String("test-1"), null);
        PayloadEvent e2 = new PayloadEvent(this, new String("test-2"), null);
        PayloadEvent e3 = new PayloadEvent(this, new String("test-3"), null);
        
        this.collector.onApplicationEvent(e1);
        this.collector.onApplicationEvent(e2);
        this.collector.onApplicationEvent(e3);
        
        assertSame(e1, this.collector.getEvents().get(0));
        assertSame(e2, this.collector.getEvents().get(1));
        assertSame(e3, this.collector.getEvents().get(2));
        
        assertSame(e1, this.collector.pollEvent());
        assertSame(e2, this.collector.pollEvent());
        assertSame(e3, this.collector.pollEvent());
        
        assertEquals(0, this.collector.getEvents().size());
    }
}
