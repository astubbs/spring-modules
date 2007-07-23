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

import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.FutureTask;
import junit.framework.TestCase;
import org.springmodules.xt.model.event.support.PayloadEvent;

/**
 * @author Sergio Bossa
 */
public class ThreadLocalCollectorTest extends TestCase {
    
    private ThreadLocalCollector collector;
    private CyclicBarrier barrier;
    
    public ThreadLocalCollectorTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        this.collector = new ThreadLocalCollector();
        this.barrier = new CyclicBarrier(2);
    }
    
    public void testCollectFIFOOrder() throws Exception {
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

    public void testCollectFromDifferentThreads() throws Exception {
        FutureTask f1 = new FutureTask(new Tester());
        FutureTask f2 = new FutureTask(new Tester());
        Thread t1 = new Thread(f1);
        Thread t2 = new Thread(f2);
        
        t1.start();
        t2.start();
        
        try {    
            f1.get();
            f2.get();
        } catch (Exception ex) {
            ex.printStackTrace();
            this.fail("Test failure: " + ex.getMessage());
        }
    }

    private class Tester implements Callable {
        public Object call() throws Exception {
            ThreadLocalCollectorTest.this.barrier.await();
            for (int i = 0; i < 100; i++) {
                ThreadLocalCollectorTest.this.collector.onApplicationEvent(new PayloadEvent(this, new String("test-" + i), null));
                assertEquals(i + 1, ThreadLocalCollectorTest.this.collector.getEvents().size());
            }
            return null;
        }
    }
}
