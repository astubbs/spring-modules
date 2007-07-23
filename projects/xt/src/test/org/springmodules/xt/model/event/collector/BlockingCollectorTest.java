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
import java.util.concurrent.Exchanger;
import java.util.concurrent.FutureTask;
import junit.framework.TestCase;
import org.springframework.context.ApplicationEvent;
import org.springmodules.xt.model.event.support.PayloadEvent;

/**
 * @author Sergio Bossa
 */
public class BlockingCollectorTest extends TestCase {
    
    private BlockingCollector collector;
    private Exchanger<ApplicationEvent> exchanger;
    
    public BlockingCollectorTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        this.collector = new BlockingCollector();
        this.exchanger = new Exchanger<ApplicationEvent>();
    }
    
    public void testCollectFIFOOrder() throws Exception {
        PayloadEvent e1 = new PayloadEvent(this, new String("test"), null);
        PayloadEvent e2 = new PayloadEvent(this);
        PayloadEvent e3 = new PayloadEvent(this);
        
        this.collector.onApplicationEvent(e1);
        this.collector.onApplicationEvent(e2);
        this.collector.onApplicationEvent(e3);
        
        assertSame(e1, this.collector.pollEvent());
        assertSame(e2, this.collector.pollEvent());
        assertSame(e3, this.collector.pollEvent());
    }
    
    public void testCollectAndPollFromDifferentThreads() throws Exception {
        FutureTask f1 = new FutureTask(new Producer());
        FutureTask f2 = new FutureTask(new Consumer());
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
    
    private class Producer implements Callable {
        public Object call() throws Exception {
            for (int i = 0; i < 5; i++) {
                ApplicationEvent produced = new PayloadEvent(this, new String("test-" + i), null);
                BlockingCollectorTest.this.collector.onApplicationEvent(produced);
                ApplicationEvent polled = BlockingCollectorTest.this.exchanger.exchange(produced);
                BlockingCollectorTest.this.assertSame(produced, polled);
            }
            return null;
        }
    }
    
    private class Consumer implements Callable {
        public Object call() throws Exception {
            for (int i = 0; i < 5; i++) {
                ApplicationEvent polled = BlockingCollectorTest.this.collector.pollEvent();
                ApplicationEvent produced = BlockingCollectorTest.this.exchanger.exchange(polled);
                BlockingCollectorTest.this.assertSame(polled, produced);
            }
            return null;
        }
    }
}
