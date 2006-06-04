package org.springmodules.javaspaces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springmodules.beans.ITestBean;
import org.springmodules.javaspaces.DelegatingWorker;
import org.springmodules.javaspaces.JavaSpaceTemplate;
import org.springmodules.location.LocationInterceptor;

public class FactoryTests extends AbstractDependencyInjectionSpringContextTests {
	private static final Log log = LogFactory.getLog(FactoryTests.class);
    
    private JavaSpaceTemplate template;
    
    private Thread testBeanWorkerThread;
    
    private Thread orderedWorkerThread;
    DelegatingWorker worker, worker2;
    
    protected String[] getConfigLocations() {
        return new String[] {"org/springmodules/javaspaces/space-context.xml" };
    }
    
    public void setJavaSpaceTemplate(JavaSpaceTemplate template) {
        this.template = template;
    }
    
    protected void onSetUp() throws Exception {
        createWorkerThreads();
    }
    
    protected void onTearDown() throws Exception {
        killWorkerThreads();
        
        LocationInterceptor li = (LocationInterceptor) applicationContext.getBean("locationInterceptor");
        log.info("Local invokes=" + li.getLocalInvokes() + "; remote invokes=" + li.getRemoteInvokes());
    }
    
    protected void createWorkerThreads() {
        worker = (DelegatingWorker) applicationContext.getBean("testBeanWorker");
        testBeanWorkerThread = new Thread(worker, "testBeanWorker-Thread");
        testBeanWorkerThread.start();
        worker2 = (DelegatingWorker) applicationContext.getBean("orderedWorker");
        orderedWorkerThread = new Thread(worker2, "orderedWorker-Thread");
        orderedWorkerThread.start();
    }
    
    protected void killWorkerThreads() {
        worker.stop();
        worker2.stop();
        try {
			Thread.sleep(700);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

    }
    
    public void testCallITestBeanMethod() throws Exception {
        ITestBean proxy = (ITestBean) applicationContext.getBean("proxy");
        assertEquals("rod", proxy.getName());
    }
    
    public void testCallOrderedMethod() throws Exception {
        Ordered proxy = (Ordered) applicationContext.getBean("proxy");
        assertEquals(1, proxy.getOrder());
    }
    
    public void testNoResponseFromSpace() throws Exception {

        Ordered proxy = (Ordered) applicationContext.getBean("proxy");
        killWorkerThreads();
        assertFalse("worker thread alive ", testBeanWorkerThread.isAlive());
        assertFalse("orderedThread alive ", orderedWorkerThread.isAlive());

        try {
            proxy.getOrder();
            fail("Workers were killed");
        }
        catch (RemoteAccessException ex) {
            // Ok
        }
    }
    
    public void testUnimplementedInterface() throws Exception {
        try {
            Comparable c = (Comparable) applicationContext.getBean("proxy");
            fail("Method should not be implemented");
        }
        catch (ClassCastException ex) {
            // Ok
        }
    }
    
    
    public static class TestOrdered implements Ordered {
        public int getOrder() {
            return 1;
        }
    }


}
